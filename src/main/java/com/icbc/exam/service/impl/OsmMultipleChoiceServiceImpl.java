package com.icbc.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icbc.exam.dao.OsmMultipleChoiceDao;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import com.icbc.exam.service.OsmMultipleChoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

  /**
 * @author: liurong
 * @title: OsmMultipleChoiceServiceImpl
 * @projectName: plm_mgmt_baddebt
 * @description: 选择题(OSM_MULTIPLE_CHOICE)表服务实现类
 * @data: 2021-04-01 17:04:26
 */
 
@Service
@Transactional
public class OsmMultipleChoiceServiceImpl
        extends ServiceImpl<OsmMultipleChoiceDao, OsmMultipleChoiceModel> implements OsmMultipleChoiceService {
    @Autowired
    private OsmMultipleChoiceDao osmMultipleChoiceDao;

    @Autowired
    private OsmMultipleChoiceService service;


    @Override
    public boolean removeByPrimaryId(String topicId) {
        int num = osmMultipleChoiceDao.removeByPrimaryId(topicId);
        return num > 0;
    }

    @Override
    public int selectByIdCount(String topicId) {
        return osmMultipleChoiceDao.selectByIdCount(topicId);
    }

      @Override
      public OsmMultipleChoiceModel findOptionById(String optionId) {
          return osmMultipleChoiceDao.selectById(optionId);
      }
  }