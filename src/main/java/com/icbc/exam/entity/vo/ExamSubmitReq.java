package com.icbc.exam.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author lida
 * @title:
 * @description:
 * @date 2021/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSubmitReq {

    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 试卷结果
     */
    private List<ExamDetailReq> detailReqList;
}
