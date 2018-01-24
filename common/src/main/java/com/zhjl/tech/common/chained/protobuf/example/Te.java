package com.zhjl.tech.common.chained.protobuf.example;

import com.google.protobuf.ByteString;

import java.util.Base64;

public class Te {

    public static void main(String[] args) {
        ExampleOuterClass.Example.Builder builder = ExampleOuterClass.Example.newBuilder();
        builder.setExpired("1");
        builder.setVersion("111");
        builder.setHash(ByteString.copyFrom(Base64.getDecoder().decode("yRbglnULCzUZoZyeLfIWAY61R/Lvo3rcyB6RYGCRpDo=")));
        builder.setOrdersn("123456789012345678");

        System.out.println(Base64.getEncoder().encodeToString(builder.build().toByteArray()));

    }
}
