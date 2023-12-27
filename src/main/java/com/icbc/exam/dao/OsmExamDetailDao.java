package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.po.OsmExamDetailModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: liurong
 * @title: OsmExamDetailDao
 * @projectName: plm_mgmt_baddebt
 * @description: 考试明细表(OSM_EXAM_DETAIL)表数据库访问层
 * @data: 2021-04-07 16:48:22
 */
@Repository
public interface OsmExamDetailDao extends BaseMapper<OsmExamDetailModel> {


    List<OsmExamDetailModel> getMultiples(@Param("examId") String examId, @Param("userId") String userId);

    void judgePointRight(@Param("examId") String examId, @Param("userId") String userId,
                         @Param("judgePoint") int judgePoint);

    void singlePointRight(@Param("examId") String examId, @Param("userId") String userId,
                          @Param("singlePoint") int singlePoint);

    void multiplePointRight(@Param("relId") Integer relId, @Param("userId") String userId,
                            @Param("multiplePoint") int multiplePoint);
}