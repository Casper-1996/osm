package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.po.OsmScopeUserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: liurong
 * @title: OsmScopeUserDao
 * @projectName: plm_mgmt_baddebt
 * @description: 题库使用范围表(OSM_SCOPE_USER)表数据库访问层
 * @data: 2021-04-06 14:07:02
 */
public interface OsmScopeUserDao extends BaseMapper<OsmScopeUserModel> {


    List<OsmScopeUserModel> selectScopeUser(String bankRecordId);

    List<String> selectBybankRecordId(String id);

    void removeOld(@Param("bankRecordId") String bankRecordId, @Param("scopeUser") String scopeUser);
}