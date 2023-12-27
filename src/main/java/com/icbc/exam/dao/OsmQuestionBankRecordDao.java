package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.bo.ExamQuesNumCheck;
import com.icbc.exam.entity.bo.ModuleQuesNum;
import com.icbc.exam.entity.bo.ModuleScale;
import com.icbc.exam.entity.bo.QuestionTypeNum;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordReq;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordResp;

import java.util.List;
 
 /**
 * @author: liurong
 * @title: OcmQuestionBankRecordDao
 * @projectName: plm_mgmt_baddebt
 * @description: 题库记录表(OCM_QUESTION_BANK_RECORD)表数据库访问层
 * @data: 2021-04-01 17:28:17
 */
public interface OsmQuestionBankRecordDao extends BaseMapper<OsmQuestionBankRecordModel>{


     int findQuestionTypeNum(ExamQuesNumCheck examQuesNumCheck);

     QuestionTypeNum findModuleQuesNum(ModuleScale moduleScale);

     List<OsmQuestionBankRecordResp> existingQuestions(OsmQuestionBankRecordReq req);

     List<OsmQuestionBankRecordResp> noExistingQuestions(OsmQuestionBankRecordReq req);

     List<OsmQuestionBankRecordResp> ccQuestions(OsmQuestionBankRecordReq req);
 }