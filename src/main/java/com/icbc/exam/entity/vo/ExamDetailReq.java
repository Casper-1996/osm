package com.icbc.exam.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lida
 * @title:
 * @description:
 * @date 2021/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailReq {
    /**
     * 关系ID
     */
    private Integer relId;
    /**
     * 考试Id
     */
    private String examId;
    /**
     * 题目ID
     */
    private String questionId;
    /**
     * 题型
     */
    private Integer questionType;
    /**
     * 答案
     */
    private AnswerReq answerReq;
}
