package com.ll.niceId.springboot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.ll.niceId.springboot.starter.properties.NiceIdProperties.PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = PREFIX)
public class NiceIdProperties {
     static final String PREFIX = "niceid";

    /**
     * 机器编号
     * <p>
     *     机器编号需要保证在整体集群中唯一，否则可能造成生成的id重复；本配置仅当machineDiscoveryType为manual时有效。
     * </p>
     */
    private short machineId = 0;

    /**
     * id时间部分的起始时间，默认值：2021-01-01
     */
    private String startTime;

    /**
     * 机器发现方式
     */
    private MachineDiscoveryType machineDiscoveryType;

    /**
     * 机器发现类型
     */
    public enum MachineDiscoveryType{
        /**
         * 手动
         */
        Manual,


    }
}

