package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.po.OsmExamInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: liurong
 * @title: OsmExamInfoDao
 * @projectName: plm_mgmt_baddebt
 * @description: 考试记录表(OSM_EXAM_INFO)表数据库访问层
 * @data: 2021-04-07 16:48:40
 */
@Repository
public interface OsmExamInfoDao extends BaseMapper<OsmExamInfoModel> {


    void modifyEndTime(@Param("examId") String examId, @Param("userId") String userId);

    void modifyObjectScore(@Param("examId") String examId, @Param("userId") String userId);
}