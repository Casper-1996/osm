package com.icbc.exam.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icbc.exam.entity.po.OsmScopeUserModel;

import java.util.List;

 /**
 * @author: liurong
 * @title: OsmScopeUserService
 * @projectName: plm_mgmt_baddebt
 * @description: 题库使用范围表(OSM_SCOPE_USER)表服务接口
 * @data: 2021-04-06 14:07:02
 */
 
@DS("master")
public interface OsmScopeUserService extends IService<OsmScopeUserModel> {

     List<String> selectById(String id);

     void removeOld(String scopeUser, String id);
 }