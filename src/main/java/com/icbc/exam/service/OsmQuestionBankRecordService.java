package com.icbc.exam.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.icbc.exam.entity.bo.ExamQuesNumCheck;
import com.icbc.exam.entity.bo.ModuleQuesNum;
import com.icbc.exam.entity.bo.ModuleScale;
import com.icbc.exam.entity.bo.QuestionTypeNum;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import com.icbc.exam.entity.vo.OsmAddOrModeifyReq;
import com.icbc.exam.entity.vo.OsmMultipleDetailsResp;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordReq;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordResp;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author: liurong
 * @title: OcmQuestionBankRecordService
 * @projectName: plm_mgmt_baddebt
 * @description: 题库记录表(OCM_QUESTION_BANK_RECORD)表服务接口
 * @data: 2021-04-01 17:28:17
 */

@DS("master")
public interface OsmQuestionBankRecordService extends IService<OsmQuestionBankRecordModel> {


    int findQuestionTypeNum(ExamQuesNumCheck examQuesNumCheck);

    QuestionTypeNum findModuleQuesNum(ModuleScale moduleScale);

    /**
     * 列表查询
     **/
    PageInfo<OsmQuestionBankRecordResp> displayList(OsmQuestionBankRecordReq req) throws Exception;

    /**
     * 非选择题详情
     **/
    OsmMultipleDetailsResp nonMultiple(String topicId) throws Exception;

    /**
     * 选择题详情
     **/
    OsmMultipleDetailsResp multiple(String topicId) throws Exception;

    boolean deleteById(String topicId);

    /**
    * 修改题目
    **/
    boolean updateTopic(OsmAddOrModeifyReq req) throws Exception;

    /**
    * 新增题目
    **/
    void saveTopic(OsmAddOrModeifyReq req) throws Exception;

    String uploadFile(MultipartFile file);

    OsmQuestionBankRecordModel findQuesById(String questionId);

}