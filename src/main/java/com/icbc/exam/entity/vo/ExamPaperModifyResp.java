package com.icbc.exam.entity.vo;

import lombok.Data;

/**
 * @author liurong
 * @title: ExamPaperModifyResp
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/12 16:14
 */
@Data
public class ExamPaperModifyResp {
    /**
     * 考试名称
     */
    private String examName;
    /**
     * 使用范围
     */
    private Integer scopeUser;
    /**
     * 使用范围翻译
     */
    private String scopeUserTran;
    /**
     * 适用层级
     */
    private Integer scopeAppliaction;
    /**
     * 适用层级翻译
     */
    private String scopeAppliactionTran;
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
     * 考试状态 1已开始  2已结束   3未开始
     */
    private String examStatueTran;

    /**
     * 考试状态 1已开始  2已结束   3未开始
     */
    private String examStatue;

    /**
     * 模块比例
     */
    private String moduleTypeScale;

    /**
     * 查询题库各题型数量
     */
    private String paperQuesPreviewType;
}
