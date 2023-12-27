package com.icbc.exam.common.enums;

/**
 * @author cyt
 * @title: DictEnum
 * @projectName plm_mgmt_npl
 * @description: 字典类别枚举
 * @date 2020/5/28 12:12
 */

public enum DictEnum {
    BANK_LEVEL("bankLevel", "银行级别"),
    AREA_TO_BANK("areaToBank", "区域对应二级分行"),
    SIGN_STATE("signState", "签约状态"),
    RECON_RESULT("reconResult", "对账结果"),
    POINT_CONSU_TYPE("pointConsuType", "积分消费类别"),
    POINT_TYPE("pointType", "积分类别"),
    ISP_PAY_STATE("ispPayState", "支付状态"),
    STATUS_STATE("statusState", "订单状态"),
    SCOPE_USER("scopeUser","使用范围"),
    MODULE_TYPE("moduleType","模块类型"),
    SCOPE_APPLIACTION("scopeAppliaction","适用层级"),
    ANSWER("answer","题目答案"),
    WHETHER("whether","通用是否"),
    QUESTION_TYPE("questionType", "题型"),
    QUESTION_TYPE_POINT("questionTypePoints", "题型分数"),
    QUESTION_TYPE_NUM("questionTypeNum", "题型数量"),
    MYEXAM_STATE("myexamState","我的考试状态"),
    EXAM_STATE("examState","考试状态"),
    QUESTION_MARK("questionMark","试题标志");


    private String code;
    private String name;

    private DictEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
        for (DictEnum enumitem : DictEnum.values()) {
            if (enumitem.getCode().equals(code)) {
                return enumitem.getName();
            }
        }
        return null;
    }

}
