package com.zhjl.tech.tasks.biz.chained;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.chained.protobuf.CcOpt;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.AttestChainedDTO;
import com.zhjl.tech.common.dto.chain.AddCzChainRequestObj;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.tasks.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.tasks.configs.EnvConfig;
import com.zhjl.tech.tasks.model.tasks.Attest;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.service.tasks.IAttestChainedService;
import com.zhjl.tech.tasks.service.tasks.IAttestService;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * 请求上链接口
 */
@Slf4j
@Service
public class DataToBlockChain {

    @Resource
    private IAttestChainedService attestChainedService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IWarningService warningService;

    /**
     * 调用上链接口
     *
     * @param attestChained
     */
    @Transactional(rollbackFor = Exception.class)
    @ZhijlLog(rquestMethod="Normal",ChannelorderSn = "p[0].getChannelOrdersn",orderSn="p[0].getOrdersn")
    public void send(AttestChained attestChained) {
        log.info("[开始调用区块链存证接口],ordersn={}",attestChained.getOrdersn());

        //查询存证订单信息
        Attest attest = attestService.getAttestByOrdersn(attestChained.getOrdersn());
        if (attest == null) {
            log.error("未找到对应订单记录,终止操作.ordersn={}",attestChained.getOrdersn());
            return;
        }

        //生成txid
        String txid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        attestChained.setTxid(txid);
        attestChainedService.updateSelectiveById(attestChained);

        //发送数据
        AddCzChainRequestObj addCzChainRequestObj = genAddCzChainRequestObj(attest, txid);
        dataToBlockChain.sendRequest(addCzChainRequestObj);

        log.info("[调用区块链存证接口完成.]ordersn={}",attestChained.getOrdersn());
    }

    @Resource
    DataToBlockChain dataToBlockChain;

    /**
     * 调用增加存证上链接口
     *
     * @param addCzChainRequestObj
     */
    @Transactional(rollbackFor = Exception.class)
    @ZhijlLog(rquestMethod="Normal",orderSn="p[0].getOrdersn")
    public void sendRequest(AddCzChainRequestObj addCzChainRequestObj) {
        String chainedUrl = EnvConfig.configChannnelIdMap.get(SysConfig.Config_type_zjl).get(SysConfig.add_cz_chained_url);
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(chainedUrl);

        //设置传输格式
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");

        // 将字段转换成json格式进行传输,用此代码可保证参数首字母大写，如果通过JSON.toJSONString，则会出现首字母小写问题
        JSONObject obj = new JSONObject();
        obj.put("Ordersn", addCzChainRequestObj.getOrdersn());
        obj.put("Sign", addCzChainRequestObj.getSign());
        obj.put("Txid", addCzChainRequestObj.getTxid());
        obj.put("CzObj", addCzChainRequestObj.getCzObj());
        String result = "";
        try {
            StringEntity s = new StringEntity(JSON.toJSONString(addCzChainRequestObj), SysConfig.CharsetName);
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);

            // 发送请求
            HttpResponse response = client.execute(post);

            // 获取响应输入流
            InputStream inStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, SysConfig.CharsetName));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                strber.append(line).append("\n");
            }
            inStream.close();
            reader.close();
            result = strber.toString();
            log.info("上链请求结果.ordersn={},result={}",addCzChainRequestObj.getOrdersn(),result);
            //通过订单号查找需要上链订单
            AttestChained attestChained = attestChainedService.getAttestChainedByOrdersn(addCzChainRequestObj.getOrdersn());

            //判断请求是否超时
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_GATEWAY_TIMEOUT) {
                attestChained.setState(SysConfig.FailedChainedAddCz);    // -1 请求失败
                attestChained.setRemark("请求区块链接口超时.");
                attestChainedService.updateSelectiveById(attestChained);
                log.info("请求区块链接口超时.ordersn={}", attestChained.getOrdersn());
                return;
            }

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                attestChained.setState(SysConfig.SuccessChainedAddCz);    // 1 请求成功
                attestChained.setRemark("请求区块链接口成功.");
                attestChained.setSendTime(new Date());
                attestChainedService.updateSelectiveById(attestChained);
                log.info("请求区块链接口成功.ordersn={}", attestChained.getOrdersn());
            } else {
                attestChained.setState(SysConfig.FailedChainedAddCz);    // -1 请求失败
                attestChained.setRemark("请求区块链接口失败,服务器未响应");
                attestChainedService.updateSelectiveById(attestChained);
                log.info("请求区块链接口失败.ordersn={}", attestChained.getOrdersn());
            }
        } catch (Exception e) {
            log.error("请求异常.ordersn={}",addCzChainRequestObj.getOrdersn(), e);
        }
    }

    /**
     * 生成上链传输对象
     */
    @Transactional(rollbackFor = Exception.class)
    public AddCzChainRequestObj genAddCzChainRequestObj(Attest attest, String txid) {
        log.info("开始生成上链对象.ordersn={}", attest.getOrdersn());
//        //生成Cz
//        Cz cz = new Cz();
//        cz.setDocType("存证订单");
//        cz.setOrdersn(attest.getOrdersn());
//        cz.setWalletAddr(attest.getWalletAddr());
//        cz.setPublicKey(attest.getPublicKey());
//        cz.setCzDetail("");
//        cz.setExpired("0"); //是否过期
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(State.Format);
//        String date = simpleDateFormat.format(new Date());
//        cz.setChainTime(date);
//
//        //生成CzDetail
//        CzDetail czDetail = new CzDetail();
//        BeanUtils.copyProperties(cz,czDetail);
//        log.info("生成cz:{},czDetail:{}", JSONObject.toJSONString(cz),JSONObject.toJSONString(czDetail));

        //生成CzObj
        AttestChainedDTO attestChainedDTO = new AttestChainedDTO();
        BeanUtils.copyProperties(attest, attestChainedDTO);
        String czObj = CcOpt.genChainStr(attestChainedDTO, SysConfig.No_Encrypt);
        log.info("生成czObj:{}", czObj);
        attest.setChainedContent(czObj);
        attest.setUpdateTime(new Date());
        attestService.updateSelectiveById(attest);

        //生成请求参数对象
        AddCzChainRequestObj addCzChainRequestObj = new AddCzChainRequestObj();
        addCzChainRequestObj.setCzObj(czObj);
        addCzChainRequestObj.setOrdersn(attest.getOrdersn());
        addCzChainRequestObj.setTxid(txid);
        StringBuilder sb = new StringBuilder();
        sb = sb.append("&czObj=").append(czObj).append("&ordersn=").append(attest.getOrdersn()).append("&txid=").append(txid).append("&rune=").append(SysConfig.Rune);
        String finalStr = sb.toString();

        String sign = null;
        try {
            sign = Base64.getEncoder().encodeToString(Hash.getHashMD5(finalStr.getBytes(SysConfig.CharsetName)));
            log.info("生成上链请求sign={},txid={}", sign,txid);
            addCzChainRequestObj.setSign(sign);
        } catch (UnsupportedEncodingException e) {
            Status status = statusService.getStatusByOrdersn(attest.getOrdersn());
            status.setNum(10);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            //进入报警表
            Warning warning = new Warning();
            warning.setNum(status.getNum());
            warning.setOrdersn(status.getOrdersn());
            warning.setChannelOrdersn(attest.getChannelOrdersn());
            warning.setBizType(SysConfig.BizType);
            warning.setRemark("生成attestSign失败");
            warning.setUpdateTime(new Date());
            warningService.insertSelective(warning);
            log.error("sign生成失败.ordersn={}",attest.getOrdersn(), e);
            return null;
        }

        return addCzChainRequestObj;
    }
}
