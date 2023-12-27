package com.icbc.exam.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mq")
@Data
@Component
public class MqProperties {

    private Quene quene;

    @Data
    public static class Quene {

        private String collectionAlert;

    }
}