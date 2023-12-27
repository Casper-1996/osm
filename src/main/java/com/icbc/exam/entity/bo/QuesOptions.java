package com.icbc.exam.entity.bo;

import lombok.Data;

/**
 * @author liurong
 * @title: QuesOptions
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/9 15:36
 */
@Data
public class QuesOptions {
    private String optionId;
    private String optionContent;
    private Integer correctAnswer;
}
