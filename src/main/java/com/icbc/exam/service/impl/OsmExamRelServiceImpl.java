package com.icbc.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icbc.exam.entity.po.OsmExamRelModel;
import com.icbc.exam.dao.OsmExamRelDao;
import com.icbc.exam.service.OsmExamRelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: liurong
 * @title: OsmExamRelServiceImpl
 * @projectName: plm_mgmt_baddebt
 * @description: 试卷题目表(OSM_EXAM_REL)表服务实现类
 * @data: 2021-04-09 14:10:05
 */

@Service
@Transactional
public class OsmExamRelServiceImpl  extends ServiceImpl<OsmExamRelDao, OsmExamRelModel> implements OsmExamRelService  {
    @Autowired
    private OsmExamRelDao osmExamRelDao;


    @Override
    public String getRightAnswer(Integer relId) {
        return osmExamRelDao.getRightAnswer(relId);
    }
}