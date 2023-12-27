package com.icbc.exam.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liurong
 * @title: ExamManageListReq
 * @projectName osm-mgmt-exam
 * @description: 查询考试管理列表
 * @date 2021/4/12 9:38
 */
@Data
public class ExamManageListReq {

    /**
     * 　页码
     */
    private Integer pageNum;

    /**
     * 　 页容量
     */
    private Integer pageSize;

    /**
     * 考试名称
     */
    private String examName;
    /**
     * 使用范围
     */
    private Integer scopeUser;

    /**
     * 适用层级
     */
    private Integer scopeAppliaction;

    /**
     * 考试开始时间
     */
    private String examStartDate;
    /**
     * 考试结束时间
     */
    private String examEndDate;
    /**
     * 考试状态 1已开始  2已结束   3未开始
     */
    private String examStatue;

    /**
     * 当前时间   前端不用传
     */
    private Date nowDate;

}
