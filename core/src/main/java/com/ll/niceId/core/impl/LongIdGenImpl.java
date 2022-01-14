package com.ll.niceId.core.impl;

import com.google.common.base.Preconditions;
import com.ll.niceId.core.config.NiceIdGenConfig;
import com.ll.niceId.core.model.SequenceData;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;


/**
 * @description: long类型id生成器
 * @author: Tomliu
 * @create: 2021-12-28 21:30
 **/
@Component
class LongIdGenImpl {

    /**
     * 序号部分长度
     */
    private final static long SEQUENCE_PART_WIDTH = 7L;

    /**
     * 序号的最大值
     */
    private final static long MAX_SEQUENCE = 1 << SEQUENCE_PART_WIDTH;

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
    private static long lastTimeMillis = -1L;

    /**
     * 上一次序号
     */
    private static short lastSeqenceNo = 0;


    /**
     * 机器号
     */
    private static short machineId = 1;

    /**
     * 起始时间毫秒数
     */
    private static long startTimeMillis = NiceIdGenConfig.getDefaultStartTime().getTime();//设置默认起始时间

    /**
     * 随机值生成器
     */
    private final static Random RANDOM = new Random();

    /**
     * 设置当前的机器号
     *
     * @param machineId 机器号（1-1024）
     */
    public static void setMachineId(short machineId) {
        Preconditions.checkArgument(machineId <= MAX_MACHINE_ID && machineId > 0, "机器号不符合规范，必须在1-1024范围内");
        LongIdGenImpl.machineId = machineId;
    }

    /**
     * 设置id的起始时间
     *
     * @param idStartTime
     */
    public static void setIdStartTime(Date idStartTime) {
        LongIdGenImpl.startTimeMillis = idStartTime.getTime();
    }

    /**
     * 获取新的id
     *
     * @return
     */
    public long newId() {
        long currentTimeMillis = getRealCurrentTime();//获取当前时间（规避时钟回拨的情况）
        SequenceData sequenceData = getSequenceNo(currentTimeMillis);//获取随机序号

        long machineIdShift = SEQUENCE_PART_WIDTH;//机器号偏移量
        long timestampShift = SEQUENCE_PART_WIDTH + MACHINE_ID_WIDTH;
        long relativeTimeMillis = sequenceData.getCurrentSequenceTimeMillis() - startTimeMillis; //计算当前时间与起始时间的差值，以减少时间部分的长度

        long longMachineIdPart = machineId << machineIdShift; //获取机器部分
        long longTimePart = relativeTimeMillis << (machineIdShift + timestampShift);//获取时间部分
        long longSequencePart = sequenceData.getSequence();//获取序列号部分

        return longTimePart | longMachineIdPart | longSequencePart;
    }

    /**
     * 获取序号
     *
     * @param currentTimeMillis 当前时间戳
     * @return
     */
    @SneakyThrows
    private synchronized SequenceData getSequenceNo(long currentTimeMillis) {
        long localCurrentTimeMills = currentTimeMillis;

        //序号的初始随机值；tips：这里理论上只要小于128就可以，但如果随机到接近128的数，将导致同一毫秒内可用的序号很少，会进一步增加延迟到下一毫秒的可能性。
        int maxRandomNum = 100;
        short sequence = lastSeqenceNo;

        if (localCurrentTimeMills != lastTimeMillis) {//当前时间与上一次时间不相等时，说明已经进入了新的ms区别
            sequence = (short) RANDOM.nextInt(maxRandomNum);//新ms区别，直接取随机值
        } else {
            if (++sequence >= MAX_SEQUENCE) {//当前仍然在上一次ms区间内，序号使用自增1的值。为防止序号溢出（最大长度RANDOM_PART_WIDTH），需要
                //耗尽的情况下，等待到下一ms获取新的值
                long now = System.currentTimeMillis();
                if (now == localCurrentTimeMills) {
                    Thread.sleep(1);//延时1ms，进入下一ms区间
                }
                localCurrentTimeMills = System.currentTimeMillis();
                sequence = (short) RANDOM.nextInt(maxRandomNum);//新ms区别，直接取随机值
            }
        }

        lastTimeMillis = localCurrentTimeMills;//记录当前时间戳为上一次时间戳
        lastSeqenceNo = sequence;//记录当前的序号为上一次序号
        SequenceData sequenceData = new SequenceData();
        sequenceData.setSequence(sequence);
        sequenceData.setCurrentSequenceTimeMillis(localCurrentTimeMills);
        return sequenceData;
    }

    /**
     * 获取当前真实的时间戳(即排除回拨情况下的时间戳）
     *
     * @return
     */
    private synchronized long getRealCurrentTime() {
        long currentTimeMillis;
        do {
            currentTimeMillis = System.currentTimeMillis();
            long timeDiff = lastTimeMillis - currentTimeMillis;
            if (timeDiff > MAX_TIME_DIFF_MIL) {
                throw new RuntimeException("时间回拨过大，请稍后再试");
            } else if (timeDiff > 0) {
                try {
                    Thread.sleep(timeDiff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (currentTimeMillis <= lastTimeMillis);//防止时间回拨，等待时间追上上一次时间
        return currentTimeMillis;
    }
}
