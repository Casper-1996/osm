package com.icbc.exam.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icbc.exam.entity.po.OsmExamDetailModel;
import com.icbc.exam.entity.vo.ExamSubmitReq;

import java.util.List;

 /**
 * @author: liurong
 * @title: OsmExamDetailService
 * @projectName: plm_mgmt_baddebt
 * @description: 考试明细表(OSM_EXAM_DETAIL)表服务接口
 * @data: 2021-04-07 16:48:22
 */
 
@DS("master")
public interface OsmExamDetailService extends IService<OsmExamDetailModel> {

     /**
      * 提交答案
      * @param req 请求
      * @param userId 用户id
      */
     void submit(ExamSubmitReq req, String userId);

     /**
      * 获取多选题答题
      * @param examId
      * @param userId
      * @return
      */
     List<OsmExamDetailModel> getMultiples(String examId, String userId);

     /**
      * 判断题判卷
      * @param examId 考试id
      * @param userId 用户id
      * @param judgePoint 每道判断题的分数
      */
     void judgePointRight(String examId, String userId, int judgePoint);

     /**
      * 单选题判卷
      * @param examId 考试id
      * @param userId 用户id
      * @param singlePoint 每道单选题的分数
      */
     void singlePointRight(String examId, String userId, int singlePoint);

     /**
      * 多选题判卷
      * @param relId 考试id
      * @param userId 用户id
      * @param multiplePoint 每道多选题的分数
      */
     void multiplePointRight(Integer relId, String userId, int multiplePoint);

     /**
      * 更新客观题总分
      * @param examId 考试ID
      * @param userId 用户ID
      */
     void modifyObjectScore(String examId, String userId);
 }