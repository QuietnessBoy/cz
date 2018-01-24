package com.zhjl.tech.attest.util;


/**
 * 计算天数
 */
public class ComputeTime{

    public static int days_of_one_year = 365;

    /**
     * 使用范围：申请订单号的时候，需要传递一个以年为单位的存证时长字段。
     * 输入天数，输出这些天数对应的年数。 Y:时间期限\1一年\2二年\3五年\4十年\5长期
     * @param day
     * @return
     */
    public static String convertDaysToYear(int day){
        String result;
        if(day<=days_of_one_year){
            result = "1";
        }else if( day<=days_of_one_year*2 ){
            result = "2";
        }else if(day<=days_of_one_year*3){
            result = "3";
        }else if(day<=days_of_one_year*4){
            result = "4";
        }else{
            result = "5";
        }
        return result;
    }

}
