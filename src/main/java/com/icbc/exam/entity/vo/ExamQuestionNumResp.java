package com.icbc.exam.entity.vo;

import com.icbc.exam.entity.bo.QuestionTypeNum;
import lombok.Data;

import java.util.List;

/**
 * @author liurong
 * @title: ExamQuestionNumResp
 * @projectName osm-mgmt-exam
 * @description: 查询题库各模块题型数量出参
 * @date 2021/4/8 15:39
 */
@Data
public class ExamQuestionNumResp {
    /**
     * 模块类型
     */
    private String moduleType;
    /**
     * 模块名字
     */
    private String moduleName;
    /**
     * 题量
     */
    private String quesTotal;
    /**
     * 各模块题型数量
     */
    private List<QuestionTypeNum> questionTypeNum;


}
