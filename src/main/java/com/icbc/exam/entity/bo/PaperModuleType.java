package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: PaperModuleType
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/10 13:53
 */
@Data
public class PaperModuleType {
    /**
     * 模块类型
     */
    private String moduleType;
    /**
     * 题量
     */
    private String questionNum;

}
