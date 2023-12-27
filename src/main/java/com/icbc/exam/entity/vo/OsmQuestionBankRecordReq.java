package com.icbc.exam.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
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
public class OsmQuestionBankRecordReq extends OsmQuestionBankRecordModel {
    /**
     * 题目名称
     **/
    private String titleName;

    /**
     * 题目类型
     **/
    private Integer questionType;

    /**
     * 试题标志(是否模拟题 0:否定，1:是)
     */
    private Integer questionMark;

    /**
     * 适用层级
     */
    private Integer scopeAppliaction;

    /**
     * 使用范围
     */
    private List<String> scopeUsers;

    /**
     * 模块类型
     */
    private Integer moduleType;

    /**
     * 　页码
     */
    private Integer pageNum;

    /**
     * 　 页容量
     */
    private Integer pageSize;
}
