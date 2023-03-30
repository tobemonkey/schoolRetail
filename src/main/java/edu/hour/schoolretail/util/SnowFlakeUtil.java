package edu.hour.schoolretail.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author 201926002057 戴毅
 * @Description 雪花生成工具
 * @Date 2022/12/16
 **/
@Slf4j
public class SnowFlakeUtil {

    /**
     * 开始时间截：2022-12-16 10:33:07
     */
    private final static long TW_EPOCH = 1671158025574L;

    /**
     * 机器 ID 所占的位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据标识 ID 所占的位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 支持的最大机器ID，最大为31
     * PS. Twitter的源码是 -1L ^ (-1L << workerIdBits)；这里最后和-1进行异或运算，由于-1的二进制补码的特殊性，就相当于进行取反。
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 支持的最大机房ID，最大为31
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 序列在 ID 中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器 ID 向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 机房 ID 向左移17位
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间截向左移22位
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 生成序列的掩码最大值，最大为4095
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 工作机器 ID(0~31)
     */
    @Value("${snowflake.workerId}")
    private static long workerId;

    /**
     * 机房 ID(0~31)
     */
    private static long dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private static long sequence = 0L;

    /**
     * 上次生成 ID 的时间戳
     */
    private static long lastTimestamp = -1L;

    /**
     * 获得下一个 ID(该方法是线程安全的)
     *
     * @return 返回一个长度位15的 long类型的数字
     */
    public static synchronized long nextId() {
        long timestamp = timeGen();
        // 如果当前时间小于上一次 ID 生成的时间戳，说明发生时钟回拨，为保证ID不重复抛出异常。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            // 同一时间生成的，则序号+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 毫秒内序列溢出：超过最大值
            if (sequence == 0) {
                // 阻塞到下一个毫秒，获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        // 上次生成 ID 的时间戳
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起
        return ((timestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 如果发生时钟不一致问题，进行时钟回拨，时钟回拨策略为——等待直到相等
     * @param lastTimestamp
     * @return
     */
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        // 策略一：等待直到相等
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;

        // 策略二：抛出异常 throw new RuntimeException();
        // 策略三：回拨时间较短，比如小于 100ms，可以等待
        // 回拨时间中等：回拨时间100~1000ms，记录当前毫秒最后一次生成的id，如果发生事件回拨，就继续记录的id进行继续生成，同时开始回拨
        // 回拨时间5s内，换一台服务器，等待下次换回该服务器时，如果时间回拨成功，就继续使用
        // 回拨时间过长，下线服务器，等回拨成功后，在上线。
    }

    /**
     * 获取当前时间
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

}

