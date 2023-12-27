package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: ModuleScale
 * @projectName osm-mgmt-exam
 * @description: 模块比例
 * @date 2021/4/7 16:49
 */
@Data
public class ModuleScale {
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
}
