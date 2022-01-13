package com.ll.niceId.core.impl;

import com.google.common.base.Preconditions;
import com.ll.niceId.core.config.NiceIdGenConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @description: long类型id生成器
 * @author: Tomliu
 * @create: 2021-12-28 21:30
 **/
@Component
class LongIdGenImpl {

    private Logger logger = LoggerFactory.getLogger(LongIdGenImpl.class);

    /**
     * 随机值部分长度
     */
    private final static long RANDOM_PART_WIDTH = 7L;

    /**
     * 机器号部分长度（允许最大1024台机器）
     */
    private final static long MACHINE_ID_WIDTH = 10L;

    /**
     * 最大机器号
     */
    private final static int MAX_MACHINE_ID = 1024;

    /**
     * 最大回拨时间差
     */
    private final static long MAX_TIME_DIFF_MIL = 5 * 1000L;

    /**
     * 记录上一次的时间戳
     */
    private static long lastTimeStamp = -1L;

    /**
     * 记录上一次序号时间戳
     */
    private static long lastSequenceTimeStamp = -1L;

    /**
     * 序号
     */
    private static short sequence = 0;

    /**
     * 机器号
     */
    private static short machineId = 1;

    /**
     * 起始时间毫秒数
     */
    private static long startTimeTicks = NiceIdGenConfig.getDefaultStartTime().getTime();//设置默认起始时间

    /**
     * 设置当前的机器号
     * @param machineId 机器号（1-1024）
     */
    public static void setMachineId(short machineId) {
        Preconditions.checkArgument(machineId <= MAX_MACHINE_ID && machineId >0,"机器号不符合规范，必须在1-1024范围内");
        LongIdGenImpl.machineId = machineId;
    }

    /**
     * 设置id的起始时间
     * @param idStartTime
     */
    public static void setIdStartTime(Date idStartTime) {
        LongIdGenImpl.startTimeTicks = idStartTime.getTime();
    }

    /**
     * 获取新的id
     * @return
     */
    public long newId() {
        //获取当前相对时间戳
        long timestamp = getRelativeTimeStamp(startTimeTicks);

        //获取随机号
        long randomNo = getSequenceNo(timestamp);

        long machineIdShift = RANDOM_PART_WIDTH;//机器号偏移量
        long timestampShift = RANDOM_PART_WIDTH + MACHINE_ID_WIDTH;

        long longMachineId = machineId << machineIdShift;
        long longTimeStamp = timestamp << (machineIdShift + timestampShift);
        return longTimeStamp | longMachineId | randomNo;
    }

    /**
     * 获取序号
     *
     * @param currentTimeStamp 当前时间戳
     * @return
     */
    private synchronized short getSequenceNo(long currentTimeStamp) {
        short currentSequence;
        if (currentTimeStamp != lastSequenceTimeStamp) {
            //相较上次已过1ms，重置序号
            sequence = 0;
        }
        currentSequence = ++sequence;
        lastSequenceTimeStamp = currentTimeStamp;
        if (currentSequence >= 128) {
            throw new RuntimeException("序号已用尽");
        }
        logger.info(String.format("sequence=%d", currentSequence));
        return currentSequence;
    }

    /**
     * 获取相对时间戳
     * 采用相对时间戳可以减少时间戳的总长度
     * @return 获取最新的相对时间戳
     */
    private synchronized long getRelativeTimeStamp(long startTimeStamps) {
        long now;
        do {
            now = System.currentTimeMillis();
            long timeDiff = lastTimeStamp - now;
            if (timeDiff > MAX_TIME_DIFF_MIL) {
                throw new RuntimeException("时间回拨过大，请稍后再试");
            } else if (timeDiff > 0) {
                try {
                    Thread.sleep(timeDiff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (now <= lastTimeStamp);//防止时间回拨，等待时间追上上一次时间
        lastTimeStamp = now;
        return now - startTimeStamps;
    }
}
