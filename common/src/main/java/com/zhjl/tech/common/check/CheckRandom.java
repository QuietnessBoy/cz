package com.zhjl.tech.common.check;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CheckRandom {

    private static Map<String,Record> records = new ConcurrentHashMap<>();
    private static Integer max_length = 2500;
    private static Integer remain_count = 500;
    private static Integer same_request_allow_count = 2;

    /**
     * 判断输入随机字符串是否可以继续访问
     * @param randomString 参数
     * @return 授权结果
     */
    public static boolean judge(String randomString ){
        log.info("开始判断请求串,{}", randomString);
        if(StringUtils.isBlank(randomString)){
            return false;
        }
        //重整旗鼓，刷新records内容
        if( records.size() >= max_length ){ //达到2000个元素，开始清空其中1500个老的元素
            //todo 此处采用的是随机剩余500个，以后可以进行改进
            Map<String,Record> new_records = new ConcurrentHashMap<>();
            int count = 0;

//            Set<Map.Entry<K, V>>  records.entrySet(

            for( Map.Entry<String,Record> entry : records.entrySet()){

                new_records.put(entry.getKey(), entry.getValue());
                count++;
                if( count >= remain_count ){
                    break;
                }
            }
            records = new_records;
        }

        if( records.containsKey(randomString)){
            if( records.get(randomString).count >= same_request_allow_count ){ //次数达到上限
                log.warn("此请求串短时间内达到{}次请求，被拒绝。", same_request_allow_count);
                return false;
            }else{
                //次数没有达到上限
                Record now = records.get(randomString);
                now.count = now.count + 1;
                return true;
            }
        }else{ //新的请求
            Record record = new Record();
            record.count = 1;
            record.questTime = Calendar.getInstance();
            records.put(randomString,record);
            return true;
        }
    }
}
class Record{
    Integer count;
    Calendar questTime;
}
