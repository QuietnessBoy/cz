package com.zhjl.tech.inter.biz.hashcz;

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
import com.zhjl.tech.inter.rabbitmq.send.CreateHashCzSender;
import com.zhjl.tech.inter.service.inter.IAttestService;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 文件存证具体操作
 */
@Slf4j
@Service
public class FileHashCzBiz {

    /**
     * 文件Hash存证方法
     *
     * @param attestDto
     * @return
     */
    public JsonResult solve(AttestDto attestDto) {
        //调用文件Hash存证方法
        JsonResult jsonResult = solveInternal(attestDto);

        // 判断处理是否成功
        if (!jsonResult.isSuccess()) {
           log.warn("订单不合法,终止!,{}",attestDto.getChannelOrdersn());
           return jsonResult;
        }

        // rabbitMq推送消息至业务处理子系统
        CreateCzMessage createCzMessage = new CreateCzMessage();
        createCzMessage.setChannelOrdersn(attestDto.getChannelOrdersn());
        createHashCzSender.send(createCzMessage);
        return jsonResult;
    }

    /**
     * 文件Hash文件存证
     *
     * @param attestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult solveInternal(AttestDto attestDto) {
        JsonResult result = fileHashCzVerify.verify(attestDto);
        if (result != null){
            return result;
        }

        // 判断临时订单表是否存在该记录
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(attestDto.getChannelOrdersn());
        if(tempOrder != null){
            log.warn("该存证订单正被受理，请联系管理员.channelOrdersn={}",attestDto.getChannelOrdersn());
            return new JsonResult(true,JsonConfig.HandingDesc,JsonConfig.Handing,null);
        }

        // 判断正式订单是否存在该记录
        Attest attest_local = attestService.getAttestByChannelOrdersn(attestDto.getChannelOrdersn());
        if (attest_local != null) {
            log.info("文件Hash存证:渠道订单号已存在，忽略本次请求,channelOrdersn={}", attestDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.RepeatChannelOrdersnDesc,JsonConfig.RepeatChannelOrdersn,null);
        }

        //保存到temp表
        saveTemp(attestDto);

        //更新订单当前状态
        Status status = new Status();
        status.setChannelOrdersn(attestDto.getChannelOrdersn());
        //文件Hash存证接收处理完成
        status.setStateBiz(State.Solve_FileHashCz_Ok);
        status.setUpdateTime(new Date());
        statusService.insertSelective(status);

        log.info("[接收文件Hash存证请求结束.]channelOrdersn={}",attestDto.getChannelOrdersn());
        return new JsonResult(true,JsonConfig.SolveOkDesc,JsonConfig.SolveOk,null);
    }

    private void saveTemp(AttestDto attestDto) {
        // 复制对象
        TempOrder tempOrder1 = new TempOrder();
        BeanUtils.copyProperties(attestDto, tempOrder1);

        //添加文件Hash存证订单固定字段
        tempOrder1.setRequestTime(TimeTool.str2DateWithourException(attestDto.getRequestTime()));
        tempOrder1.setAttestType(SysConfig.Attest_Type_Hash);
        tempOrder1.setProvinderId(SysConfig.DefaultProvinderId);
        tempOrder1.setVersion(SysConfig.Version);
        tempOrder1.setState(SysConfig.UnSolve);
        tempOrder1.setStateTime(new Date());
        tempOrder1.setUpdateTime(new Date());
        tempOrderService.insertSelective(tempOrder1);
    }

    @Resource
    private CreateHashCzSender createHashCzSender;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    FileHashCzVerify fileHashCzVerify;
}





