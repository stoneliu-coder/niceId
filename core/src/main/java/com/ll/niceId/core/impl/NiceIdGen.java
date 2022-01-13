package com.ll.niceId.core.impl;

import com.ll.niceId.core.config.NiceIdGenConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
     *     本编码保证唯一，唯一标识某一个实例，用于在存储中区分不同的实例
     * </p>
     */
    private final static String GENERATOR_ID = UUID.randomUUID().toString();

    /**
     * long 类型id生成器
     */
    private final LongIdGen longIdGenerator;

    /**
     * string 类型id生成器
     */
    private final StringIdGen stringIdGenerator;

    /**
     * id生成配置项
     */
    private NiceIdGenConfig niceIdGenConfig = new NiceIdGenConfig();

    /**
     * 获取一个新的id
     *
     * @return
     */
    public long newLongId() {
        return longIdGenerator.newId(niceIdGenConfig.getMachineId(), niceIdGenConfig.getStartTime());
    }

    /**
     * 配置id生成参数
     *
     * @param niceIdGenConfig
     */
    public void config(NiceIdGenConfig niceIdGenConfig) {
        this.niceIdGenConfig = niceIdGenConfig;
        logger.info("设置配置：{}", niceIdGenConfig);
    }

    /**
     * 获取一个新的String类型id
     *
     * @return
     */
    public String newStringId() {
        return stringIdGenerator.newId(niceIdGenConfig.getMachineId(), niceIdGenConfig.getStartTime());
    }
}
