package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
 
 /**
 * @author: liurong
 * @title: OsmMultipleChoiceDao
 * @projectName: plm_mgmt_baddebt
 * @description: 选择题(OSM_MULTIPLE_CHOICE)表数据库访问层
 * @data: 2021-04-01 17:04:23
 */
public interface OsmMultipleChoiceDao extends BaseMapper<OsmMultipleChoiceModel>{


     List<OsmMultipleChoiceModel> selectByPrimaryId(@Param("primaryId") String primaryId);

     int removeByPrimaryId(@Param("primaryId") String primaryId);

     int selectByIdCount(String topicId);

 }