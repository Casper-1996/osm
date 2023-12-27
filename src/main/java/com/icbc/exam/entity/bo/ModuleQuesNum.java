package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: ModuleQuesNum
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/7 17:10
 */
@Data
public class ModuleQuesNum {
    /**
     * 题型
     */
    private String questionType;
    /**
     * 题型数量
     */
    private String questionNum;
}
