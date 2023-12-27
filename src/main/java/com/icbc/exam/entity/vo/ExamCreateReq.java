package com.icbc.exam.entity.vo;

import com.icbc.exam.entity.bo.PaperQuesType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liurong
 * @title: ExamCreateReq
 * @projectName osm-mgmt-exam
 * @description: 试卷创建
 * @date 2021/4/7 14:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamCreateReq {
    /**
     * 试卷id 修改时传
     */
    private String exId;
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
     * 考试时长
     */
    private String examDuration;

    private String paperQuesType2;
    private String successMsg;

    /**
     * 查询题库各题型数量
     */
    private List<PaperQuesType> paperQuesType;

}
