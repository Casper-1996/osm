package com.icbc.exam.service.impl;

import com.icbc.exam.entity.po.OsmExamInfoModel;
import com.icbc.exam.dao.OsmExamInfoDao;
import com.icbc.exam.service.OsmExamInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

  /**
 * @author: liurong
 * @title: OsmExamInfoServiceImpl
 * @projectName: plm_mgmt_baddebt
 * @description: 考试记录表(OSM_EXAM_INFO)表服务实现类
 * @data: 2021-04-07 16:48:40
 */
 
@Service
@Transactional
public class OsmExamInfoServiceImpl implements OsmExamInfoService {
    @Autowired
    private OsmExamInfoDao osmExamInfoDao;
    
    @Autowired
    private OsmExamInfoService osmExamInfoService;

    
}