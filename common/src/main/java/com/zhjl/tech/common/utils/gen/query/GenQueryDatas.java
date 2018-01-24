package com.zhjl.tech.common.utils.gen.query;

import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class GenQueryDatas {

    public QueryOrderDto gen(String ordersn){
        QueryOrderDto queryOrderDto = new QueryOrderDto();
        queryOrderDto.setSignType(comBizConfig.getSignType());
        queryOrderDto.setAccessKey(comBizConfig.getAccessKey());
        queryOrderDto.setRandom(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
        queryOrderDto.setChannelId(comBizConfig.getChannelId());
        queryOrderDto.setOrdersn(ordersn);

        //生成sign
        String sign = QueryRequestSign.genQueryDataSign(queryOrderDto,comBizConfig.getChannelPublickKey());
        queryOrderDto.setSign(sign);

        return queryOrderDto;
    }


    @Resource
    private ComBizConfig comBizConfig;
}
