package com.zhjl.tech.common.utils;

import com.zhjl.tech.common.exception.CommonException;
import com.zhjl.tech.common.constant.SysConfig;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class TimeTool {

    /**
     * 字符串转日期。内部消化异常。请在有很强自信心的时候使用
     * @param strDate
     * @return
     * @throws CommonException
     */
    public static Date str2DateWithourException(String strDate){
        SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format, Locale.PRC);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            log.error("日期转换失败.", e);
            throw new CommonException("日期转换失败.");
        }
    }

    /**
     *
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDateAfterDays(Date date, int days ){
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        time.add(Calendar.DAY_OF_YEAR, days);
        return time.getTime();
    }

}
