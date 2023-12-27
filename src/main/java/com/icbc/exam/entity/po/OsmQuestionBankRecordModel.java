package com.icbc.exam.entity.po;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: liurong
 * @title: OcmQuestionBankRecordModel
 * @projectName: plm_mgmt_npl
 * @description: 题库记录表(OCM_QUESTION_BANK_RECORD)实体类
 * @data: 2021-04-01 17:28:17
 */
@Data
@TableName("OSM_QUESTION_BANK_RECORD")
public class OsmQuestionBankRecordModel implements Serializable {
    private static final long serialVersionUID = -33072293865919210L;

    /**
     * id
     */
    @TableId("ID")
    private String id;
    /**
     * 题目名称
     */
    @TableField("TITLE_NAME")
    private String titleName;
    /**
     * 题目内容
     */
    @TableField("TITLE_CONTENT")
    private String titleContent;
    /**
     * 模块类型
     */
    @TableField("MODULE_TYPE")
    private Integer moduleType;
    /**
     * 试题标志(是否模拟题 0:否定，1:是)
     */
    @TableField("QUESTION_MARK")
    private Integer questionMark;
    /**
     * 上传附件名
     */
    @TableField("UPLOAD_FILE_NAME")
    private String uploadFileName;
    /**
     * 适用层级
     */
    @TableField("SCOPE_APPLIACTION")
    private Integer scopeAppliaction;
    /**
     * 题目类型(0：主观题，1：判断题，2：单选题，3：多选题）
     */
    @TableField("QUESTION_TYPE")
    private Integer questionType;
    /**
     * 题目答案(选择题 0:错，1:对)
     */
    @TableField("CORRECT_ANSWER")
    private Integer correctAnswer;

    /**
     * 参考答案
     */
    @TableField("REFERENCE_ANSWER")
    private String referenceAnswer;

    /**
     * 插入时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;
    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;


}