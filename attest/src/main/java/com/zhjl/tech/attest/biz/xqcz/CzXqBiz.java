package com.zhjl.tech.attest.biz.xqcz;

import com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.attest.biz.common.CommonBiz;
import com.zhjl.tech.attest.biz.filecz.FileCzBizDos;
import com.zhjl.tech.attest.configs.EnvConfig;
import com.zhjl.tech.attest.model.attest.*;
import com.zhjl.tech.attest.service.attest.*;
import com.zhjl.tech.attest.util.ChainedTool;
import com.zhjl.tech.attest.util.OrdersnGeneration;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.utils.TimeTool;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzAttestSign;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzPlatformSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 续期存证
 */
@Service
@Slf4j
public class CzXqBiz{

    /**
     * 存证续期业务处理，推送消息至业务接口/文件处理子系统
     *
     * @param createAttestDetailMessage
     */
    @ZhijlLog(rquestMethod="rabbitMQ",ChannelorderSn = "p[0].getChannelOrdersn",orderSn="r")
    public String solveCzXq(CreateAttestDetailMessage createAttestDetailMessage) {

            //调用续期业务处理方法
            Attest attest = xqsolve(createAttestDetailMessage);
            //异步回调
            czXqNotify.XqCzNotify(attest);

            // 保存上链信息
            chainedTool.saveDataToBlockChain(attest.getOrdersn());

            return attest.getOrdersn();
    }


    /**
     * 续期业务处理
     *
     * @param createAttestDetailMessage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @ZhijlLog(rquestMethod="rabbitMQ",orderSn="r.getOrdersn",ChannelorderSn = "p[0].getChannelOrdersn")
    public Attest xqsolve(CreateAttestDetailMessage createAttestDetailMessage) {
        TempOrder tempOrder = czXqBizDos.getTempOrderAndLock(createAttestDetailMessage);
        if (tempOrder == null){
            return null;
        }

        //判断最初的存证记录是否存在
        Attest originalAttest = attestService.getAttestByOrdersn(createAttestDetailMessage.getOrdersn());
        if (originalAttest == null) {
            log.warn("续期订单异常.ordersn={}",createAttestDetailMessage.getOrdersn());
            throw new AttestBizException("续期订单没要找到原始订单！ordersn="+createAttestDetailMessage.getOrdersn());
        }

        //****************此处查找之前的最近一条订单********************
        Attest lastestAttest = attestService.getlastestAttestByOrdersn(createAttestDetailMessage.getOrdersn(), SysConfig.BizSolveOk, SysConfig.GoChained);
        if(lastestAttest == null){
            log.warn("待续期订单不存在.ordersn={}",createAttestDetailMessage.getOrdersn());
            throw new AttestBizException("续期订单没要找到最近的订单！ordersn="+createAttestDetailMessage.getOrdersn());
        }

        //复制对象
        Attest newAttest = new Attest();
        BeanUtils.copyProperties(tempOrder,newAttest);

        // 根据channelOrdersn查找status表中对应订单记录
        Status status = statusService.getStatusByChannelOrdersn(createAttestDetailMessage.getChannelOrdersn());

        //判断订单状态表是否存在该订单记录，如果不存在，则返回
        if (status == null) {
            log.error("订单状态表未找到该订单记录.channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn());
            throw new AttestBizException("续期订单没要找到状态记录！ordersn="+createAttestDetailMessage.getOrdersn());
        }

        //判断该存证续期订单状态是否匹配"[czxq]存证续期接收处理完成1"
        if (!status.getStateBiz().equals(State.Solve_Xq_Ok)) {
            log.error("订单状态不匹配该流程处理状态.state={}",status.getStateBiz());
            throw new NormalException("续期订单没要找到原始订单！ordersn="+createAttestDetailMessage.getOrdersn());
        }

        //根据channelID查找对应渠道信息
        Channel channel = EnvConfig.channelMap.get(newAttest.getChannelId());

        /////////////////////// temp operation start
        // 判断用户是否存在，获取用户公钥信息，生成attestSign
        SM2KeyPair sm2KeyPair = commonBiz.genUserKeyPair(newAttest);

        //生成订单的用户签名信息
        AttestXqDto attestXqDto1 = new AttestXqDto();
        BeanUtils.copyProperties(newAttest,attestXqDto1);

        String attestSign = XqCzAttestSign.genSign(sm2KeyPair,channel.getChannelPublicKey() , channel.getChannelIda(), attestXqDto1);
        newAttest.setAttestSign(attestSign);
        /////////////////////// temp operation end

        //调用servlet请求获取存证号
        String ordersn = ordersnGeneration.gen( newAttest);
        if(StringUtils.isBlank(ordersn)){
            log.warn("[czxqbiz]获取ordersn失败,ordersn={}",newAttest.getOrdersn());
            throw new AttestBizException("获取ordersn失败，ordersn="+ ordersn);
        }

        fileCzBizDos.checkDumplicateOrdersn(ordersn);

        //转存证请求时间格式
        AttestXqDto attestXqDto = new AttestXqDto();
        BeanUtils.copyProperties(newAttest, attestXqDto);
        attestXqDto.setRequestTime(TimeTool.date2Str(newAttest.getRequestTime()));

        // 计算expireTime
        newAttest.setStartTime(lastestAttest.getExpiredTime());
        newAttest.setExpiredTime(TimeTool.getDateAfterDays(newAttest.getStartTime(), Integer.valueOf(newAttest.getDuration())));

        // 生成platformsign
        attestXqDto.setOrdersn(ordersn);
        attestXqDto.setStartTime(TimeTool.date2Str(lastestAttest.getExpiredTime()));
        String platformSign = XqCzPlatformSign.genSign(attestXqDto, channel.getPlatformPublickKey(),channel.getPlatformPrivateKey(),channel.getPlatformIda());
        newAttest.setPlatformSign(platformSign);
        log.info("生成续期订单号:ordersn={}和续期platformSign={}", ordersn, platformSign);

        // 添加attest对象
        newAttest.setAncestorsOrdersn(originalAttest.getAncestorsOrdersn());
        newAttest.setParentOrdersn(lastestAttest.getOrdersn());
        newAttest.setOrdersn(ordersn);
        newAttest.setRequestTime(createAttestDetailMessage.getRequestTime());
        newAttest.setState(SysConfig.BizSolveOk);
        newAttest.setDuration(createAttestDetailMessage.getDuration());
        newAttest.setStateTime(new Date());
        newAttest.setUpdateTime(new Date());
        attestService.insertSelective(newAttest);

        //删除临时表存证记录
        tempOrderService.deleteById((long)tempOrder.getId());

        //更新订单状态表对应订单
        status.setOrdersn(ordersn);
        status.setStateBiz(State.Solve_XqBiz_Ok);
        status.setUpdateTime(new Date());
        statusService.updateSelectiveById(status);

        log.info("[续期业务处理完毕],ordersn={}",ordersn);
        return newAttest;
    }


    @Resource
    private OrdersnGeneration ordersnGeneration;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private CzXqNotify czXqNotify;

    @Resource
    private CommonBiz commonBiz;

    @Resource
    private ChainedTool chainedTool;

    @Resource
    CzXqBizDos czXqBizDos;

    @Resource
    FileCzBizDos fileCzBizDos;
}