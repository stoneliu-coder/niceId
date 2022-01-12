package com.ll.niceId.core;

import com.ll.niceId.core.config.NiceIdGenConfig;
import com.ll.niceId.core.impl.LongIdGen;
import com.ll.niceId.core.impl.StringIdGen;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



/**
 * @description: com.ll.niceId.NiceId
 * @author: Tomliu
 * @create: 2021-12-28 21:20
 **/
@Component
@RequiredArgsConstructor
public class NiceIdGen {

    private final Logger logger = LoggerFactory.getLogger(NiceIdGen.class);

    /**
     * long 类型id生成器
     */
    private final LongIdGen longIdGernerator;

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
        return longIdGernerator.newId(niceIdGenConfig.getMachineId(), niceIdGenConfig.getStartTime());
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
