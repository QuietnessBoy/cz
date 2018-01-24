package com.zhjl.tech.inter.biz.filecz;

import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.message.CreateCzMessage;
import com.zhjl.tech.common.utils.TimeTool;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.model.inter.Status;
import com.zhjl.tech.inter.model.inter.TempOrder;
import com.zhjl.tech.inter.rabbitmq.send.CreateFileCzSender;
import com.zhjl.tech.inter.service.inter.IAttestService;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 文件存证业务处理
 */
@Slf4j
@Service
public class FileCzBiz {

    /**
     * 文件存证操作入口
     *
     * @param attestDto
     * @return
     */
    public JsonResult solve(AttestDto attestDto) {

        //调用源文件存证方法
        JsonResult jsonResult = solveInteral(attestDto);

        //如果jsonResult返回成功，则推送消息
        if (!JsonConfig.SolveOkDesc.equals(jsonResult.getMsg())) {
            return jsonResult;
        }

        // rabbitMq推送请求消息至业务处理子系统
        CreateCzMessage createCzMessage = new CreateCzMessage();
        createCzMessage.setChannelOrdersn(attestDto.getChannelOrdersn());
        createFileCzSender.send(createCzMessage);

        return jsonResult;
    }

    /**
     * 源文件存证，数据库相关操作。封装此方法的目的是为了事务。
     *
     * @param attestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult solveInteral(AttestDto attestDto) {

        //参数校验
        JsonResult result = fileCzVerify.verify(attestDto);
        if (result != null) {
            return result;
        }

        //在临时表里存在
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(attestDto.getChannelOrdersn());
        if (tempOrder != null) {
            log.warn("该订单正处于业务受理中，详情请联系管理员.channelOrdersn={}", attestDto.getChannelOrdersn());
            return new JsonResult(true, JsonConfig.HandingDesc, JsonConfig.Handing, null);
        }

        //本订单是否为异常订单
        boolean exceptionOrderFlag = false;

        //Temp无数据,查看正式表attest有ma
        Attest attest = attestService.getAttestByChannelOrdersn(attestDto.getChannelOrdersn());
        if (attest != null) {
            //如果ordersn不存在，说明此时正在业务处理，需要立即返回
            if (StringUtils.isEmpty(attest.getOrdersn())) {
                log.warn("该订单正处于业务受理中，详情请联系管理员.channelOrdersn={}",attest.getChannelOrdersn());
                return new JsonResult(true, JsonConfig.HandingDesc, JsonConfig.Handing, null);
            }

            //订单记录不存在。注意！！这种情况一般是不会发生的！！
            Status status = statusService.getStatusByChannelOrdersn(attestDto.getChannelOrdersn());
            if (status == null) {
                log.warn("订单状态记录不存在.channelOrdersn={}", attestDto.getChannelOrdersn());
                return new JsonResult(false, JsonConfig.SystemExceptionDesc, JsonConfig.SystemException, null);
            }

            //ordersn不为空，那么此时只接收业务异常的订单；不是异常的状态则直接pass
            //attest表不为空  且存在ordersn 且状态为业务处理失败
            // 当状态为solveFailed时候，此流程支持用户重新发送原订单。
            if (attest.getState().equals(SysConfig.SolveFaild)) {
                exceptionOrderFlag = true;
            } else if (attest.getState().equals(SysConfig.BizSolveOk)) {
                log.warn("渠道订单业务处理已完成.ordersn={}", attest.getOrdersn());
                return new JsonResult(false, JsonConfig.RepeatChannelOrdersnDesc, JsonConfig.RepeatChannelOrdersn, null);
            } else {
                log.warn("该订单正处于业务受理中，详情请联系管理员.ordersn={}",attest.getOrdersn());
                return new JsonResult(true, JsonConfig.HandingDesc, JsonConfig.Handing, null);
            }
        }

        //保存临时订单
        saveTemp(attestDto);

        // 执行对处理失败订单操作
        if (exceptionOrderFlag) {
            Status status = statusService.getStatusByChannelOrdersn(attestDto.getChannelOrdersn());
            status.setStateBiz(State.Request_File_Ok);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            log.info("更新订单状态至status表,channelOrdersn={}", attestDto.getChannelOrdersn());
        } else {
            //记录订单当前处理状态
            Status status = new Status();
            status.setChannelOrdersn(attestDto.getChannelOrdersn());
            status.setStateBiz(State.Request_File_Ok);
            status.setUpdateTime(new Date());
            statusService.insertSelective(status);
            log.info("添加订单状态至status表,channelOrdersn={}", attestDto.getChannelOrdersn());
        }

        log.info("[文件存证请求接收完成],channelOrdersn={}", attestDto.getChannelOrdersn());
        return new JsonResult(true, JsonConfig.SolveOkDesc, JsonConfig.SolveOk, null);
    }

    /**
     * 保存临时订单表
     * @param attestDto
     */
    private void saveTemp(AttestDto attestDto) {
        //复制对象属性
        TempOrder tempOrder1 = new TempOrder();
        BeanUtils.copyProperties(attestDto, tempOrder1);
        tempOrder1.setRequestTime(TimeTool.str2DateWithourException(attestDto.getRequestTime()));

        //正常订单请求操作. 添加存证订单固定字段
        //服务商ID
        tempOrder1.setProvinderId(SysConfig.DefaultProvinderId);
        tempOrder1.setVersion(SysConfig.Version);
        // 文件存证类型
        tempOrder1.setAttestType(SysConfig.Attest_Type_File);
        //处理中
        tempOrder1.setState(SysConfig.UnSolve);
        tempOrder1.setStateTime(new Date());
        tempOrder1.setUpdateTime(new Date());
        tempOrderService.insertSelective(tempOrder1);
        log.info("添加新临时存证订单成功.channelOrdersn{}", tempOrder1.getChannelOrdersn());
    }

    @Resource
    private CreateFileCzSender createFileCzSender;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private FileCzVerify fileCzVerify;
}
