package com.icbc.exam.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author liurong
 * @title: PaperQuesType
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/10 13:50
 */
@Data
public class PaperQuesType {
    /**
     * 题型
     */
    private String questionType;
    /**
     * 各模块数量
     */
    private List<PaperModuleType> moduleTypeNum;

}
