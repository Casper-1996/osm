package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: CreatePaper
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/8 17:23
 */
@Data
public class CreatePaper {
    /**
     * 模块类型
     */
    private String moduleType;
    /**
     * 题型
     */
    private String questionType;
    /**
     * 使用范围
     */
    private Integer scopeUser;
    /**
     * 适用层级
     */
    private Integer scopeAppliaction;
    /**
     * 题型数量
     */
    private String questionNum;
}
