package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: QuestionTypeNum
 * @projectName osm-mgmt-exam
 * @description: 各模块题型数量
 * @date 2021/4/8 15:45
 */
@Data
public class QuestionTypeNum {
    /**
     * 题型
     */
    private String questionType;
    /**
     * 题型名字
     */
    private String questionName;
    /**
     * 题型数量
     */
    private String questionNum;
}
