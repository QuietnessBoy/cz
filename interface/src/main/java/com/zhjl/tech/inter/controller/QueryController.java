package com.zhjl.tech.inter.controller;

import com.zhjl.tech.common.annotation.controllers.ZhijlCtrl;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.inter.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.inter.biz.query.chained.QueryChainedBiz;
import com.zhjl.tech.inter.biz.query.order.QueryOrdersnBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class QueryController extends BaseController {

    /**
     * 根据订单号查询存证订单
     * URL=http://localhost:8080/inter/queryAttest?ordersn=005341171127000132&signType=MD5&sign=PdJz6sMywqk/N49o4mEPDg==&accessKey=2kDv8FXEzb69pk261PXPQ65KezG2fzbbHX7SrUEYkkabk74yHa&random=qewqlkazwessszxcv&channelId=005     *
     * @param queryOrderDto 接收订单查询所需要的参数对象
     */
    @ZhijlCtrl
    @ResponseBody
    @RequestMapping("queryAttest")
    @ZhijlLog(rquestMethod="Http",orderSn = "p[2].getOrdersn")
    public JsonResult selectAttestFile( HttpServletRequest request,
                                        HttpServletResponse response,
                                        QueryOrderDto queryOrderDto) {
        return queryOrdersnBiz.selectAttestByOrdersn(queryOrderDto);
    }

    /**
     * 根据数字指纹、存证人身份标识等参数作为条件查询存证订单,同步
     */
    @ZhijlCtrl
    @ResponseBody
    @RequestMapping("verifyAttest")
    @ZhijlLog(rquestMethod="Http")
    public JsonResult selectAttestFileByDigital( HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 DigitalCertificateDto digitalCertificateDto) {
        return queryChainedBiz.select(digitalCertificateDto,request,response);
    }


    @Resource
    private QueryOrdersnBiz queryOrdersnBiz;

    @Resource
    private QueryChainedBiz queryChainedBiz;

}