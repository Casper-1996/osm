package com.icbc.exam.resource;

import org.springframework.beans.factory.annotation.Autowired;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import org.springframework.web.bind.annotation.*;
import com.icbc.exam.service.OsmMultipleChoiceService;

   /**
 * @author: liurong
 * @title: OsmMultipleChoiceResource
 * @projectName: plm_mgmt_baddebt
 * @description: 选择题(OSM_MULTIPLE_CHOICE)表控制层
 * @data: 2021-04-01 17:17:03
 */
 
@RestController
public class OsmMultipleChoiceResource {
    /**
     * 服务对象
     */
    @Autowired
    private OsmMultipleChoiceService osmMultipleChoiceService;

    

}