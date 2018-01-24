package com.zhjl.tech.inter.biz.czxq;

import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.utils.TimeTool;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.model.inter.Status;
import com.zhjl.tech.inter.model.inter.TempOrder;
import com.zhjl.tech.inter.rabbitmq.send.CreateCzXqSender;
import com.zhjl.tech.inter.service.inter.IAttestService;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CzXqBiz {

    @Resource
    private CreateCzXqSender createCzXqSender;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private  IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private CzXqVerify czXqVerify;

    public JsonResult solve(AttestXqDto attestXqDto) {
        JsonResult jsonResult = solveInternal(attestXqDto);

        //找到对应临时订单记录
        if (!jsonResult.isSuccess()) {
            log.warn("订单不合法,终止!,channelOrdersn={}",attestXqDto.getChannelOrdersn());
            return jsonResult;
        }

        // rabbitMq推送消息至业务处理子系统
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(attestXqDto.getChannelOrdersn());
        CreateAttestDetailMessage createAttestDetailMessage = new CreateAttestDetailMessage();
        BeanUtils.copyProperties(tempOrder, createAttestDetailMessage);
        createCzXqSender.send(createAttestDetailMessage);
        return jsonResult;
    }

    /**
     * 存证续期
     *
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult solveInternal(AttestXqDto attestXqDto) {

        JsonResult result = czXqVerify.verify(attestXqDto);
        if (result != null){
            return result;
        }

        //判断临时表（tempOrder）存证订单是否存在
        TempOrder tempOrder1 = tempOrderService.getTempOrderByChannelOrdersn(attestXqDto.getChannelOrdersn());
        if(tempOrder1 != null){
            log.warn("该订单正处于业务受理中，详情请联系管理员,channelOrdersn={}", attestXqDto.getChannelOrdersn());
            return new JsonResult(true,new StringBuilder().append(JsonConfig.HandingDesc).append(attestXqDto.getChannelOrdersn()).toString(),JsonConfig.Handing,null);
        }

        //根据渠道号判断订单是否重复
        Attest attest2 = attestService.getAttestByChannelOrdersn(attestXqDto.getChannelOrdersn());
        if (attest2 != null) {
            log.warn("channelOrdersn不能重复.channelOrdersn={}", attestXqDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.ExistChannelOrdersnDesc,JsonConfig.ExistChannelOrdersn,null);
        }

        //****************此处查找之前的最近一条订单是否过期********************
        Attest lastestAttest = attestService.getlastestAttestByOrdersn(attestXqDto.getOrdersn(), SysConfig.BizSolveOk, SysConfig.GoChained);
        Attest attest = attestService.getAttestByOrdersn(attestXqDto.getOrdersn());
        result = czXqVerify.checklastestAttest(attestXqDto, lastestAttest, attest);
        if (result != null) {
            return result;
        }

        result = czXqVerify.checkContent(attestXqDto, attest);
        if (result != null) {
            return result;
        }

        // 复制对象属性，存入数据库中
        TempOrder tempOrder = new TempOrder();
        BeanUtils.copyProperties(attestXqDto,tempOrder);

        tempOrder.setRequestTime(TimeTool.str2DateWithourException(attestXqDto.getRequestTime()));
        tempOrder.setAttestType(attest.getAttestType());
        tempOrder.setVersion(SysConfig.Version);
        tempOrder.setProvinderId(SysConfig.DefaultProvinderId);
        tempOrder.setState(SysConfig.UnSolve);
        tempOrder.setStateTime(new Date());
        tempOrder.setUpdateTime(new Date());
        tempOrder.setOriginTime(attest.getOriginTime());
        tempOrderService.insertSelective(tempOrder);

        //更新订单当前状态，针对续期订单的并发情况,
        Status status = new Status();
        status.setChannelOrdersn(attestXqDto.getChannelOrdersn());
        //文件存证接收处理完成
        status.setStateBiz(State.Solve_Xq_Ok);
        status.setUpdateTime(new Date());
        statusService.insertSelective(status);

        return new JsonResult(true,"续期请求成功!channelOrdersn="+attestXqDto.getChannelOrdersn(),JsonConfig.SolveOk,null);
    }
}
