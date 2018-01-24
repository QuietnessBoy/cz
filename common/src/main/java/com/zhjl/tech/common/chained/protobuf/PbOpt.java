package com.zhjl.tech.common.chained.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Base64;

public class PbOpt {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        System.out.println("start...");

        Types.CzDetail_1.Builder czdBuilder = Types.CzDetail_1.newBuilder();

        czdBuilder.setOrdersn("ordersn_0012345678");
        czdBuilder.setWalletAddr("V9Px94293u3S8ginFho7a6X7nadYocq9ptYWVPrPbmUT6Gezt");
        czdBuilder.setVersion("0.1");
        czdBuilder.setPlatformSign("xalrpohGBteKef89Z4qmjO2hdculAWoVdzCuhZrIhqrwi/LpKXR7V22JofGBSlIe611603OsbVm6021usa2iZg==");
        czdBuilder.setBizSign("0no1hdHq7vhMQi8t7ZpKRGsADB7AvpbHOoe0S5XesX7E/eT/Fjy6BiB+NQTLxOV5NDbTfic8LpeddsPeSADTjg==");
        czdBuilder.setChannelId("channelId_1234567890");
        czdBuilder.setBizType("bizType");
        czdBuilder.setFileName("fileName_012345678");
        czdBuilder.setFileType("doc");
        czdBuilder.setFileSize("10201024");
        czdBuilder.setFileSign("7jaNZvSuDxqJ2P/0scUesS8f5kd4Gv9FgtFh7cGbogP08k7p8HQCkxMKyeO1k0xeyBVoZo8IqF71WFvNkmLHqA==");
        czdBuilder.setFileHash("Xk0LNidH+LEP8Q/f3mGDqi9ww63JnxtVHOEeojrrH+k=");
        czdBuilder.setOwnerType("1");
        czdBuilder.setOwnerId("110101200001011122");
        czdBuilder.setOwnerName("张复兴");
        czdBuilder.setDuration("365");
        czdBuilder.setOriginalOrdersn("original_0012345678");
        czdBuilder.setProvinderId("provinderId");
        czdBuilder.setPublicKey("00b80ef23ae010126ae2ca46e46a954392358399966916ba308b2f8e916000955c");
        czdBuilder.setAttestSign("7jaNZvSuDxqJ2P/0scUesS8f5kd4Gv9FgtFh7cGbogP08k7p8HQCkxMKyeO1k0xeyBVoZo8IqF71WFvNkmLHqA==");
        czdBuilder.setAttestType("attestType");
        czdBuilder.setOriginTime("originTime");
        czdBuilder.setStartTime("startTime");
        czdBuilder.setDescription("description。这是在遥远的凄凉之地拍摄的一个低温空气分子动态截图。");
        czdBuilder.setPrice("price");

        Types.CzDetail_1 czDetail_1 = czdBuilder.build();
        byte[] czdBytes = czDetail_1.toByteArray();
        String czdBase64 = Base64.getEncoder().encodeToString(czdBytes);
        System.out.println("czd=\n" + czdBase64);
//------------------------------------------------------------------

        Types.Cz.Builder czBuilder = Types.Cz.newBuilder();
        czBuilder.setDocType("cz");
        czBuilder.setOperationTime("20171117-121219");
        czBuilder.setOrdersn(czDetail_1.getOrdersn());
        czBuilder.setWalletAddr(czDetail_1.getWalletAddr());
        czBuilder.setPublicKey(czDetail_1.getPublicKey());
        czBuilder.setChainTime("20171117-121219");
        czBuilder.setExpired("0");
        czBuilder.setCzDetail(com.google.protobuf.ByteString.copyFrom(czdBytes));

        Types.Cz cz = czBuilder.build();
        byte[] czBytes = cz.toByteArray();
        String czBase64 = Base64.getEncoder().encodeToString(czBytes);

        System.out.println("cz=\n" + czBase64);


