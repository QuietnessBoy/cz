package com.zhjl.tech.common.chained.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zhjl.tech.common.chained.protobuf.chainobj.v1.ChainedCz;
import com.zhjl.tech.common.chained.protobuf.hash.v1.AttestType;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.AttestChainedDTO;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class CcOpt {

    /**
     * 根据业务attest对象生成上链的数据
     * @param attest
     * @param expired 1 过期  0 未过期
     * @return
     */
    public static String genChainStr(AttestChainedDTO attest, String expired){

        //生成待hash的对象
        AttestType.AttestType_1 attestType_1 = xferToAttestType_1(attest);

        // aaa the sm3 hash
        byte[] bs = attestType_1.toByteArray();
        SM3 sm3 = new SM3Impl();
        sm3.update(bs);
        byte[] rs = sm3.digest();
        log.info("生成的对象的hash:\n" + Hex.encode(rs));

        ////准备上链的对象,内对象
        ChainedCz.CzDetail_1.Builder czDetail_1_Builder = ChainedCz.CzDetail_1.newBuilder();
        czDetail_1_Builder.setHash(ByteString.copyFrom(rs));

        // 准备链上对象，外对象
        ChainedCz.Cz.Builder czBuilder = ChainedCz.Cz.newBuilder();
        czBuilder.setVersion("1");//当前的固定值
        czBuilder.setOrdersn(attest.getOrdersn());
        czBuilder.setExpired(expired);/*是否过期。 0否，1是 */
        czBuilder.setDetail( czDetail_1_Builder.build().toByteString());

        //最终的字节
        byte[] rbs = czBuilder.build().toByteArray();
        return  Base64.getEncoder().encodeToString(rbs);
    }

    /**
     * 校验对象是否符合链上的情况
     * @param chainStr
     * @param attest
     * @return
     */
    public static boolean verify(String chainStr, AttestChainedDTO attest) throws InvalidProtocolBufferException {

        if(StringUtils.isBlank(chainStr) || attest == null ){
            return false;
        }

        // 解析对象
        byte[] bs = Base64.getDecoder().decode(chainStr);

        ChainedCz.Cz cz = ChainedCz.Cz.parseFrom(bs);
        ByteString byteString = cz.getDetail();
        ChainedCz.CzDetail_1 czDetail_1 = ChainedCz.CzDetail_1.parseFrom(byteString);

        //订单号是否一致
        if( ! cz.getOrdersn().equals(attest.getOrdersn())){
            return false;
        }

        //获取cz中的hash
        ByteString czHash = czDetail_1.getHash();
        String bb = Base64.getEncoder().encodeToString(czHash.toByteArray());
        log.info("区块链上的hash:\n" + bb);

        //生成attest对应的hash
        AttestType.AttestType_1 attestType_1 = xferToAttestType_1(attest);

        // aaa the sm3 hash
        bs = attestType_1.toByteArray();
        SM3 sm3 = new SM3Impl();
        sm3.update(bs);
        String genHash = Base64.getEncoder().encodeToString(sm3.digest());
        log.info("生成的对象hash:\n" + genHash);

        if(! bb.equals(genHash)){
            return false;
        }

        return true;
    }

    /**
     * 根据attest基础存证对象，生成protobuf格式的对象。
     * @param attest
     * @return
     */
    private static AttestType.AttestType_1 xferToAttestType_1(AttestChainedDTO attest){
        log.info("start to encoed attest.");

        if( attest == null ){
            throw new NullPointerException("CcOpt.encodeToProtobuffBytes接收的attest对象不能为空！");
        }

        AttestType.AttestType_1.Builder builder = AttestType.AttestType_1.newBuilder();

        builder.setVersion(attest.getVersion());
        builder.setAncestorsOrdersn(attest.getAncestorsOrdersn());
        if(StringUtils.isBlank(attest.getParentOrdersn())){
            attest.setParentOrdersn("");
        }
        builder.setParentOrdersn(attest.getParentOrdersn());
        builder.setOrdersn(attest.getOrdersn());
        builder.setChannelId(attest.getChannelId());
        builder.setChannelUserid(attest.getChannelUserid());
        builder.setChannelOrdersn(attest.getChannelOrdersn());
        builder.setBizSign(attest.getBizSign());
        builder.setProvinderId(attest.getProvinderId());
        builder.setChained(attest.getChained());
        builder.setWalletAddr(attest.getWalletAddr());
        builder.setPublicKey(attest.getPublicKey());
        builder.setAttestSign(attest.getAttestSign());
        builder.setAttestType(attest.getAttestType());
        builder.setBizType(attest.getBizType());
        builder.setFileName(attest.getFileName());
        builder.setFileType(attest.getFileType());
        builder.setFileSize(attest.getFileSize());
        if(StringUtils.isBlank(attest.getFileSign())){
            attest.setFileSign("");
        }
        builder.setFileSign(attest.getFileSign());
        builder.setFileHash(attest.getFileHash());
        builder.setOwnerType(attest.getOwnerType());
        builder.setOwnerId(attest.getOwnerId());
        builder.setOwnerName(attest.getOwnerName());

        //日期格式转换 yyyyMMdd-HHmmss
        SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format, Locale.PRC);

        String ds = sdf.format(attest.getOriginTime());
        builder.setOriginTime(ds);

        ds = sdf.format(attest.getStartTime());
        builder.setStartTime(ds);

        builder.setDuration(attest.getDuration());
        builder.setDescription(attest.getDescription());
        builder.setPrice(attest.getPrice());
        builder.setPlatformSign(attest.getPlatformSign());

        log.info("待hash对象:\n" + Base64.getEncoder().encodeToString(builder.build().toByteArray()));

        return builder.build();
    }


    public static void main(String[] args) throws InvalidProtocolBufferException {

        ///mock a attest obj

        AttestChainedDTO attest = new AttestChainedDTO();
        attest.setOrdersn("ordersn_0012345678");
        attest.setWalletAddr("V9Px94293u3S8ginFho7a6X7nadYocq9ptYWVPrPbmUT6Gezt");
        attest.setVersion("0.1");
        attest.setPlatformSign("xalrpohGBteKef89Z4qmjO2hdculAWoVdzCuhZrIhqrwi/LpKXR7V22JofGBSlIe611603OsbVm6021usa2iZg==");
        attest.setBizSign("0no1hdHq7vhMQi8t7ZpKRGsADB7AvpbHOoe0S5XesX7E/eT/Fjy6BiB+NQTLxOV5NDbTfic8LpeddsPeSADTjg==");
        attest.setChannelId("channelId_1234567890");
        attest.setChannelUserid("channelId_1234567890");
        attest.setChannelOrdersn("channelId_1234567890");
        attest.setBizType("bizType");
        attest.setFileName("fileName_012345678");
        attest.setFileType("doc");
        attest.setFileSize("10201024");
        attest.setFileSign("7jaNZvSuDxqJ2P/0scUesS8f5kd4Gv9FgtFh7cGbogP08k7p8HQCkxMKyeO1k0xeyBVoZo8IqF71WFvNkmLHqA==");
        attest.setFileHash("Xk0LNidH+LEP8Q/f3mGDqi9ww63JnxtVHOEeojrrH+k=");
        attest.setOwnerType("1");
        attest.setOwnerId("110101200001011122");
        attest.setOwnerName("张复兴");
        attest.setDuration("365");
        attest.setAncestorsOrdersn("original_0012345678");
        attest.setParentOrdersn("original_0012345678");
        attest.setProvinderId("provinderId");
        attest.setPublicKey("00b80ef23ae010126ae2ca46e46a954392358399966916ba308b2f8e916000955c");
        attest.setAttestSign("7jaNZvSuDxqJ2P/0scUesS8f5kd4Gv9FgtFh7cGbogP08k7p8HQCkxMKyeO1k0xeyBVoZo8IqF71WFvNkmLHqA==");
        attest.setAttestType("attestType");
        attest.setChained("1");

        SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format, Locale.PRC);
        Date d1 = null;
        try {
            d1 = sdf.parse("20171101-010101");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        attest.setOriginTime(d1);
        attest.setStartTime(d1);
        attest.setDescription("description。这是在遥远的凄凉之地拍摄的一个低温空气分子动态截图。");
        attest.setPrice("price");

        String str = CcOpt.genChainStr(attest,"1");
        System.out.println( " aaa chainedCallBack str: \n" + str);


        System.out.println( " verify chainedCallBack str: \n" + CcOpt.verify(str,attest));

        /**
         - 待hash对象:
         CgMwLjESE29yaWdpbmFsXzAwMTIzNDU2NzgaE29yaWdpbmFsXzAwMTIzNDU2NzgiEm9yZGVyc25fMDAxMjM0NTY3OCoUY2hhbm5lbElkXzEyMzQ1Njc4OTAyFGNoYW5uZWxJZF8xMjM0NTY3ODkwOhRjaGFubmVsSWRfMTIzNDU2Nzg5MEJYMG5vMWhkSHE3dmhNUWk4dDdacEtSR3NBREI3QXZwYkhPb2UwUzVYZXNYN0UvZVQvRmp5NkJpQitOUVRMeE9WNU5EYlRmaWM4THBlZGRzUGVTQURUamc9PUoLcHJvdmluZGVySWRSATFaMVY5UHg5NDI5M3UzUzhnaW5GaG83YTZYN25hZFlvY3E5cHRZV1ZQclBibVVUNkdlenRiQjAwYjgwZWYyM2FlMDEwMTI2YWUyY2E0NmU0NmE5NTQzOTIzNTgzOTk5NjY5MTZiYTMwOGIyZjhlOTE2MDAwOTU1Y2pYN2phTlp2U3VEeHFKMlAvMHNjVWVzUzhmNWtkNEd2OUZndEZoN2NHYm9nUDA4azdwOEhRQ2t4TUt5ZU8xazB4ZXlCVm9abzhJcUY3MVdGdk5rbUxIcUE9PXIKYXR0ZXN0VHlwZXoHYml6VHlwZYIBEmZpbGVOYW1lXzAxMjM0NTY3OIoBA2RvY5IBCDEwMjAxMDI0mgFYN2phTlp2U3VEeHFKMlAvMHNjVWVzUzhmNWtkNEd2OUZndEZoN2NHYm9nUDA4azdwOEhRQ2t4TUt5ZU8xazB4ZXlCVm9abzhJcUY3MVdGdk5rbUxIcUE9PaIBLFhrMExOaWRIK0xFUDhRL2YzbUdEcWk5d3c2M0pueHRWSE9FZW9qcnJIK2s9qgEBMbIBEjExMDEwMTIwMDAwMTAxMTEyMroBCeW8oOWkjeWFtMIBDzIwMTcxMTAxLTAxMDEwMcoBDzIwMTcxMTAxLTAxMDEwMdIBAzM2NdoBXGRlc2NyaXB0aW9u44CC6L+Z5piv5Zyo6YGl6L+c55qE5YeE5YeJ5LmL5Zyw5ouN5pGE55qE5LiA5Liq5L2O5rip56m65rCU5YiG5a2Q5Yqo5oCB5oiq5Zu+44CC4gEFcHJpY2XqAVh4YWxycG9oR0J0ZUtlZjg5WjRxbWpPMmhkY3VsQVdvVmR6Q3VoWnJJaHFyd2kvTHBLWFI3VjIySm9mR0JTbEllNjExNjAzT3NiVm02MDIxdXNhMmlaZz09

          - 生成的对象的hash:
         c916e096750b0b3519a19c9e2df216018eb547f2efa37adcc81e91606091a43a

         最终的上链str:
         CgExEhJvcmRlcnNuXzAwMTIzNDU2NzgaATEiIgogyRbglnULCzUZoZyeLfIWAY61R/Lvo3rcyB6RYGCRpDo=

         *
         *
         */

    }
}
