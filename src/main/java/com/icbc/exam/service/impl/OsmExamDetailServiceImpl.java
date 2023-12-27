package com.icbc.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icbc.exam.common.constant.ExamConstant;
import com.icbc.exam.dao.OsmExamInfoDao;
import com.icbc.exam.entity.po.OsmExamDetailModel;
import com.icbc.exam.dao.OsmExamDetailDao;
import com.icbc.exam.entity.po.OsmExamInfoModel;
import com.icbc.exam.entity.vo.ExamDetailReq;
import com.icbc.exam.entity.vo.ExamSubmitReq;
import com.icbc.exam.service.OsmExamDetailService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: liurong
 * @title: OsmExamDetailServiceImpl
 * @projectName: plm_mgmt_baddebt
 * @description: 考试明细表(OSM_EXAM_DETAIL)表服务实现类
 * @data: 2021-04-07 16:48:22
 */

@Service
@Transactional
public class OsmExamDetailServiceImpl extends ServiceImpl<OsmExamDetailDao, OsmExamDetailModel>
        implements OsmExamDetailService {
    @Autowired
    private OsmExamDetailDao osmExamDetailDao;
    @Autowired
    private OsmExamInfoDao osmExamInfoDao;

    @Override
    public void submit(ExamSubmitReq req, String userId) {
        List<ExamDetailReq> detailReqList = req.getDetailReqList();
        //一期没有报名入口 先只做插入
        AtomicInteger atomicInteger = new AtomicInteger();
        OsmExamInfoModel examInfoModel = new OsmExamInfoModel();
        examInfoModel.setInfoId(System.currentTimeMillis() + "_" + StringUtils.leftPad(atomicInteger.getAndIncrement() + "", 5, "0"));
        examInfoModel.setStartTime(req.getStartTime());
        examInfoModel.setEndTime(new Date());
        examInfoModel.setExamId(detailReqList.get(0).getExamId());
        examInfoModel.setUserId(userId);
        osmExamInfoDao.insert(examInfoModel);
        //更新考试记录表
        //osmExamInfoDao.modifyEndTime(detailReqList.get(0).getExamId(), userId);
        //插入考试明细表
        List<OsmExamDetailModel> models = new ArrayList<OsmExamDetailModel>();
        detailReqList.forEach(detailReq -> {
            OsmExamDetailModel model = new OsmExamDetailModel();
            model.setRelId(detailReq.getRelId());
            model.setExamId(detailReq.getExamId());
            model.setQuestionId(detailReq.getQuestionId());
            model.setQuestionType(detailReq.getQuestionType());
            model.setUserId(userId);
            model.setScore(new BigDecimal("0"));
            if (detailReq.getQuestionType() == ExamConstant.TYPE_SUBJECT) {
                model.setAnswer(String.valueOf(detailReq.getAnswerReq().getJudgment()));
                model.setSubjectAnswer(detailReq.getAnswerReq().getReason());
            } else if (detailReq.getQuestionType() == ExamConstant.TYPE_JUDGE) {
                model.setAnswer(String.valueOf(detailReq.getAnswerReq().getJudgment()));
            } else {
                model.setAnswer(detailReq.getAnswerReq().getSelect());
            }
            models.add(model);
        });
        this.saveBatch(models);
    }

    @Override
    public List<OsmExamDetailModel> getMultiples(String examId, String userId) {
        return osmExamDetailDao.getMultiples(examId, userId);
    }

    @Override
    public void judgePointRight(String examId, String userId, int judgePoint) {
        osmExamDetailDao.judgePointRight(examId, userId, judgePoint);
    }

    @Override
    public void singlePointRight(String examId, String userId, int singlePoint) {
        osmExamDetailDao.singlePointRight(examId, userId, singlePoint);
    }

    @Override
    public void multiplePointRight(Integer relId, String userId, int multiplePoint) {
        osmExamDetailDao.multiplePointRight(relId, userId, multiplePoint);
    }

    @Override
    public void modifyObjectScore(String examId, String userId) {
        osmExamInfoDao.modifyObjectScore(examId, userId);
    }
}