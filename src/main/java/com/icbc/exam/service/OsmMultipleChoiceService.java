package com.icbc.exam.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;


import java.util.List;

/**
 * @author: liurong
 * @title: OsmMultipleChoiceService
 * @projectName: plm_mgmt_baddebt
 * @description: 选择题(OSM_MULTIPLE_CHOICE)表服务接口
 * @data: 2021-04-01 17:04:26
 */

@DS("master")
public interface OsmMultipleChoiceService extends IService<OsmMultipleChoiceModel> {


    boolean removeByPrimaryId(String topicId);

    OsmMultipleChoiceModel findOptionById(String optionId);

    int selectByIdCount(String topicId);
}