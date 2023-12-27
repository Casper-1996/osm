package com.icbc.exam.entity.pojo.sms;

import lombok.Data;

import java.util.List;

/**
 * @author liurong
 * @title: GroupSMSPhoneMsg
 * @projectName pes-mgmt-system
 * @description:
 * @date 2021/1/28 11:27
 */
@Data
public class GroupSMSPhoneMsg {
    /**
     　* @description: 接收人列表
     　*/
    private List<String> phoneList;
    /**
     　* @description: 发送信息
     　*/
    private String writeMsg;
}
