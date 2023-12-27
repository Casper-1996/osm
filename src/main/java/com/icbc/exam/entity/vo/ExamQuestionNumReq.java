package com.icbc.exam.entity.vo;

import lombok.Data;

/**
 * @author liurong
 * @title: ExamQuestionNumReq
 * @projectName osm-mgmt-exam
 * @description: 查询题库各模块题型数量入参
 * @date 2021/4/8 15:41
 */
@Data
public class ExamQuestionNumReq {
    /**
     * 使用范围
     */
    private Integer scopeUser;
    /**
     * 适用层级
     */
    private Integer scopeAppliaction;

}
