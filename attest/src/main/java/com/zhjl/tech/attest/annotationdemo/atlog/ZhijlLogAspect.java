package com.zhjl.tech.attest.annotationdemo.atlog;

import com.zhjl.tech.attest.annotationdemo.ThreadLocalVar;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.chain.AddCzChainRequestObj;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.message.CreateCzMessage;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class ZhijlLogAspect {
    private String direction_start ="start";
    private String direction_end ="end";

    //todo 从此处获取spring上下文的信息

    @Pointcut("@annotation(com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog)")
    private void zjllog(){}//定义一个切入点

    @Resource
    ThreadLocalVar threadLocalVar;

    @Around(value ="zjllog() && @annotation(annotation) &&args(object,..) ")
    public Object interceptorApplogic(ProceedingJoinPoint pj,
                                      ZhijlLog annotation, Object object) throws Throwable {


        long startTime = System.currentTimeMillis();

        //拼接参数内容,打印普通日志
        String targetName = pj.getTarget().getClass().getName();

        //获取日志对象
        Logger logger = getOriLogger(targetName);

        StringBuffer stringBuffer = new StringBuffer();

        Object[] arguments = pj.getArgs();   //获得参数列表

        if(arguments.length<=0){
            stringBuffer.append("no params!");
        }else{
            for(int i=0;i<arguments.length;i++){
                if(         ( ! (arguments[i] instanceof HttpServletRequest ) )
                        &&  ( ! (arguments[i] instanceof HttpServletResponse) )
                        ){
                    stringBuffer.append("{")
                            .append(arguments[i].toString())
                            .append("},");
                }
            }
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);

        //获取接口名称
        String methodName = pj.getSignature().getName();
        //获取traceID
        String orderSn="";
        String ChannelorderSn="";
        //        if(
//            //http方法
//                methodName.equals("fileAttest")
//                        ||  methodName.equals("HashAttest")
//                        ||  methodName.equals("attestContinue")
//                        //其他方法
//                        //文件存证
//                        ||  methodName.equals("solveFileCz")
//                        ||  methodName.equals("solveNormal")
//                        ||  methodName.equals("solveFileCzFile")
//                        ||  methodName.equals("solveFile")
//                        ||  methodName.equals("fileCzSuccessNotify")
//                        ||  methodName.equals("saveDataToBlockChain")
//                        //hash存证
//                        ||  methodName.equals("sloveHashAttest")
//                        ||  methodName.equals("Hashsolve")
//                        ||  methodName.equals("HashCzNotify")
//                        //续期
//                        ||  methodName.equals("solveCzXq")
//                        ||  methodName.equals("xqsolve")
//                        ||  methodName.equals("XqCzNotify")
//                        //上链
//                        ||  methodName.equals("sendRequest")
//                ){
//            //http
//            if(methodName.equals("fileAttest")){
//                AttestDto attestDto=(AttestDto)arguments[2];
//                ChannelorderSn=attestDto.getChannelOrdersn();
//            }
//            if(methodName.equals("HashAttest")){
//                AttestDto attestDto=(AttestDto)arguments[2];
//                ChannelorderSn=attestDto.getChannelOrdersn();
//            }
//            if(methodName.equals("attestContinue")){
//                AttestXqDto attestXqDto=(AttestXqDto)arguments[2];
//                ChannelorderSn=attestXqDto.getChannelOrdersn();
//            }
//            //MQ和normal
//            //文件存证
//            if(methodName.equals("solveFileCz")){
//                ChannelorderSn=(String)arguments[0];
//            }
//            if(methodName.equals("solveNormal")){
//                ChannelorderSn=(String)arguments[0];//channelordersn
//            }
//            if(methodName.equals("solveFileCzFile")){
//                CreateFileDataMessage createFileDataMessage=(CreateFileDataMessage)arguments[0];
//                orderSn=createFileDataMessage.getOrdersn();
//            }
//            if(methodName.equals("solveFile")){
//                CreateFileDataMessage createFileDataMessage=(CreateFileDataMessage)arguments[0];
//                orderSn=createFileDataMessage.getOrdersn();
//            }
//            if(methodName.equals("fileCzSuccessNotify")){
//                Attest attest=(Attest)arguments[0];
//                ChannelorderSn=attest.getChannelOrdersn();
//            }
//            if(methodName.equals("saveDataToBlockChain")){
//                orderSn=(String)arguments[0];
//            }
//            //hash存证
//            if(methodName.equals("sloveHashAttest")){
//                ChannelorderSn=(String)arguments[0];
//            }
//            if(methodName.equals("Hashsolve")){
//                ChannelorderSn=(String)arguments[0];//channelordersn
//            }
//            if(methodName.equals("HashCzNotify")){
//                Attest attest=(Attest)arguments[0];
//                ChannelorderSn=attest.getChannelOrdersn();
//            }
//            //续期
//            if(methodName.equals("solveCzXq")){
//                CreateAttestDetailMessage createAttestDetailMessage=(CreateAttestDetailMessage)arguments[0];
//                ChannelorderSn=createAttestDetailMessage.getChannelOrdersn();
//            }
//            if(methodName.equals("xqsolve")){
//                CreateAttestDetailMessage createAttestDetailMessage=(CreateAttestDetailMessage)arguments[0];
//                ChannelorderSn=createAttestDetailMessage.getChannelOrdersn();
//            }
//            if(methodName.equals("XqCzNotify")){
//                Attest attest=(Attest)arguments[0];
//                ChannelorderSn=attest.getChannelOrdersn();
//            }
//            //上链
//            if(methodName.equals("sendRequest")){
//                AddCzChainRequestObj addCzChainRequestObj=(AddCzChainRequestObj)arguments[0];
//                orderSn=addCzChainRequestObj.getOrdersn();
//            }
//        }
//        String snType= annotation.Sntype();
//        String bizIdLocation = annotation.Sn();
//        if(snType.equals("channelOrdersn")) ChannelorderSn=getBizId(bizIdLocation,pj);
//        if(snType.equals("ordersn")) orderSn=getBizId(bizIdLocation,pj);
//        orderSn=annotation.orderSn();
//        ChannelorderSn=annotation.orderSn();
        if((!StringUtils.isEmpty(annotation.orderSn()))||(!StringUtils.isEmpty(annotation.ChannelorderSn()))){
            if(annotation.ChannelorderSn().startsWith("p")){
                ChannelorderSn= getBizId(annotation.ChannelorderSn(),pj);
            }
            if(annotation.orderSn().startsWith("p")){
                orderSn= getBizId(annotation.orderSn(),pj);
            }
        }

        //获取请求方式---请求方式与方法获取参数方式有关
        String requestMethod=annotation.rquestMethod();
        if(requestMethod.equals("Http")){
            HttpServletRequest request = (HttpServletRequest)arguments[0];
            requestMethod=requestMethod+"-"+request.getRemoteAddr();
        }

        //获取txid
        String txid=UUID.randomUUID().toString().replaceAll("-","");

        //tod 获取执行状态
        String status="normal";

        //to 获取参数内容
        String param=stringBuffer.toString();
        String separation="&zhijinlian_watch&,";
        stringBuffer = new StringBuffer();
        stringBuffer.append(methodName);
        stringBuffer.append(",");
        stringBuffer.append(requestMethod);
        stringBuffer.append(",");
        stringBuffer.append(txid);
        stringBuffer.append(",");
        stringBuffer.append(orderSn);
        stringBuffer.append(",");
        stringBuffer.append(ChannelorderSn);
        stringBuffer.append(",");
        stringBuffer.append(direction_start);
        stringBuffer.append(",");
        stringBuffer.append(status);
        stringBuffer.append(",");
        String usetime="-1";
        stringBuffer.append(usetime);
        stringBuffer.append(",");
        stringBuffer.append(separation);
        stringBuffer.append(param);
        logger.warn(stringBuffer.toString());

        //to 测试有无返回值
        Object o=null;
        Exception exception=null;
        try {
            o = pj.proceed();

        }catch (Exception e){
            exception = e;
        }

        stringBuffer.delete((stringBuffer.length()-param.length()-separation.length()-usetime.length()-6-direction_start.length()-ChannelorderSn.length()-orderSn.length()-5),stringBuffer.length());

        if(o!=null){
            if((!StringUtils.isEmpty(annotation.orderSn()))||(!StringUtils.isEmpty(annotation.ChannelorderSn()))){
                if(annotation.ChannelorderSn().startsWith("r")){
                    ChannelorderSn=getFromReturn(annotation.ChannelorderSn(),o);
                }
                if(annotation.orderSn().startsWith("r")){
                    orderSn= getFromReturn(annotation.orderSn(),o);
                }
            }
        }

        if(exception == null){//按照方法名称判断，1.有些方法名执行完毕后即为businessDone，2.有些方法需要判断回参决定是normal还是businessDone
            if(methodName.equals("fileCzSuccessNotify")){
                status="businessDone";
            }
            if(methodName.equals("HashCzNotify")){
                status="businessDone";
            }
            if(methodName.equals("XqCzNotify")){
                status="businessDone";
            }

            if(methodName.equals("fileAttest")){
                if(((JsonResult) o).isSuccess()) status="normal";
                else status="businessDone";
            }
            if(methodName.equals("HashAttest")){
                if(((JsonResult) o).isSuccess()) status="normal";
                else status="businessDone";
            }
            if(methodName.equals("attestContinue")){
                if(((JsonResult) o).isSuccess()) status="normal";
                else status="businessDone";
            }
            if(methodName.equals("selectAttestFile")){
                if(((JsonResult) o).isSuccess()) status="normal";
                else status="businessDone";
            }
            if(methodName.equals("selectAttestFileByDigital")){
                if(((JsonResult) o).isSuccess()) status="normal";
                else status="businessDone";
            }
            if(methodName.equals("downLoadFile")){
                if(((JsonResult) o).isSuccess()) status="normal";
                else status="businessDone";
            }
        }else status="SystemERROR";

        long endTime = System.currentTimeMillis();
        //to 合并


        System.out.print("\n"+stringBuffer.toString()+"\n");
        stringBuffer.append(orderSn);
        stringBuffer.append(",");
        stringBuffer.append(ChannelorderSn);
        stringBuffer.append(",");
        stringBuffer.append(direction_end);
        stringBuffer.append(",");
        stringBuffer.append(status);
        stringBuffer.append(",");
        usetime=""+(endTime-startTime);
        stringBuffer.append(usetime);
        stringBuffer.append(",");
        stringBuffer.append(separation);
        if(exception==null){
            stringBuffer.append(o);
        }else stringBuffer.append(exception);
        logger.warn(stringBuffer.toString());

        if( exception != null){
            throw  exception;
        }
        return o;
    }

    /**
     * 获取业务id
     * 一般参数的个数不会太多，所以此处未与上一个遍历进行合并
     * @param bizIdLocation
     * @param pj
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String getBizId(String bizIdLocation, ProceedingJoinPoint pj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if(StringUtils.isEmpty(bizIdLocation)){
            return "";
        }
        //获取业务id的所在索引
        int begin = bizIdLocation.indexOf(ZhijlLog.START);
        int end = bizIdLocation.indexOf(ZhijlLog.END);
        int idx = Integer.valueOf(bizIdLocation.substring(begin+1,end));

        //是否需要进一步调用方法
        boolean useMethod = false;
        String bizIdMethodName = null;
        int split_loc = bizIdLocation.indexOf(ZhijlLog.SPLIT);
        if( split_loc >-1){
            useMethod = true;
            bizIdMethodName = bizIdLocation.substring(split_loc+1);
        }

        log.debug("idex={}, use={},bizMethod={}",idx,useMethod,bizIdMethodName);

        //开始寻找业务ID
        String bizId = null;
        Object[] arguments = pj.getArgs();   //获得参数列表
        if(arguments.length > 0){
            for(int i=0;i<arguments.length;i++){
                if(i!=idx){
                    continue;
                }else {
                    Object tar = arguments[i];
                    if(useMethod){
                        //需要调用方法
                        Method method = tar.getClass().getDeclaredMethod(bizIdMethodName);
                        Object o=method.invoke(tar);
                        if(o!=null){
                            bizId = o.toString();
                        }else
                            bizId="";
                    }else{
                        bizId = tar.toString();
                    }
                    break;
                }
            }
        }

        return bizId;
    }

    private String getFromReturn(String bizIdLocation, Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(StringUtils.isEmpty(bizIdLocation)){
            return "";
        }

        //是否需要进一步调用方法
        boolean useMethod = false;
        String bizIdMethodName = null;
        int split_loc = bizIdLocation.indexOf(ZhijlLog.SPLIT);
        if( split_loc >-1){
            useMethod = true;
            bizIdMethodName = bizIdLocation.substring(split_loc+1);
        }
        String bizId = null;
        if(useMethod){
            //需要调用方法
            Method method = o.getClass().getDeclaredMethod(bizIdMethodName);
            bizId = method.invoke(o).toString();
        }else{
            bizId = o.toString();
        }
        return bizId;

    }


    /**
     * 获取指定类的logger对象
     * @param className
     * @return
     */
    private Logger getOriLogger(String className) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {

        Logger logger = loggerMap.get(className);

        if( logger==null){
            Class clazz = Class.forName(className);
            Field field = clazz.getDeclaredField("log");//和上面一样，通过Class得到类声明的属性。
            field.setAccessible(true);
            logger = (Logger)field.get(clazz) ;//因为该属性是静态的，所以直接从类的Class里取。

            loggerMap.put(className,logger);
        }

        return logger;
    }

    private Map<String,Logger> loggerMap = new HashMap<>();
}

