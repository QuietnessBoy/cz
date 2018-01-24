package com.zhjl.tech.inter.biz.query.chained;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import com.zhjl.tech.common.zjlsign.request.RequestSign;
import com.zhjl.tech.inter.configs.EnvConfig;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.model.inter.Channel;
import com.zhjl.tech.inter.service.inter.IAttestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Slf4j
@Service
public class QueryChainedBiz {

    /**
     * 根据数字指纹、存证人身份标识等参数作为条件查询存证订单,同步
     *
     * @param digitalCertificateDto 请求参数对象
     * @return
     */
    @Transactional
    public JsonResult select(DigitalCertificateDto digitalCertificateDto, HttpServletRequest request, HttpServletResponse response) {

        JsonResult jsonResult = queryChainedVerify.verify(digitalCertificateDto);

        // 判断处理是否成功
        if(jsonResult != null){
            log.warn("订单不合法,终止!,Hash:{},ownerId:{}",digitalCertificateDto.getFileHash(),digitalCertificateDto.getOwnerId());
            return jsonResult;
        }

        //根据fileHash/ownerId查询订单是否存在  暂时不做对数据库的操作，如果数据库被更改，不好说明情况，直接跟链上数据进行匹配
        Attest attest = attestService.getAttestByFileHashAndOwnerId(digitalCertificateDto.getFileHash(), digitalCertificateDto.getOwnerId());
        log.info("@@@@@@@@@@@@@@@state:{},channelOrdersn:{}",attest.getState(),attest.getChannelOrdersn());
        if(attest == null){
            log.warn("未通过该数据找到对应订单信息.fileHash={},ownerId={}",digitalCertificateDto.getFileHash(),digitalCertificateDto.getOwnerId());
            return new JsonResult(true,JsonConfig.SearchNoDesc,JsonConfig.SearchNo,null);
        }

        // 匹配上链状态是否一致
        if(!SysConfig.GoChained.equals(attest.getState())){
            log.info("[校验订单结束]:校验失败.区块链暂不存在该存证单.");
            return new JsonResult(true,JsonConfig.ErrorChainedStateDesc,JsonConfig.ErrorChainedState,null);
        }

        log.info("[校验订单结束]:校验成功.{}",attest.getOrdersn());
        return new JsonResult(true,JsonConfig.ChainedOkDesc,JsonConfig.ChainedOk,null);
    }


    @Resource
    private IAttestService attestService;

    @Resource
    private QueryChainedVerify queryChainedVerify;
}
