package com.icbc.exam.entity.vo;

import com.icbc.exam.entity.bo.Options;
import lombok.Data;

import java.util.List;

/**
 * @author liurong
 * @title: ExamPreviewResp
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/12 14:59
 */
@Data
public class ExamPreviewResp {
    private String questionType;
    private String questionTypeName;
    private String fileUrl;
    private String question;
    private String questionId;
    /**
     * 题目答案( 0:错，1:对)
     */
    private String correctAnswer;
    /**
     * 参考答案
     */
    private String referenceAnswer;
    private List<Options> options;
}
