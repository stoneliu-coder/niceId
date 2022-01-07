package com.ll.niceId.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.ll.niceId.core.config.BizConfig.*;


/**
 * @description: long类型id生成器
 * @author: Tomliu
 * @create: 2021-12-28 21:30
 **/
@Component
public class LongIdGen {

    private Logger logger = LoggerFactory.getLogger(LongIdGen.class);

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
     * 获取新的id
     * @param machineId 机器号（须小于1024）
     * @return
     */
    public long newId(int machineId) {
        if (machineId >= MAX_MACHINE_ID) {
            throw new RuntimeException("机器号超出最大限制1024");
        }

        //获取当前相对时间戳
        long timestamp = getRelativeTimeStamp();

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
        if (currentSequence >= 128) {
            throw new RuntimeException("序号已用尽");
        }
        lastSequenceTimeStamp = currentTimeStamp;
        logger.info(String.format("sequence=%d", currentSequence));
        return currentSequence;
    }

    /**
     * 获取相对时间戳
     * 采用相对时间戳可以减少时间戳的总长度
     * @return 获取最新的相对时间戳
     */
    private synchronized long getRelativeTimeStamp() {
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
        return now - START_TIMESTAMP;
    }
}
