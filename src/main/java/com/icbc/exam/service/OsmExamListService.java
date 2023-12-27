package com.icbc.exam.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.vo.*;

import java.util.List;

 /**
 * @author: liurong
 * @title: OsmExamListService
 * @projectName: osm-mgmt-exam
 * @description: 考试试卷管理(OSM_EXAM_LIST)表服务接口
 * @data: 2021-04-07 11:57:42
 */
 
@DS("master")
public interface OsmExamListService {


     ResultData createPaper(ExamCreateReq entity);

     ResultData qryModuleQuestionNum(ExamQuestionNumReq entity);

     ResultData startExam(StartExamReq entity);

     ResultData decryptExam(List<ExamPaperResp> examPaper);

     ResultData qryExamList(ExamManageListReq entity);

     ResultData deleteExam(String examId);

     ResultData previewExam(String examId);

     ResultData modifyExamEcho(String examId);

     int findExamState(String eId);

     ResultData qryMyExamList(MyExamManageListReq entity);

     List<JudAdminResp> findjudAdmin(JudAdminReq entity);
 }