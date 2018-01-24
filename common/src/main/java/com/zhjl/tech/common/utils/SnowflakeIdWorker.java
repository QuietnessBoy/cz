package com.zhjl.tech.common.utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 * 前提：本地服务器校时必须准确，不得时间回退。
 *
 * Twitter_Snowflake变体版
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 *
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，存储时间的差值（当前时间 - 基准时间)。基准时间为id生成器开始使用的时间。
 *      可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * 16位的数据分片。其中12位标识联盟节点序号，4位标识联盟内部节点。支持4096个联盟单位，每个联盟支持16台逻辑机器，总共65536数据分片。
 * 6位序列，毫秒内的计数，每个节点每毫秒(同一机器，同一时间截)产生64个ID序号.相当于每节点每秒吞吐量64000个
 *
 *
 * 原始版算法说明
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
public class SnowflakeIdWorker {

    /** 测试 */
    public static void main(String[] args) throws ParseException {

        System.out.println((1L << 16) );
        // 对 calendar 设置为 date 所定的日期

        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(10, 7);
        for (int i = 0; i < 100; i++) {
            long id = idWorker.nextId();
            System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
    }

    // ==============================Fields===========================================
    /** 开始时间截 (2017-08-01) 。该处可以自定义系统的起始时间
        Calendar now = Calendar.getInstance();
        now.set(2017,7,1,0,0,0);
     */
    private final long twepoch = 1501516800000L;

    /** 联盟内部机器数量 */
    private final long alianceMachineIdBits = 4L;

    /** 联盟标号 */
    private final long alianceIdBits = 12L;

    /** 支持的最大机器id， (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxMachineId = -1L ^ (-1L << alianceMachineIdBits);

    /** 支持的最大数据标识id */
    private final long maxAlianceId = -1L ^ (-1L << alianceIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 6L;

    /** 联盟内部机器ID向左移6位 */
    private final long machineIdShift = sequenceBits;

    /** 联盟id向左移10位(6+4) */
    private final long alianceIdShift = sequenceBits + alianceMachineIdBits;

    /** 时间截向左移22位(6+4+12) */
    private final long timestampLeftShift = sequenceBits + alianceMachineIdBits + alianceIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID(0~31) */
    private long alianceMachineId;

    /** 数据中心ID(0~31) */
    private long alianceId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param alianceId 联盟节点标识 (0~1023)
     * @param alianceMachineId 联盟内部机器ID (0~15)
     */
    public SnowflakeIdWorker( long alianceId,long alianceMachineId) {
        if (alianceMachineId > maxMachineId || alianceMachineId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxMachineId));
        }
        if (alianceId > maxAlianceId || alianceId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxAlianceId));
        }
        this.alianceMachineId = alianceMachineId;
        this.alianceId = alianceId;
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        System.out.println("@@" + ((timestamp - twepoch) +","+ timestampLeftShift)
                + ";  " + alianceId + "," + alianceIdShift
                + ";  " + alianceMachineId + "," + machineIdShift
                + ";  " +  sequence);

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (alianceId << alianceIdShift) //
                | (alianceMachineId << machineIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================
    public static void p(String s, Calendar calendar){
        System.out.println( s + "\t" + calendar.get(Calendar.YEAR) + ","
                + calendar.get(Calendar.MONDAY) + ","
                + calendar.get(Calendar.DAY_OF_MONTH) + ","
                + calendar.get(Calendar.HOUR) + ","
                + calendar.get(Calendar.MINUTE) + ","
                + calendar.get(Calendar.SECOND) + ","
                + calendar.get(Calendar.MILLISECOND)
                + ", \t" + calendar.getTimeInMillis()
        );
    }
}
