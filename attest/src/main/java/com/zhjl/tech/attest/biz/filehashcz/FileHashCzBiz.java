package com.zhjl.tech.attest.biz.filehashcz;

import com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.attest.biz.common.CommonBiz;
import com.zhjl.tech.attest.biz.filecz.FileCzBizDos;
import com.zhjl.tech.attest.configs.EnvConfig;
import com.zhjl.tech.attest.model.attest.*;
import com.zhjl.tech.attest.service.attest.*;
import com.zhjl.tech.attest.util.ChainedTool;
import com.zhjl.tech.attest.util.OrdersnGeneration;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.utils.TimeTool;
import com.zhjl.tech.common.zjlsign.filecz.FileCzAttestSign;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzPlatformSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 处理存证业务
 */
@Service
@Slf4j
public class FileHashCzBiz {

    @ZhijlLog(rquestMethod="rabbitMQ",ChannelorderSn = "p[0]",orderSn="r")
    public String sloveHashAttest(String channelOrdersn) {

            // 文件Hash存证
            Attest attest = hashsolve(channelOrdersn);

            //异步回调
            fileHashCzNotify.HashCzNotify(attest);

            //保存上链信息
            chainedTool.saveDataToBlockChain(attest.getOrdersn());

            return attest.getOrdersn();
    }

    /**
     * 文件Hash存证
     *
     * @param channelOrdersn
     * @return
     */
    @Transactional
    public Attest hashsolve(String channelOrdersn) {
        log.info("[业务处理系统开始工作]:接收文件Hash存证参数:channelOrdersn={}", channelOrdersn);

        TempOrder tempOrder = fileHashCzBizDos.getLockAndCheck(channelOrdersn);

        //复制对象
        Attest attest_new = new Attest();
        BeanUtils.copyProperties(tempOrder, attest_new);

        // 获取缓存的channel信息
        Channel channel = EnvConfig.channelMap.get(attest_new.getChannelId());

        /////////////////////// temp operation start
        //2.判断用户是否存在？不存在：1.生成用户；2.验证用户签名是否正确（1.0版本实现）3.生成签名
        SM2KeyPair sm2KeyPair = commonBiz.genUserKeyPair(attest_new);

        AttestDto attestDto = new AttestDto();
        BeanUtils.copyProperties(attest_new, attestDto);
        //生成订单的用户签名信息attestSign
        String attestSign = FileCzAttestSign.gen(sm2KeyPair, channel.getChannelPublicKey(), channel.getChannelIda(), attestDto);
        attest_new.setAttestSign(attestSign);
        /////////////////////// temp operation end

        //调用servlet请求获取存证号
        String ordersn = ordersnGeneration.gen(attest_new);
        fileCzBizDos.checkDumplicateOrdersn(ordersn);

        //时间格式转换-requestTime
        attestDto.setRequestTime(TimeTool.date2Str(attest_new.getRequestTime()));

        //todo 重要的时间
        Date nowDate = new Date();
        attest_new.setStartTime(nowDate);
        attest_new.setOriginTime(nowDate);

        // 计算expireTime
        attest_new.setExpiredTime(TimeTool.getDateAfterDays(attest_new.getStartTime(), Integer.valueOf(attest_new.getDuration())));

        //生成Hash存证platformSign
        attestDto.setOrdersn(ordersn);
        attestDto.setStartTime(TimeTool.date2Str(new Date()));
        String platformSign = HashCzPlatformSign.gen(attestDto, channel.getPlatformPublickKey(), channel.getPlatformPrivateKey());
        attest_new.setPlatformSign(platformSign);

        attest_new.setState(SysConfig.BizSolveOk);
        attest_new.setAncestorsOrdersn(ordersn);
        attest_new.setOrdersn(ordersn);
        attest_new.setStateTime(new Date());
        attest_new.setUpdateTime(null);
        attest_new.setCreateTime(null);
        attestService.insertSelective(attest_new);

        //删除临时表存证记录
        tempOrderService.deleteById((long)tempOrder.getId());

        //更新订单状态
        Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
        status.setOrdersn(ordersn);
        status.setStateBiz(State.Solve_Biz_Ok);
        status.setRemark("存证完成.");
        status.setUpdateTime(new Date());
        statusService.updateSelectiveById(status);

        log.info("[业务处理系统工作结束]channelOrdersn={},ordersn={}", channelOrdersn,ordersn);
        return attest_new;
    }



    @Resource
    private FileCzBizDos fileCzBizDos;

    @Resource
    private FileHashCzBizDos fileHashCzBizDos;

    @Resource
    private FileHashCzNotify fileHashCzNotify;

    @Resource
    private OrdersnGeneration ordersnGeneration;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private ChainedTool chainedTool;

    @Resource
    private CommonBiz commonBiz;
}
