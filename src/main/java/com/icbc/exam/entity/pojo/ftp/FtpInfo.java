package com.icbc.exam.entity.pojo.ftp;

import lombok.Data;

/**
 * @author liurong
 * @title:
 * @projectName pes-mgmt-main
 * @description: ftp
 * @date 2021/1/12 14:39
 */
@Data
public class FtpInfo {
    private String ftpUsername;

    private String ftpPassword;

    private String ftpIp;

    private String ftpPort;

    private String ftpPath;

    private String ftpEncode;

    private String dataTimeOut;

    private String connectTimeOut;

}
