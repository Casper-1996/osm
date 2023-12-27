package com.icbc.exam.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "thread.pool")
@Data
public class ThreadPoolConfig {

    private Integer maxNum;

    private Integer coreNum;

    private Integer outTime;

}
