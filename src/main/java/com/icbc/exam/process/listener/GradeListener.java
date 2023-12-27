package com.icbc.exam.process.listener;

import com.icbc.exam.common.constant.ExamConstant;
import com.icbc.exam.common.enums.DictEnum;
import com.icbc.exam.common.util.other.DictUtils;
import com.icbc.exam.entity.po.OsmExamDetailModel;
import com.icbc.exam.process.listener.event.GradeEvent;
import com.icbc.exam.service.OsmExamDetailService;
import com.icbc.exam.service.OsmExamRelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author lida
 * @title:
 * @description:
 * @date 2020/7/13
 */
@Component
@Slf4j
public class GradeListener {

    @Autowired
    private OsmExamDetailService examDetailService;
    @Autowired
    private OsmExamRelService examRelService;
    @Autowired
    private DictUtils dictUtils;

    @EventListener
    @Async
    public void acceptMessage(GradeEvent gradeEvent) {
        Long now = System.currentTimeMillis();
        String examId = gradeEvent.getGradeEventModel().getExamId();
        String userId = gradeEvent.getGradeEventModel().getUserId();
        log.info("开始判卷，考试ID=={}，userId=={}", examId, userId);
        //获取各题型分数
        int judgePoint = Integer.parseInt(dictUtils.getValue(DictEnum.QUESTION_TYPE_POINT.getCode(),
                String.valueOf(ExamConstant.TYPE_JUDGE)));
        int singlePoint = Integer.parseInt(dictUtils.getValue(DictEnum.QUESTION_TYPE_POINT.getCode(),
                String.valueOf(ExamConstant.TYPE_SINGLE_CHOICE)));
        int multiplePoint = Integer.parseInt(dictUtils.getValue(DictEnum.QUESTION_TYPE_POINT.getCode(),
                String.valueOf(ExamConstant.TYPE_MULTIPLE_CHOICE)));
        //判断题判分
        examDetailService.judgePointRight(examId, userId, judgePoint);
        //单选题判分
        examDetailService.singlePointRight(examId, userId, singlePoint);
        //多选题判分
        List<OsmExamDetailModel> detailModels = examDetailService.getMultiples(examId, userId);
        //更新总分
        detailModels.forEach(detail -> {
            if (detail.getQuestionType() == ExamConstant.TYPE_MULTIPLE_CHOICE) {
                Integer relId = detail.getRelId();
                String answer = detail.getAnswer();
                String right = examRelService.getRightAnswer(relId);
                if (isRight(answer, right)) {
                    examDetailService.multiplePointRight(relId, userId, multiplePoint);
                }
            }
        });
        //更新客观总分
        examDetailService.modifyObjectScore(examId, userId);
        //更新总条数，总金额
        log.info("结束判卷,考试ID={}, ，userId=={}，时间={}", examId, userId, System.currentTimeMillis()-now);
    }

    /**
     * 多选题判分
     * @param answer 回答
     * @param right 标准答案
     * @return
     */
    private boolean isRight(String answer, String right) {
        if (StringUtils.equals(answer, right)) {
            return true;
        }
        String[] answers = answer.split(",");
        String[] rights = right.split(",");
        if (answers.length == rights.length) {
            for (String a : answers) {
                if (!ArrayUtils.contains(rights, a)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
