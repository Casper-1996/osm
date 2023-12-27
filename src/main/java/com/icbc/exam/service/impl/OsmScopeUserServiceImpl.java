package com.icbc.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icbc.exam.entity.po.OsmScopeUserModel;
import com.icbc.exam.dao.OsmScopeUserDao;
import com.icbc.exam.service.OsmScopeUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: liurong
 * @title: OsmScopeUserServiceImpl
 * @projectName: plm_mgmt_baddebt
 * @description: 题库使用范围表(OSM_SCOPE_USER)表服务实现类
 * @data: 2021-04-06 14:07:02
 */

@Service
@Transactional
public class OsmScopeUserServiceImpl
        extends ServiceImpl<OsmScopeUserDao, OsmScopeUserModel> implements OsmScopeUserService {
    @Autowired
    private OsmScopeUserDao osmScopeUserDao;

    @Autowired
    private OsmScopeUserService service;


    @Override
    public List<String> selectById(String id) {
        return osmScopeUserDao.selectBybankRecordId(id);
    }

    @Override
    public void removeOld(String scopeUser, String id) {
        osmScopeUserDao.removeOld(id, scopeUser);
    }
}