package com.icbc.exam.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liurong
 * @title: MyExamManageListReq
 * @projectName osm-mgmt-exam
 * @description: 我的考试列表
 * @date 2021/4/13 14:26
 */
@Data
public class MyExamManageListReq {

    /**
     * 　页码
     */
    private Integer pageNum;

    /**
     * 　 页容量
     */
    private Integer pageSize;

    /**
     * 用户id  前端不用传
     */
    private String userId;

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
     * 考试状态 1未开始  2未考试   3已完成  4已过期
     */
    private String examState;

    /**
     * 当前时间   前端不用传
     */
    private Date nowDate;

}
