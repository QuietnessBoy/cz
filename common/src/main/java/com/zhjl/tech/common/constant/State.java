package com.zhjl.tech.common.constant;

/**
 * @author wind
 * 订单状态字典 use it
 */
public class State {
    public static final String Request_File_Ok ="[File]文件存证请求处理完成1";             
    public static final String Solve_FileCz_Ok ="[File]文件存证业务处理完成2";             
    public static final String Receive_File_Gen_BlockFile_Ok="[File]文件存证完成3";
    public static final String File_Failed="[File]文件存证处理失败4";
    public static final String CallBack_File_Success="[File]存证成功,回调成功5";
    public static final String CallBack_File_Failed="[File]存证成功,回调失败6";
    public static final String FailedAndCallBackFailed="[File]处理失败,回调失败7";
    public static final String FailedAndCallBackSuccess="[File]处理失败,回调成功8";

    public static final String Solve_FileHashCz_Ok ="[Hash]Hash存证接收处理完成1";            
    public static final String Solve_Biz_Ok="[Hash]Hash存证业务处理完成2";                     
    public static final String CallBack_Hash_Success="[Hash]存证成功,回调成功3";
    public static final String CallBack_Hash_Failed="[Hash]存证成功,回调失败4";
    
    public static final String Solve_Xq_Ok ="[czxq]存证续期接收处理完成1";                      
    public static final String Solve_XqBiz_Ok="[czxq]存证续期业务处理完成2";                    
    public static final String CallBack_Xq_Success="[czxq]续期成功,回调成功3";
    public static final String CallBack_Xq_Failed="[czxq]续期成功,回调失败4";

}
