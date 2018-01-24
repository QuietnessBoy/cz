package com.zhjl.tech.ordersn.biz;

import com.zhjl.tech.ordersn.model.ordersn.Attest;
import com.zhjl.tech.ordersn.service.ordersn.IAttestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GenOrdersn {

    @Resource
    private IAttestService attestService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    String isTody_sql = "select is_today();";

    String next_sql = "select nextval();";

    String again_sql = "select again();";


    /**
     * 事务边界
     * @param channelId
     * @param buninessType
     * @param bizType
     * @param duration
     * @return
     */
    @Transactional
    public String genOrdersn(String channelId, String buninessType, String bizType, String duration){
        String dateStr = new SimpleDateFormat("yyMMdd").format(new Date());
        String nextStr =null;
        String ordersn = null;
        Attest attest = null;
        if (!isToday()) {
            again();
        }

        do{
            StringBuilder sb = new StringBuilder();
            nextStr = nextVal().toString();
            nextStr = StringUtils.leftPad(nextStr, 6, "0");
            ordersn = sb.append(channelId).append(buninessType).append(bizType)
                    .append(duration).append(dateStr).append(nextStr).toString();

            attest = attestService.getAttestByOrdersn(ordersn);
            if(attest != null){
                log.warn("订单号重复,重新生成.ordersn={}",ordersn);
            }
        }while (attest != null);

        log.info("生成的订单号.ordersn={}.", ordersn);
        return ordersn;
    }

    /**
     * 请求时间校验
     */
    private boolean isToday() {
        int rs = -1;
        List<Map<String, Object>> lst = jdbcTemplate.queryForList(isTody_sql);
        for (Map<String, Object> map : lst) {
            rs = (Integer) map.get("is_today()");
        }

        return rs == 1 ? true : false;
    }

    /**
     * 请求下一个值
     */
    private Integer nextVal() {
        List<Map<String, Object>> lst = jdbcTemplate.queryForList(next_sql);
        for (Map<String, Object> map : lst) {
            return (Integer) map.get("nextval()");
        }
        return -1;
    }

    /**
     * 重置归零
     */
    private void again() {
        jdbcTemplate.execute(again_sql);
    }
}