        //解码cz
        String cds = "CgJjehIPMjAxNzExMTctMTIxMjE5GhJvcmRlcnNuXzAwMTIzNDU2NzgiMVY5UHg5NDI5M3UzUzhnaW5GaG83YTZYN25hZFlvY3E5cHRZV1ZQclBibVVUNkdlenQqQjAwYjgwZWYyM2FlMDEwMTI2YWUyY2E0NmU0NmE5NTQzOTIzNTgzOTk5NjY5MTZiYTMwOGIyZjhlOTE2MDAwOTU1YzIPMjAxNzExMTctMTIxMjE5OgEwQtEGCgMwLjESWHhhbHJwb2hHQnRlS2VmODlaNHFtak8yaGRjdWxBV29WZHpDdWhacklocXJ3aS9McEtYUjdWMjJKb2ZHQlNsSWU2MTE2MDNPc2JWbTYwMjF1c2EyaVpnPT0aWDBubzFoZEhxN3ZoTVFpOHQ3WnBLUkdzQURCN0F2cGJIT29lMFM1WGVzWDdFL2VUL0ZqeTZCaUIrTlFUTHhPVjVORGJUZmljOExwZWRkc1BlU0FEVGpnPT0iFGNoYW5uZWxJZF8xMjM0NTY3ODkwKgdiaXpUeXBlMhJmaWxlTmFtZV8wMTIzNDU2Nzg6A2RvY0IIMTAyMDEwMjRKWDdqYU5adlN1RHhxSjJQLzBzY1Vlc1M4ZjVrZDRHdjlGZ3RGaDdjR2JvZ1AwOGs3cDhIUUNreE1LeWVPMWsweGV5QlZvWm84SXFGNzFXRnZOa21MSHFBPT1SLFhrMExOaWRIK0xFUDhRL2YzbUdEcWk5d3c2M0pueHRWSE9FZW9qcnJIK2s9WgExYhIxMTAxMDEyMDAwMDEwMTExMjJqF+W8oOWkjeWFtCx6Ynpi5byg5aSN5YW0cgMzNjV6Em9yZGVyc25fMDAxMjM0NTY3OIIBE29yaWdpbmFsXzAwMTIzNDU2NziKAQtwcm92aW5kZXJJZJIBMVY5UHg5NDI5M3UzUzhnaW5GaG83YTZYN25hZFlvY3E5cHRZV1ZQclBibVVUNkdlenSaAUIwMGI4MGVmMjNhZTAxMDEyNmFlMmNhNDZlNDZhOTU0MzkyMzU4Mzk5OTY2OTE2YmEzMDhiMmY4ZTkxNjAwMDk1NWOiAVg3amFOWnZTdUR4cUoyUC8wc2NVZXNTOGY1a2Q0R3Y5Rmd0Rmg3Y0dib2dQMDhrN3A4SFFDa3hNS3llTzFrMHhleUJWb1pvOElxRjcxV0Z2TmttTEhxQT09qgEKYXR0ZXN0VHlwZbIBCm9yaWdpblRpbWW6AQlzdGFydFRpbWXCAVxkZXNjcmlwdGlvbuOAgui/meaYr+WcqOmBpei/nOeahOWHhOWHieS5i+WcsOaLjeaRhOeahOS4gOS4quS9jua4qeepuuawlOWIhuWtkOWKqOaAgeaIquWbvuOAgsoBBXByaWNl";
        Types.Cz cz1 = Types.Cz.parseFrom(Base64.getDecoder().decode(cds));
        System.out.println(cz1);

        //继续解码czDetail
        ByteString czbb = cz1.getCzDetail();
        Types.CzDetail_1 czDetail1 = Types.CzDetail_1.parseFrom(czbb.toByteArray());
        System.out.println(czDetail1);
        System.out.println("!!!==" + czDetail1.getOwnerName());

    }
}
