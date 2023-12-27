package com.icbc.exam.entity.pojo.sms;

import lombok.Data;

import java.util.Map;

/**
 * @author liurong
 * @title: SinglePointPhoneMsg
 * @projectName pes-mgmt-system
 * @description:
 * @date 2021/1/28 11:16
 */
@Data
public class SinglePointPhoneMsg {
    private Map<String, String> sendMsg;
}
