package com.icbc.exam.entity.pojo.mail;

import lombok.Data;

import java.util.Vector;

/**
 * @author cyt
 * @title: IcbcMail
 * @projectName cbb_gateway_ucenter
 * @description: TODO
 * @date 2020/8/10 10:10
 */
@Data
public class IcbcMail {
    /**
    　* @description: 接收人列表
    　*/
    private Vector<String> mailToList;
    /**
     　* @description: 抄送人列表
     　*/
    private Vector<String> mailccList;
    /**
     　* @description: 密送人列表
     　*/
    private Vector<String> mailbccList;
    /**
     　* @description: 主题
     　*/
    private String subject;
    /**
     　* @description: 内容
     　*/
    private String content;
    /**
     　* @description: 文件列表
     　*/
    private Vector<String> fileList;
}
