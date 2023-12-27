package com.icbc.exam.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icbc.exam.entity.po.OsmExamRelModel;

import java.util.List;

/**
 * @author: liurong
 * @title: OsmExamRelService
 * @projectName: plm_mgmt_baddebt
 * @description: 试卷题目表(OSM_EXAM_REL)表服务接口
 * @data: 2021-04-09 14:10:05
 */

@DS("master")
public interface OsmExamRelService extends IService<OsmExamRelModel> {

    /**
     * 获取正确答案
     * @param relId 题目关系id
     * @return
     */
    String getRightAnswer(Integer relId);
}