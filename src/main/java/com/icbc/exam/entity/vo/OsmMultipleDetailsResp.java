package com.icbc.exam.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cxk
 * @title: ${CLASS}
 * @projectName: osm-mgmt-exam
 * @description:
 * @date: 2021/4/7 11:34
 */
@Data
public class OsmMultipleDetailsResp extends OsmQuestionBankRecordModel{


    /**
     * 题目答案(选择题 0:错，1:对)
     */
    private List<OsmMultipleChoiceModel> omcModelList;

    /**
     * 模块类型
     */
    private String moduleTypeStr;

    /**
     * 试题标志(是否模拟题 0:否，1:是)
     */
    private String questionMarkStr;

    /**
     * 适用层级
     */
    private String scopeAppliactionStr;

    /**
     * 题目类型(0：判断题，1：主观题，2：选择题 3:多选题）
     */
    private String questionTypeStr;

    /**
     * 题目答案(选择题 0:错，1:对)
     */
    private String correctAnswerStr;

    /**
    * 使用范围
    **/
    private List<String> scopeUser;



}
