package com.zhjl.tech.inter.biz.query.order;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.QueryCallBackData;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.service.inter.IAttestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class QueryOrdersnBiz {

    /**
     * 根据存证号查找存证订单
     *
     * @param queryOrderDto 请求参数对象
     * @return
     */
    @Transactional
    public JsonResult selectAttestByOrdersn(QueryOrderDto queryOrderDto) {

        JsonResult jsonResult  = queryOrdersnVerify.verify(queryOrderDto);

        // 判断处理是否成功
        if (jsonResult != null) {
            log.warn("订单不合法,终止!,{}",queryOrderDto.getOrdersn());
            return jsonResult;
        }

        // 根据ordersn查找订单，ordersn为存证祖先订单号,state 1为处理完成状态，2为上链完成状态
        Attest attest = attestService.getlastestAttestByOrdersn(queryOrderDto.getOrdersn(), SysConfig.BizSolveOk, SysConfig.GoChained);
        if (attest == null) {
            log.warn("根据存证号:{}.未找到该订单.", queryOrderDto.getOrdersn());
            return new JsonResult(true, JsonConfig.SearchNoDesc,JsonConfig.SearchNo,null);
        }
        log.info("找到订单.{}", JSONObject.toJSONString(attest));

        //准备返回对象
        QueryCallBackData queryCallBackData = new QueryCallBackData();
        BeanUtils.copyProperties(attest, queryCallBackData);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        queryCallBackData.setStartTime(simpleDateFormat.format(attest.getStartTime()));

        return new JsonResult(true,JsonConfig.SearchOkDesc,JsonConfig.SearchOk,queryCallBackData);
    }


    @Resource
    private IAttestService attestService;

    @Resource
    private QueryOrdersnVerify queryOrdersnVerify;
}

