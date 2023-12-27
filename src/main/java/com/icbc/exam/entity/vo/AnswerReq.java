package com.icbc.exam.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lida
 * @title:
 * @description:
 * @date 2021/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerReq {
    /**
     * 选择题答案
     */
    private String select;
    /**
     *  判断，主观答案1
     */
    private Integer judgment;
    /**
     * 主观题答案2
     */
    private String reason;
}
