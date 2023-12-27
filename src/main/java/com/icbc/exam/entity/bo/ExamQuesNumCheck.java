package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: ExamQuesNumCheck
 * @projectName osm-mgmt-exam
 * @description: 校验对应题型数量
 * @date 2021/4/7 15:16
 */
@Data
public class ExamQuesNumCheck {
    /**
     * 使用范围
     */
    private Integer scopeUser;
    /**
     * 适用层级
     */
    private Integer scopeAppliaction;

    /**
     * 题目类型
     */
    private String questionType;

}
