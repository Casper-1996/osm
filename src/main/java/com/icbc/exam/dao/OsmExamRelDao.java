package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.po.OsmExamRelModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: liurong
 * @title: OsmExamRelDao
 * @projectName: plm_mgmt_baddebt
 * @description: 试卷题目表(OSM_EXAM_REL)表数据库访问层
 * @data: 2021-04-09 14:10:05
 */
@Repository
public interface OsmExamRelDao extends BaseMapper<OsmExamRelModel> {

    String getRightAnswer(Integer relId);


    int selectByIdCount(String id);
}