package com.icbc.exam.entity.vo;

import com.icbc.exam.entity.bo.Options;
import lombok.Data;

import java.util.List;

/**
 * @author liurong
 * @title: ExamPaperResp
 * @projectName osm-mgmt-exam
 * @description: 试卷出参 (开始考试)
 * @date 2021/4/7 14:22
 */
@Data
public class ExamPaperResp {
    private String questionType;
    private String questionTypeName;
    private String fileUrl;
    private String question;
    private String questionId;
    private List<Options> options;
}
