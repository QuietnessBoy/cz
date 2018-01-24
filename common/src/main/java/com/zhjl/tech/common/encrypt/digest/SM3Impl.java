package com.zhjl.tech.common.encrypt.digest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 国密摘要算法
 * 注意每次获取的对象实例不是线程安全的，所以每次加密都要使用新的instance。
 */
public final class SM3Impl implements SM3 {

    @Override
    public byte[] getHash(byte [] input){
        this.update(input);
        return this.digest();
    }

    public static void main(String[] args) {
        SM3 sm3 = new SM3Impl();
        try {
            System.out.print(Base64.getEncoder().encodeToString(sm3.getFileHash("/NewFilecz/fname-ordersnfb64b9b0d")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    2DN+zdfbXsxY2ewfmStrTaqXtajQ9A1E5FnMBtSha3Q=
    /**
     *
     * 1 手动更新流
     *   SM3 sm3 = SM3.getInstance();
     *   sm3.update("abc".getBytes());
     *   sm3.update("abc".getBytes());
     *   sm3.digest();
     *  2 独立的文件摘要模式
     *   getFileHash(filepath)
     */

    //对文件实现摘要
    @Override
    public byte[] getFileHash(String path) throws IOException {
        long start = System.currentTimeMillis();
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        SM3Impl sm3 = this;
        try {
            //分配4m内存
            byte[] buffer = new byte[1024 * 1024 * 4];
            int len;

            while ((len = in.read(buffer)) > 0) {
                sm3.update(buffer, 0, len);
            }

            long total = System.currentTimeMillis() - start;
            System.out.println("sm3 file, time ellapse : " + total);
            return sm3.digest();
        } finally {
            in.close();
        }
    }

    private int H0, H1, H2, H3, H4, H5, H6, H7;
    private final int[] w = new int[80];

    //常量Tj的值如下
    private static final Integer Tj15 = Integer.valueOf("79cc4519", 16);
    private static final Integer Tj63 = Integer.valueOf("7a879d8a", 16);
    private int currentPos;
    private long currentLen;

    public SM3Impl()
    {
        reset();
    }

    public final void reset()
    {
        //8个字寄存器的初始值
        H0 = 0x7380166F;
        H1 = 0x4914B2B9;
        H2 = 0x172442D7;
        H3 = 0xDA8A0600;
        H4 = 0xA96F30BC;
        H5 = 0x163138AA;
        H6 = 0xE38DEE4D;
        H7 = 0xB0FB0E4E;
        currentPos = 0;
        currentLen = 0;
    }

    @Override
    public final void update(byte b[])
    {
        update(b, 0, b.length);
    }

    @Override
    public final void update(byte b[], int off, int len)
    {
        //太短的暂不处理
        if (len >= 4)
        {
            int idx = currentPos >> 2;//4个byte一划分

            //准备16字（512bit，每个字32bit，能存放4个byte）的w数组部分。
            // 开始读取第一个4 byte
            switch (currentPos & 3) //其实是  currentPos % 4
            {
                case 0://一次读取4个字节
                    w[idx] = (((b[off++] & 0xff) << 24) | ((b[off++] & 0xff) << 16) | ((b[off++] & 0xff) << 8) | (b[off++] & 0xff));
                    len -= 4;
                    currentPos += 4;
                    currentLen += 32;
                    if (currentPos == 64) //准备好了w数组，可以开始一次压缩运算
                    {
                        perform();
                        currentPos = 0;
                    }
                    break;
                case 1: //上次处理了一个字节
                    w[idx] = (w[idx] << 24) | (((b[off++] & 0xff) << 16) | ((b[off++] & 0xff) << 8) | (b[off++] & 0xff));
                    len -= 3;
                    currentPos += 3;
                    currentLen += 24;
                    if (currentPos == 64)
                    {
                        perform();
                        currentPos = 0;
                    }
                    break;
                case 2:
                    w[idx] = (w[idx] << 16) | (((b[off++] & 0xff) << 8) | (b[off++] & 0xff));
                    len -= 2;
                    currentPos += 2;
                    currentLen += 16;
                    if (currentPos == 64)
                    {
                        perform();
                        currentPos = 0;
                    }
                    break;
                case 3:
                    w[idx] = (w[idx] << 8) | (b[off++] & 0xff);
                    len--;
                    currentPos++;
                    currentLen += 8;
                    if (currentPos == 64)
                    {
                        perform();
                        currentPos = 0;
                    }
                    break;
            }

            /* Now currentPos is a multiple of 4 - this is the place to be...*/
            //快速处理方式
            while (len >= 8)
            {
                w[currentPos >> 2] = ((b[off++] & 0xff) << 24) | ((b[off++] & 0xff) << 16) | ((b[off++] & 0xff) << 8) | (b[off++] & 0xff);
                currentPos += 4;

                if (currentPos == 64)
                {
                    perform();
                    currentPos = 0;
                }

                w[currentPos >> 2] = ((b[off++] & 0xff) << 24) | ((b[off++] & 0xff) << 16) | ((b[off++] & 0xff) << 8) | (b[off++] & 0xff);

                currentPos += 4;

                if (currentPos == 64)
                {
                    perform();
                    currentPos = 0;
                }

                currentLen += 64;
                len -= 8;
            }

            //何时调用？
            while (len < 0) //(len >= 4)
            {
                w[currentPos >> 2] = ((b[off++] & 0xff) << 24) | ((b[off++] & 0xff) << 16) | ((b[off++] & 0xff) << 8) | (b[off++] & 0xff);
                len -= 4;
                currentPos += 4;
                currentLen += 32;
                if (currentPos == 64)
                {
                    perform();
                    currentPos = 0;
                }
            }
        }

        /* Remaining bytes (1-3)  这里不只1-8*/
        while (len > 0)
        {
            /* Here is room for further improvements */
            int idx = currentPos >> 2; //当前的位置
            w[idx] = (w[idx] << 8) | (b[off++] & 0xff);

            currentLen += 8;
            currentPos++;

            if (currentPos == 64)
            {
                perform();
                currentPos = 0;
            }
            len--;
        }
    }

    @Override
    public final void update(byte b)
    {
        int idx = currentPos >> 2;
        w[idx] = (w[idx] << 8) | (b & 0xff);

        currentLen += 8;
        currentPos++;

        if (currentPos == 64)
        {
            perform();
            currentPos = 0;
        }
    }

    private final void putInt(byte[] b, int pos, int val)
    {
        b[pos] = (byte) (val >> 24);
        b[pos + 1] = (byte) (val >> 16);
        b[pos + 2] = (byte) (val >> 8);
        b[pos + 3] = (byte) val;
    }

    @Override
    public final byte[] digest()
    {
        //填充
        int idx = currentPos >> 2;
        // 0x80
        w[idx] = ((w[idx] << 8) | (0x80)) << ((3 - (currentPos & 3)) << 3);

        currentPos = (currentPos & ~3) + 4;

        if (currentPos == 64)
        {
            currentPos = 0;
            perform();
        }
        else if (currentPos == 60)
        {
            currentPos = 0;
            w[15] = 0;
            perform();
        }

        for (int i = currentPos >> 2; i < 14; i++) {
            w[i] = 0;
        }

        w[14] = (int) (currentLen >> 32);
        w[15] = (int) currentLen;

        perform();

        //输出
        byte[] out = new byte[32];
        int off=0;
        putInt(out, off, H0);
        putInt(out, off + 4, H1);
        putInt(out, off + 8, H2);
        putInt(out, off + 12, H3);
        putInt(out, off + 16, H4);
        putInt(out, off + 20, H5);
        putInt(out, off + 24, H6);
        putInt(out, off + 28, H7);

        reset();

        return out;
    }

    //常量T
    private static int T(int j) {
        if (j >= 0 && j <= 15) {
            return Tj15.intValue();
        } else if (j >= 16 && j <= 63) {
            return Tj63.intValue();
        } else {
            throw new RuntimeException("data invalid");
        }
    }

    //原算法中采用的是字（32bit）为单位，这里的函数并未指定长度，更广义而无影响。
    //布尔函数FF
    private static Integer FF(Integer x, Integer y, Integer z, int j) {
        if (j >= 0 && j <= 15) {
            return Integer.valueOf(x.intValue() ^ y.intValue() ^ z.intValue());
        } else if (j >= 16 && j <= 63) {
            return Integer.valueOf((x.intValue() & y.intValue())
                    | (x.intValue() & z.intValue())
                    | (y.intValue() & z.intValue()));
        } else {
            throw new RuntimeException("data invalid");
        }
    }

    //布尔函数GG
    private static Integer GG(Integer x, Integer y, Integer z, int j) {
        if (j >= 0 && j <= 15) {
            return Integer.valueOf(x.intValue() ^ y.intValue() ^ z.intValue());
        } else if (j >= 16 && j <= 63) {
            return Integer.valueOf((x.intValue() & y.intValue())
                    | (~x.intValue() & z.intValue()));
        } else {
            throw new RuntimeException("data invalid");
        }
    }

    //置换函数P0
    private static Integer P0(Integer x) {
        return Integer.valueOf(x.intValue()
                ^ Integer.rotateLeft(x.intValue(), 9)
                ^ Integer.rotateLeft(x.intValue(), 17));
    }

    //置换函数P1
    private static Integer P1(Integer x) {
        return Integer.valueOf(x.intValue()
                ^ Integer.rotateLeft(x.intValue(), 15)
                ^ Integer.rotateLeft(x.intValue(), 23));
    }


    //每一次的摘要。此时w0~w15已经准备好值。
    private final void perform()
    {
        //消息扩展。将分组扩展为132个字。w0~w67存储在w， w'0~w'63存储在w1
        int j;
        int[] w1=new int[64];

        for (j = 16; j < 68; j++)
        {
            w[j] = P1(w[j - 16] ^ w[j - 9] ^ Integer.rotateLeft(w[j - 3], 15))
                    ^ Integer.rotateLeft(w[j - 13], 7) ^ w[j - 6];
        }
        for(j = 0; j < 64; j++)
        {
            w1[j] = w[j] ^ w[j + 4];
        }

        //压缩函数。H是上一次运算的寄存器。
        int A = H0;
        int B = H1;
        int C = H2;
        int D = H3;
        int E = H4;
        int F = H5;
        int G = H6;
        int H = H7;
        int ss1, ss2, tt1, tt2;
        for (j = 0; j < 64; j++) {
            ss1 = Integer
                    .rotateLeft(
                            Integer.rotateLeft(A, 12) + E
                                    + Integer.rotateLeft(T(j), j), 7);
            ss2 = ss1 ^ Integer.rotateLeft(A, 12);
            //todo 此处加法是否需要mod 2^32
            tt1 = FF(A, B, C, j) + D + ss2 + w1[j];
            tt2 = GG(E, F, G, j) + H + ss1 + w[j];
            D = C;
            C = Integer.rotateLeft(B, 9);
            B = A;
            A = tt1;
            H = G;
            G = Integer.rotateLeft(F, 19);
            F = E;
            E = P0(tt2);
        }

        H0 ^= A;
        H1 ^= B;
        H2 ^= C;
        H3 ^= D;
        H4 ^= E;
        H5 ^= F;
        H6 ^= G;
        H7 ^= H;
    }

}

