package com.ll.niceId.core.impl;

import com.google.common.base.Preconditions;
import com.ll.niceId.core.config.NiceIdGenConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.ll.niceId.core.config.Constants.DEFAULT_AVAILABLE_CHARS;

/**
 * @description: com.ll.niceId.NiceId
 * @author: Tomliu
 * @create: 2021-12-28 21:20
 **/
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NiceIdGen {

    private final Logger logger = LoggerFactory.getLogger(NiceIdGen.class);

    /**
     * 生成器编号
     * <p>
     * 本编码保证唯一，唯一标识某一个实例，用于在存储中区分不同的实例
     * </p>
     */
    private final static String GENERATOR_ID = UUID.randomUUID().toString();

    /**
     * long 类型id生成器
     */
    private final LongIdGenImpl longIdGenerator;


    /**
     * 获取一个新的id
     *
     * @return
     */
    public long newLongId() {
        return longIdGenerator.newId();
    }

    /**
     * 获取一个新的String类型id
     *
     * @return
     */
    public String newStringId() {
        //获取10进制的id
        long id = longIdGenerator.newId();

        //将10进制id进行进制转换
        return tenToRadix(id);
    }

    /**
     * 配置id生成参数
     *
     * @param niceIdGenConfig
     */
    public void config(NiceIdGenConfig niceIdGenConfig) {
        Preconditions.checkNotNull(niceIdGenConfig);
        LongIdGenImpl.setIdStartTime(niceIdGenConfig.getStartTime());
        LongIdGenImpl.setMachineId(niceIdGenConfig.getMachineId());
        logger.info("设置配置：{}", niceIdGenConfig);
    }


    /**
     * 10进制转当前进制
     *
     * @return 目标进制结果
     */
    private String tenToRadix(long number) {
        StringBuilder sb = new StringBuilder();
        char[] availableChars = DEFAULT_AVAILABLE_CHARS;
        int toRadix = availableChars.length;
        long currentNumber = number;
        while (currentNumber != 0) {
            sb.append(availableChars[(int) (currentNumber % toRadix)]);
            currentNumber = currentNumber / toRadix;
        }
        return sb.reverse().toString();
    }
}
