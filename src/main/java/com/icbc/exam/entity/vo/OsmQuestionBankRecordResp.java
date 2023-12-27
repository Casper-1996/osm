package com.icbc.exam.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import com.icbc.exam.entity.po.OsmScopeUserModel;
import lombok.Data;

import java.util.List;

/**
 * @author cxk
 * @title: ${CLASS}
 * @projectName: osm-mgmt-exam
 * @description:
 * @date: 2021/4/6 10:46
 */
@Data
public class OsmQuestionBankRecordResp extends OsmQuestionBankRecordModel {

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
     * 题目类型(0：主观题，1：判断题，2：选择题 3:多选题）
     */
    private String questionTypeStr;

    /**
     * 题目答案(选择题 0:错，1:对)
     */
    private String correctAnswerStr;

    /**
     * 使用范围
     **/
    private List<OsmScopeUserModel> scopeUserModelList;

    /**
     * 使用范围
     **/
    private String scopeUser;

    /**
     *
     * 是否可进行操作 0 否 1 是
     **/
    private int flag;

}

