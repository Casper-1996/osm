package com.icbc.exam.entity.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lida
 * @title: 选择题
 * @projectName：plm_mgmt_npl
 * @description:
 * @date 2020/6/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MultipleChoiceExcelModel extends BaseRowModel {

    @ExcelProperty(value = "编号", index = 0)
    public String id;

    @ExcelProperty(value = "使用范围", index = 1)
    public String scopeUser;

    @ExcelProperty(value = "题目名称", index = 2)
    public String titleName;

    @ExcelProperty(value = "题目内容", index = 3)
    public String titleContent;

    @ExcelProperty(value = "选项A", index = 4)
    public String optionA;

    @ExcelProperty(value = "选项B", index = 5)
    public String optionB;

    @ExcelProperty(value = "选项C", index = 6)
    public String optionC;

    @ExcelProperty(value = "选项D", index = 7)
    public String optionD;

    @ExcelProperty(value = "是否正确答案", index = 8)
    private String correctAnswer;

    @ExcelProperty(value = "模块类型", index = 9)
    private String moduleType;

    @ExcelProperty(value = "试题标志\n(是否模拟题)", index = 10)
    private String questionMark;

    @ExcelProperty(value = "附件上传文件名", index = 11)
    private String uploadFileName;

    @ExcelProperty(value = "适用层级", index = 12)
    private String scopeAppliaction;


}
