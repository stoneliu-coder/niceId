package com.ll.niceId.springboot.starter.config;

import com.ll.niceId.core.impl.NiceIdGen;
import com.ll.niceId.core.config.NiceIdGenConfig;
import com.ll.niceId.springboot.starter.properties.NiceIdProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tomliu
 */
@Configuration
@RequiredArgsConstructor
public class NiceIdAutoConfiguration {

    private final NiceIdProperties niceIdProperties;

    @Bean
    public NiceIdGen init(NiceIdGen niceIdGen) {
        NiceIdProperties.MachineDiscoveryType machineDiscoveryType = niceIdProperties.getMachineDiscoveryType();
        short machineId = 0;
        if (machineDiscoveryType == NiceIdProperties.MachineDiscoveryType.Manual) {
            machineId = niceIdProperties.getMachineId();
        }
        NiceIdGenConfig config = new NiceIdGenConfig();
        config.setMachineId(machineId);
        config.setStartTime(niceIdProperties.getStartTime());
        niceIdGen.config(config);
        return niceIdGen;
    }
}
