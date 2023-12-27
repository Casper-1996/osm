package com.icbc.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.exam.entity.bo.CreatePaper;
import com.icbc.exam.entity.bo.ExamPreviewInfo;
import com.icbc.exam.entity.bo.PaperQuestion;
import com.icbc.exam.entity.bo.QuesOptions;
import com.icbc.exam.entity.po.OsmExamListModel;
import com.icbc.exam.entity.po.OsmExamRelModel;
import com.icbc.exam.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author: liurong
 * @title: OsmExamListDao
 * @projectName: osm-mgmt-exam
 * @description: 考试试卷管理(OSM_EXAM_LIST)表数据库访问层
 * @data: 2021-04-07 11:57:42
 */
public interface OsmExamListDao extends BaseMapper<OsmExamListModel>{


     List<PaperQuestion> qryModuleQues(CreatePaper createPaper);

    List<QuesOptions> findQuesOption(@Param("primaryId") String id);

    List<PaperQuestion> findSubQues(@Param("questionNum") String subNum, @Param("scopeAppliaction") Integer scopeAppliaction, @Param("scopeUser") Integer scopeUser);

    List<String> findQuesSeq(@Param("code") String code);

    int findExamDate(@Param("examId") String examId,@Param("nowDate") Date date);

    List<ExamManageListResp> qryExamList(ExamManageListReq entity);

    int findExamState(@Param("examId") String examId,@Param("nowDate")  Date date);

    void deleteExamQuesTion(@Param("examId") String examId);

    ExamPreviewInfo selectPreviewExam(@Param("examId") String examId,@Param("nowDate")  Date date);

    List<MyExamManageListResp> qryMyExamList(MyExamManageListReq entity);

    List<OsmExamRelModel> findExamPaperAllQues(@Param("examId") String examId);

    List<JudAdminResp> findjudAdmin(JudAdminReq entity);
}