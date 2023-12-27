package com.icbc.exam.resource;

import com.icbc.exam.common.constant.ExamConstant;
import com.icbc.exam.common.enums.DictEnum;
import com.icbc.exam.common.util.other.DictUtils;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.vo.*;
import com.icbc.exam.service.OsmExamListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icbc.exam.common.util.other.UserUtils;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.pojo.result.ResultUtil;
import com.icbc.exam.entity.pojo.user.UserDetailInfo;
import com.icbc.exam.process.listener.entity.GradeEventModel;
import com.icbc.exam.process.listener.event.GradeEvent;
import com.icbc.exam.service.OsmExamDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liurong
 * @title: ExamManageResource
 * @projectName osm-mgmt-exam
 * @description: 考试管理
 * @date 2021/4/7 11:42
 */
@RestController
@RequestMapping("/exam")
public class ExamManageResource implements ApplicationEventPublisherAware {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private OsmExamDetailService examDetailService;
    @Autowired
    private OsmExamListService examListService;

    /**
     　* @description: 新增考试(组卷)
     　* @author liurong
     　* @date 2021/4/7 14:42
     　*/
    @PostMapping(path = "/create/paper")
    public ResultData createPaper(@RequestBody ExamCreateReq entity){
        ResultData result = examListService.createPaper(entity);
        return result;
    }

    /**
     　* @description: 开始考试
     　* @author liurong
     　* @date 2021/4/10 15:42
     　*/
    @PostMapping(path = "/start")
    public ResultData startExam(@RequestBody StartExamReq entity){
        ResultData result = examListService.startExam(entity);
        if(result.getCode() == 200){
            //解密
            return examListService.decryptExam((List<ExamPaperResp>)result.getData());
        }
        return result;
    }

    /**
     　* @description: 查询题库各模块题型数量
     　* @author liurong
     　* @date 2021/4/8 15:55
     　*/
    @PostMapping(path = "/find/module/question")
    public ResultData qryModuleQuestionNum(@RequestBody ExamQuestionNumReq entity){
        ResultData result = examListService.qryModuleQuestionNum(entity);
        return result;
    }

    /**
     　* @description: 查询考试管理列表
     　* @author liurong
     　* @date 2021/4/12 10:55
     　*/
    @PostMapping(path = "/find/exam")
    public ResultData qryExamList(@RequestBody ExamManageListReq entity){
        ResultData result = examListService.qryExamList(entity);
        return result;
    }

    /**
     　* @description: 删除考试管理列表
     　* @author liurong
     　* @date 2021/4/12 14:55
     　*/
    @GetMapping(path = "/delete/exam/{examId}")
    public ResultData deleteExam(@PathVariable("examId") String examId){
        ResultData result = examListService.deleteExam(examId);
        return result;
    }

    /**
     　* @description: 查询考试管理考试详情 (预览)
     　* @author liurong
     　* @date 2021/4/12 14:55
     　*/
    @GetMapping(path = "/preview/exam/{examId}")
    public ResultData previewExam(@PathVariable("examId") String examId){
        ResultData result = examListService.previewExam(examId);
        return result;
    }


    /**
     　* @description: 考试管理试卷修改 (回显)
     　* @author liurong
     　* @date 2021/4/12 16:55
     　*/
    @GetMapping(path = "/modify/exam/{examId}")
    public ResultData modifyExamEcho(@PathVariable("examId") String examId){
        ResultData result = examListService.modifyExamEcho(examId);
        return result;
    }


    /**
     　* @description: 考试管理试卷修改
     　* @author liurong
     　* @date 2021/4/7 16:42
     　*/
    @PostMapping(path = "/modify/exam")
    public ResultData modifyExam(@RequestBody ExamCreateReq entity){
        int examState = examListService.findExamState(entity.getExId());
        if(examState != ExamConstant.EXAM_STATE_1){
            return ResultUtil.error("考试已开始或已结束不允许修改!");
        }
        ResultData result = examListService.createPaper(entity);
        if(result.getCode() == 200){
            //删除旧试卷
            examListService.deleteExam(entity.getExId());
        }
        return result.getCode() == 200 ? ResultUtil.success("试卷修改成功") : result;
    }

    /**
     　* @description: 我的考试列表
     　* @author liurong
     　* @date 2021/4/13 14:55
     　*/
    @PostMapping(path = "/find/myexam")
    public ResultData qryMyExamList(@RequestBody MyExamManageListReq entity,@RequestHeader("Authorization")String token){
        UserDetailInfo userInfo = userUtils.getUserInfoByToken(token);
        if (userInfo == null) {
            return ResultUtil.error("获取用户信息错误");
        }
        String userId = userInfo.getUser().getUsername();
        if (StringUtils.isBlank(userId)) {
            return ResultUtil.error("获取用户ID错误");
        }
        entity.setUserId(userId);
        ResultData result = examListService.qryMyExamList(entity);
        return result;
    }

    /**
     * 　* @description: 判卷人员分配查询
     */
    @PostMapping(path = "/find/judging")
    public ResultData findjudAdmin(@RequestBody JudAdminReq entity) {
        int examState = examListService.findExamState(entity.getExamId());
        if(examState == ExamConstant.EXAM_STATE_3){
            return ResultUtil.error("考试"+ dictUtils.getValue(DictEnum.EXAM_STATE.getCode(),String.valueOf(examState) ) +"不允许判卷分配!");
        }
       List<JudAdminResp> judAdminResp =  examListService.findjudAdmin(entity);
        return ResultUtil.success(judAdminResp);
    }







    @PostMapping("/submit")
    public ResultData submit(@RequestBody ExamSubmitReq req,
                               @RequestHeader("Authorization")String token){
        UserDetailInfo userInfo = userUtils.getUserInfoByToken(token);
        if (userInfo == null) {
            return ResultUtil.error("获取用户信息错误");
        }
        String userId = userInfo.getUser().getUsername();
        if (StringUtils.isBlank(userId)) {
            return ResultUtil.error("获取用户ID错误");
        }
        if (req == null || CollectionUtils.isEmpty(req.getDetailReqList())) {
            return ResultUtil.error("答题数据有误");
        }
        String examId = req.getDetailReqList().get(0).getExamId();
        if (StringUtils.isBlank(examId)) {
            return ResultUtil.error("考试ID为空");
        }
        examDetailService.submit(req, userId);
        //触发客观题判卷
        GradeEventModel eventModel = new GradeEventModel();
        eventModel.setExamId(examId);
        eventModel.setUserId(userId);
        applicationEventPublisher.publishEvent(new GradeEvent(eventModel));
        return ResultUtil.success();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
