package com.icbc.exam.entity.vo;

import lombok.Data;

/**
 * @author liurong
 * @title: ExamManageListResp
 * @projectName osm-mgmt-exam
 * @description: 查询考试管理列表
 * @date 2021/4/12 9:48
 */
@Data
public class ExamManageListResp {
    /**
     * 试卷id
     */
    private String examId;
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
     * 参考人数
     */
    private String examNum;
    /**
     * 考试状态 1未开始  2(考试中)已开始  3未分配判卷 4判卷中 5已完成
     */
    private String examStatue;
    /**
     * 考试状态 1已开始  2已结束   3未开始
     */
    private String examStatueTran;
}
