package com.icbc.exam.entity.vo;

import lombok.Data;

/**
 * @author liurong
 * @title: MyExamManageListResp
 * @projectName osm-mgmt-exam
 * @description: 我的考试列表
 * @date 2021/4/13 14:30
 */
@Data
public class MyExamManageListResp {
    /**
     * 试卷id
     */
    private String examId;
    /**
     * 考试名称
     */
    private String examName;
    /**
     * 考试开始时间
     */
    private String examStartDate;
    /**
     * 考试结束时间
     */
    private String examEndDate;
    /**
     * 考试时长
     */
    private String examDuration;

    /**
     * 考试用时
     */
    private String examUseTime;
    /**
     * 客观题分数
     */
    private String objectScore;
    /**
     * 主观题分数
     */
    private String subjectScore;
    /**
     * 总分数
     */
    private String totalScore;
    /**
     * 考试状态 1未开始  2未考试   3已完成  4已过期
     */
    private String examState;

    /**
     * 考试状态 1未开始  2未考试   3已完成  4已过期
     */
    private String examStateTran;
}
