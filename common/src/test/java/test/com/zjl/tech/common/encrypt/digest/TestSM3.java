package test.com.zjl.tech.common.encrypt.digest;


import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestSM3 {


    SM3 sm3Impl = new SM3Impl();

    @Test
    public void testSm3() throws IOException {
        {

            System.out.println("sm3摘要0："+Hex.encode(new SM3Impl().digest()));
            System.out.println("sm3摘要："+Hex.encode(sm3Impl.getHash("test sm3 hash".getBytes())));
            System.out.println("sm3摘要："+Hex.encode(sm3Impl.getHash("aaaa".getBytes())));

            //66C7F0F462EEEDD9D1F2D46BDC10E4E24167C4875CF2F7A2297DA02B8F4BA8E0
            byte[] byte1 = "abc".getBytes();
            sm3Impl.update("abc".getBytes());
            assertEquals(
                    "66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0",
                    Hex.encode(sm3Impl.digest()).toLowerCase()
            );

            byte[] input = new byte[]{
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64,
                    0x61, 0x62, 0x63, 0x64, 0x61, 0x62, 0x63, 0x64};
            sm3Impl.update(input);
            assertEquals(
                    "DEBE9FF92275B8A138604889C18E5A4D6FDB70E5387E5765293DCBA39C0C5732",
                    Hex.encode(sm3Impl.digest()).toUpperCase()
            );

            for (int i = 0; i < 1000000; i++){
                sm3Impl.update((byte) 'a');
            }
            assertEquals(
                    "C8AAF89429554029E231941A2ACC0AD61FF2A5ACD8FADD25847A3A732B3B02C3",
                    Hex.encode(sm3Impl.digest()).toUpperCase()
            );

            /**
             * 文件大小  时间
             * 1.5g       67.3s
             * 512M       26.0s
             * 215M        9.6s
             * 19M          810ms
             * 5.4M         230ms
             */

            long start = System.currentTimeMillis();

            for (int i=0;i<20;i++){
                String s = Hex.encode(sm3Impl.getFileHash("D:\\情报学基础教程.pdf"));

            }
            System.out.println("time=" +( System.currentTimeMillis()-start));

        }
    }
}
