package com.icbc.exam.process.listener.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lida
 * @title:
 * @projectName：plm_mgmt_baddebt
 * @description:
 * @date 2020/7/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeEventModel {
    private String examId;
    private String userId;
}
