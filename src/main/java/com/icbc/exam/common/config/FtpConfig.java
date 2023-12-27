package com.icbc.exam.common.config;


import com.icbc.exam.entity.pojo.ftp.FtpInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liurong
 * @title:
 * @projectName pes-mgmt-main
 * @description:
 * @date 2021/1/12 14:39
 */
@Component
@Data
@ConfigurationProperties(prefix = "osm.ftps")
public class FtpConfig {
    private Map<String, FtpInfo> ftp;
}
