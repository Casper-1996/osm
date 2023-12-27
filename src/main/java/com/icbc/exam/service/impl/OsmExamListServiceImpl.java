package com.icbc.exam.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icbc.exam.common.constant.DailyConstant;
import com.icbc.exam.common.constant.ExamConstant;
import com.icbc.exam.common.util.other.RSAUtils;
import com.icbc.exam.dao.OsmExamListDao;
import com.icbc.exam.entity.bo.*;
import com.icbc.exam.entity.po.OsmExamListModel;
import com.icbc.exam.entity.po.OsmExamRelModel;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.vo.*;
import com.icbc.exam.service.OsmExamListService;
import com.icbc.exam.service.OsmExamRelService;
import com.icbc.exam.service.OsmMultipleChoiceService;
import com.icbc.exam.service.OsmQuestionBankRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icbc.exam.common.util.other.DictUtils;
import com.icbc.exam.common.enums.DictEnum;
import com.icbc.exam.entity.pojo.result.ResultUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: liurong
 * @title: OsmExamListServiceImpl
 * @projectName: osm-mgmt-exam
 * @description: 考试试卷管理(OSM_EXAM_LIST)表服务实现类
 * @data: 2021-04-07 11:57:42
 */
@Slf4j
@Service
@Transactional
public class OsmExamListServiceImpl implements OsmExamListService {
    @Value("${prefix.url}")
    private String prefixUrl;
    @Autowired
    private OsmExamListDao osmExamListDao;
    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private OsmQuestionBankRecordService questionBankRecordService;
    @Autowired
    private OsmExamRelService examRelService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OsmMultipleChoiceService choiceService;


      /**
       * 新增考试(组卷)
       * @param entity
       * @return
       */
      @Override
      public ResultData createPaper(ExamCreateReq entity) {
          try {
              //组卷校验
              ResultData resultData = checkCreatePaper(entity);
              if(resultData.getCode() != 200){
                  return resultData;
              }
              //试卷
              List<ExamPreviewResp> examPaperResp = new ArrayList<>();

              Date date = new Date();
              AtomicInteger atomicInteger = new AtomicInteger();
              OsmExamListModel osmExamListModel = new OsmExamListModel();
              BeanUtils.copyProperties(entity, osmExamListModel);
              String examId = System.currentTimeMillis() + "_" + StringUtils.leftPad(atomicInteger.getAndIncrement() + "", 5, "0");
              osmExamListModel.setExamId(examId);
              osmExamListModel.setCreateTime(date);
              osmExamListModel.setUpdateTime(date);
              //默认未分配判卷
              osmExamListModel.setExamState("3");
              osmExamListModel.setExamConfig(entity.getPaperQuesType2());
              osmExamListModel.setModuleTypeScale(entity.getSuccessMsg());
              //落考试表
              osmExamListDao.insert(osmExamListModel);
              List<OsmExamRelModel> osmExamRels = new ArrayList<>();
              final Integer[] quesSeq = {1};
              //排序 单选  多选  判断
              //获取题型
              List<String> questionType = osmExamListDao.findQuesSeq(DictEnum.QUESTION_TYPE.getCode());
              List<PaperQuesType> examQuestionNum = entity.getPaperQuesType();
              Collections.sort(examQuestionNum, new Comparator<PaperQuesType>() {
                  @Override
                  public int compare(PaperQuesType o1, PaperQuesType o2) {
                      int io1 = questionType.indexOf(o1.getQuestionType());
                      int io2 = questionType.indexOf(o2.getQuestionType());
                      return io1-io2;
                  }
              });
              if(!CollectionUtils.isEmpty(examQuestionNum)){
                  for (PaperQuesType quesTypeResp : examQuestionNum) {
                      List<PaperModuleType> moduleTypeNum = quesTypeResp.getModuleTypeNum();
                      if(!CollectionUtils.isEmpty(moduleTypeNum)){
                          for (PaperModuleType queTypeNum : moduleTypeNum) {
                              CreatePaper createPaper = new CreatePaper();
                              createPaper.setModuleType(queTypeNum.getModuleType());
                              createPaper.setQuestionNum(queTypeNum.getQuestionNum());
                              createPaper.setQuestionType(quesTypeResp.getQuestionType());
                              createPaper.setScopeAppliaction(entity.getScopeAppliaction());
                              createPaper.setScopeUser(entity.getScopeUser());
                              //取题
                              List<PaperQuestion> paperQuestion = osmExamListDao.qryModuleQues(createPaper);
                              //选择题
                              if(dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"单选题").equals(quesTypeResp.getQuestionType()) || dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"多选题").equals(quesTypeResp.getQuestionType())){
                                  paperQuestion.stream().forEach(x ->{
                                      OsmExamRelModel examRelModel = new OsmExamRelModel();
                                      //组卷试卷
                                      ExamPreviewResp examPaper = new ExamPreviewResp();
                                      //组卷题目选项
                                      List<Options> options = new ArrayList<>();

                                      examRelModel.setRelId(System.currentTimeMillis() + "_" + StringUtils.leftPad(atomicInteger.getAndIncrement() + "", 5, "0"));
                                      examRelModel.setExamId(examId);
                                      examRelModel.setQuestionId(x.getId());
                                      examRelModel.setQuestionType(Integer.valueOf(quesTypeResp.getQuestionType()));
                                      examRelModel.setQuestionSeq(quesSeq[0]++);
                                      //查找选项
                                      List<QuesOptions> quesOptions = osmExamListDao.findQuesOption(x.getId());
                                      StringBuffer opsbf = new StringBuffer();
                                      StringBuffer ansbf = new StringBuffer();
                                      quesOptions.stream().forEach(q ->{
                                          //组卷题目选项
                                          Options option = new Options();
                                          option.setOption(q.getOptionContent());
                                          option.setOptionId(q.getOptionId());
                                          options.add(option);
                                          opsbf.append(q.getOptionId()+ "|");
                                          if(q.getCorrectAnswer() == 1){
                                              ansbf.append(q.getOptionId()+ "|");
                                          }
                                      });
                                      examRelModel.setAnswerId(ansbf.deleteCharAt(ansbf.length()-1).toString());
                                      examRelModel.setOptionId(opsbf.deleteCharAt(opsbf.length()-1).toString());
                                      osmExamRels.add(examRelModel);

                                      //组卷
                                      examPaper.setQuestionType(quesTypeResp.getQuestionType());
                                      examPaper.setQuestionTypeName(dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), quesTypeResp.getQuestionType()));
                                      examPaper.setFileUrl(StringUtils.isBlank(x.getUploadFileName()) ? "" : prefixUrl + x.getUploadFileName());
                                      examPaper.setQuestionId(x.getId());
                                      examPaper.setQuestion(x.getTitleContent());
                                      //题目答案
                                      examPaper.setCorrectAnswer(ansbf.toString());
                                      examPaper.setOptions(options);
                                      examPaperResp.add(examPaper);
                                  });
                              }
                              //判断题
                              if(dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"判断题").equals(quesTypeResp.getQuestionType())){
                                  paperQuestion.stream().forEach(x ->{
                                      OsmExamRelModel examRelModel = new OsmExamRelModel();
                                      examRelModel.setRelId(System.currentTimeMillis() + "_" + StringUtils.leftPad(atomicInteger.getAndIncrement() + "", 5, "0"));
                                      examRelModel.setExamId(examId);
                                      examRelModel.setQuestionId(x.getId());
                                      examRelModel.setQuestionType(Integer.valueOf(quesTypeResp.getQuestionType()));
                                      examRelModel.setQuestionSeq(quesSeq[0]++);
                                      examRelModel.setAnswerId(x.getCorrectAnswer().toString());
                                      osmExamRels.add(examRelModel);

                                      //组卷试卷
                                      ExamPreviewResp examPaper = new ExamPreviewResp();
                                      examPaper.setQuestionType(quesTypeResp.getQuestionType());
                                      examPaper.setQuestionTypeName(dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), quesTypeResp.getQuestionType()));
                                      examPaper.setFileUrl(StringUtils.isBlank(x.getUploadFileName()) ? "" : prefixUrl + x.getUploadFileName());
                                      examPaper.setQuestionId(x.getId());
                                      //题目答案
                                      examPaper.setCorrectAnswer(x.getCorrectAnswer().toString());
                                      examPaper.setQuestion(x.getTitleContent());
                                      examPaperResp.add(examPaper);
                                  });
                              }
                          }
                      }
                  }
              }
              //主观题
              //获取主观题数
              String subNum = dictUtils.getValue(DictEnum.QUESTION_TYPE_NUM.getCode(), "主观题");
              //随即抽取主观题
              List<PaperQuestion> paperQuestion =  osmExamListDao.findSubQues(subNum,entity.getScopeAppliaction(),entity.getScopeUser());
              paperQuestion.stream().forEach(x ->{
                  OsmExamRelModel examRelModel = new OsmExamRelModel();
                  examRelModel.setRelId(System.currentTimeMillis() + "_" + StringUtils.leftPad(atomicInteger.getAndIncrement() + "", 5, "0"));
                  examRelModel.setExamId(examId);
                  examRelModel.setQuestionId(x.getId());
                  examRelModel.setQuestionType(0);
                  examRelModel.setQuestionSeq(quesSeq[0]++);
                  osmExamRels.add(examRelModel);

                  //组卷试卷
                  ExamPreviewResp examPaper = new ExamPreviewResp();
                  examPaper.setQuestionType("0");
                  examPaper.setQuestionTypeName(dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), "0"));
                  examPaper.setFileUrl(StringUtils.isBlank(x.getUploadFileName()) ? "" : prefixUrl + x.getUploadFileName());
                  examPaper.setQuestionId(x.getId());
                  examPaper.setQuestion(x.getTitleContent());
                  //题目答案
                  examPaper.setCorrectAnswer(null != x.getCorrectAnswer() ? x.getCorrectAnswer().toString() : "");
                  //参考答案
                  examPaper.setReferenceAnswer(null != x.getReferenceAnswer() ? x.getReferenceAnswer() : "");
                  examPaperResp.add(examPaper);
              });
              //入试卷题目表
              examRelService.saveBatch(osmExamRels);
              //存放redis
              String key = "paperCreate:";
              redisTemplate.opsForValue().set(key + examId, JSON.toJSONString(examPaperResp));
          } catch (BeansException e) {
              log.error("试卷生成失败-->{}",e.getMessage(),e);
              return ResultUtil.error("试卷生成失败");
          }
          return ResultUtil.success("试卷生成成功!");
      }

    /*@Override
    public ResultData qryModuleQuestionNum(ExamQuestionNumReq entity) {
        //各模块数量
        List<ExamQuestionNumResp> examQuestionNumList = new ArrayList<>();
        //获取题型
        Map<String, String> questionTypeEntry = dictUtils.getEntry(DictEnum.QUESTION_TYPE.getCode());
        //获取模块类型
        Map<String, String> moduleTypeEntry = dictUtils.getEntry(DictEnum.MODULE_TYPE.getCode());
        for (String moduleKey : moduleTypeEntry.keySet()) {
            //各模块题型数量
            List<QuestionTypeNum> questionList = new ArrayList<>();
            ExamQuestionNumResp examQuestionNumResp = new ExamQuestionNumResp();
            BigDecimal quesTotal = BigDecimal.ZERO;
            for (String questionKey : questionTypeEntry.keySet()) {
                QuestionTypeNum queTypeNum = new QuestionTypeNum();
                if("0".equals(questionKey)){
                    //主观题
                    continue;
                }
                ModuleScale moduleScale = new ModuleScale();
                BeanUtils.copyProperties(entity, moduleScale);
                moduleScale.setModuleType(moduleKey);
                moduleScale.setQuestionType(questionKey);
                QuestionTypeNum questionTypeNum =  questionBankRecordService.findModuleQuesNum(moduleScale);
                if(null != questionTypeNum){
                    BeanUtils.copyProperties(questionTypeNum, queTypeNum);
                }else{
                    queTypeNum.setQuestionType(questionKey);
                    queTypeNum.setQuestionNum("0");
                }
                quesTotal = quesTotal.add(new BigDecimal(queTypeNum.getQuestionNum()));
                queTypeNum.setQuestionName(dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(),questionKey));
                questionList.add(queTypeNum);
            }
            examQuestionNumResp.setQuesTotal(quesTotal.toString());
            examQuestionNumResp.setModuleName(dictUtils.getValue(DictEnum.MODULE_TYPE.getCode(),moduleKey));
            examQuestionNumResp.setModuleType(moduleKey);
            examQuestionNumResp.setQuestionTypeNum(questionList);
            examQuestionNumList.add(examQuestionNumResp);
        }
        return ResultUtil.success(examQuestionNumList);
    }*/

    @Override
    public ResultData qryModuleQuestionNum(ExamQuestionNumReq entity) {
        //各模块数量
        //获取题型
        Map<String, String> questionTypeEntry = dictUtils.getEntry(DictEnum.QUESTION_TYPE.getCode());
        //获取模块类型
        Map<String, String> moduleTypeEntry = dictUtils.getEntry(DictEnum.MODULE_TYPE.getCode());
//        xin
        ArrayList numList = new ArrayList<>();
//            循环题目类型  单选多选判断

        for (String questionKey : questionTypeEntry.keySet()) {
            int AllNum = 0;
            HashMap<String, String> map = new HashMap<>();
            map.put("name", questionTypeEntry.get(questionKey));
            System.out.println(questionTypeEntry.get(questionKey));
            System.out.println(questionKey);
            System.out.println("------------------------------");
            //对公 对私
            for (String moduleKey : moduleTypeEntry.keySet()) {
                System.out.println(questionTypeEntry.get(questionKey));
                System.out.println(questionKey);
                System.out.println("------------------------------");


                ModuleScale moduleScale = new ModuleScale();
                moduleScale.setModuleType(moduleKey);
                moduleScale.setQuestionType(questionKey);
                moduleScale.setScopeAppliaction(entity.getScopeAppliaction());
                moduleScale.setScopeUser(entity.getScopeUser());
                QuestionTypeNum questionTypeNum = questionBankRecordService.findModuleQuesNum(moduleScale);
                if (null != questionTypeNum) {
                    map.put(moduleTypeEntry.get(moduleKey), questionTypeNum.getQuestionNum());
                    AllNum = AllNum + Integer.parseInt(questionTypeNum.getQuestionNum());
                } else {
                    map.put(moduleTypeEntry.get(moduleKey), "0");
                }
                map.put(moduleTypeEntry.get(moduleKey)+"输入","0");
            }
            map.put("总数", AllNum + "");
            map.put("总数输入", AllNum + "0");
            numList.add(map);
        }
        return ResultUtil.success(numList);
    }
    /**
     * 开始考试
     * @param entity
     */
    @Override
    public ResultData startExam(StartExamReq entity) {
        //试卷
        List<ExamPaperResp> examPaperResp = new ArrayList<>();
        if (StringUtils.isBlank(entity.getExamId())) {
            return ResultUtil.error("考试ID为空");
        }
        //判断是否在考试时间内
        int examNum = osmExamListDao.findExamDate(entity.getExamId(),new Date());
        if(examNum == 0){
            return ResultUtil.error("当前不在考试时间段内!");
        }
        if (!CollectionUtils.isEmpty(redisTemplate.keys("paperCreate:" + entity.getExamId()))) {
            //取redis数据
            examPaperResp = JSONObject.parseArray(redisTemplate.opsForValue().get("paperCreate:" + entity.getExamId()), ExamPaperResp.class);
        }else{
            //查询保存的试卷
            List<ExamPreviewResp> examQues = findSaveExam(entity.getExamId());
            examPaperResp = JSONObject.parseArray(JSON.toJSONString(examQues), ExamPaperResp.class);
            String key = "paperCreate:";
            redisTemplate.opsForValue().set(key + entity.getExamId(), JSON.toJSONString(examQues));
        }
        return ResultUtil.success(examPaperResp);
    }

    @Override
    public ResultData decryptExam(List<ExamPaperResp> examPaper) {
        try {
            for (ExamPaperResp examPaperResp : examPaper) {
                examPaperResp.setQuestion(StringUtils.isBlank(examPaperResp.getQuestion()) ? "" : RSAUtils.decrypt2(examPaperResp.getQuestion(), DailyConstant.privateKey));
                List<Options> options = examPaperResp.getOptions();
                if(!CollectionUtils.isEmpty(options)){
                    for (Options option : options) {
                        option.setOption(StringUtils.isBlank(option.getOption()) ? "" : RSAUtils.decrypt2(option.getOption(), DailyConstant.privateKey));
                    }
                }
            }
        } catch (Exception e) {
            log.error("试卷解密失败-->{}",e.getMessage(),e);
            return ResultUtil.error("试卷解密失败!");
        }
        return ResultUtil.success(examPaper);
    }

    /**
     * 查询考试管理列表
     * @param entity
     * @return
     */
    @Override
    public ResultData qryExamList(ExamManageListReq entity) {
        if (entity == null) {
            entity = new ExamManageListReq();
        }
        entity.setNowDate(new Date());
        Integer pageNum = entity.getPageNum() == null ? 1 : entity.getPageNum();
        Integer pageSize = entity.getPageSize() == null ? 10 : entity.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ExamManageListResp> examManageList = osmExamListDao.qryExamList(entity);
        examManageList.stream().forEach(examManage ->{
            examManage.setScopeUserTran(dictUtils.getValue(DictEnum.SCOPE_USER.getCode(),examManage.getScopeUser().toString()));
            examManage.setScopeAppliactionTran(dictUtils.getValue(DictEnum.SCOPE_APPLIACTION.getCode(),examManage.getScopeAppliaction().toString()));
            examManage.setExamStatueTran(dictUtils.getValue(DictEnum.EXAM_STATE.getCode(),examManage.getExamStatue()));
        });
        return ResultUtil.success(new PageInfo<ExamManageListResp>(examManageList));
    }

    @Override
    public ResultData deleteExam(String examId) {
        int examNum = osmExamListDao.findExamState(examId,new Date());
        if(examNum != ExamConstant.EXAM_STATE_1){
            ResultUtil.error("考试已开始或已结束不允许删除!");
        }
        //删除试卷
        osmExamListDao.deleteById(examId);
        //删除试卷题目
        osmExamListDao.deleteExamQuesTion(examId);
        //删除redis
        String key = "paperCreate:";
        redisTemplate.delete(key + examId);
        return ResultUtil.success();
    }

    @Override
    public ResultData previewExam(String examId) {
        ExamPaperPreviewResp examPaperPreviewResp = new ExamPaperPreviewResp();
        try {
            //查询试卷配置
            ExamPaperModifyResp examPaperModifyResp = getExamPaperConfig(examId);
            BeanUtils.copyProperties(examPaperModifyResp, examPaperPreviewResp);
            //试卷
            List<ExamPreviewResp> examPreviewResps = new ArrayList<>();
            if (!CollectionUtils.isEmpty(redisTemplate.keys("paperCreate:" + examId))) {
                //取redis数据
                examPreviewResps = JSONObject.parseArray(redisTemplate.opsForValue().get("paperCreate:" + examId), ExamPreviewResp.class);
            }else{
                //查询保存的试卷
                examPreviewResps = findSaveExam(examId);
                String key = "paperCreate:";
                redisTemplate.opsForValue().set(key + examId, JSON.toJSONString(examPreviewResps));
            }
            //解密预览试卷
            decryptPreviewExam(examPreviewResps);
            examPaperPreviewResp.setExamPaperPreview(examPreviewResps);
        } catch (BeansException e) {
            log.error("试卷预览失败-->{}",e.getMessage(),e);
            return ResultUtil.error("试卷预览失败");
        }
        return ResultUtil.success(examPaperPreviewResp);
    }

    private List<ExamPreviewResp> findSaveExam(String examId) {
        List<ExamPreviewResp> examPapers = new ArrayList<>();
        //查询整张试卷所有题目
        List<OsmExamRelModel>  examPaperAllQues =  osmExamListDao.findExamPaperAllQues(examId);
        if(!CollectionUtils.isEmpty(examPaperAllQues)){
            for (OsmExamRelModel examPaperAllQue : examPaperAllQues) {
                //组卷试卷
                ExamPreviewResp examPaper = new ExamPreviewResp();
                //选择题
                if(dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"单选题").equals(examPaperAllQue.getQuestionType().toString())  || dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"多选题").equals(examPaperAllQue.getQuestionType().toString())){

                    //组卷题目选项
                    List<Options> options = new ArrayList<>();
                    OsmQuestionBankRecordModel quesModel = questionBankRecordService.findQuesById(examPaperAllQue.getQuestionId());
                    //组卷
                    examPaper.setQuestionType(examPaperAllQue.getQuestionType().toString());
                    examPaper.setQuestionTypeName(dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), examPaperAllQue.getQuestionType().toString()));
                    examPaper.setFileUrl(StringUtils.isBlank(quesModel.getUploadFileName()) ? "" : prefixUrl + quesModel.getUploadFileName());
                    examPaper.setQuestionId(quesModel.getId());
                    examPaper.setQuestion(quesModel.getTitleContent());
                    //题目答案
                    examPaper.setCorrectAnswer(examPaperAllQue.getAnswerId());
                    if(!StringUtils.isEmpty(examPaperAllQue.getOptionId())){
                        String[] optionIds = examPaperAllQue.getOptionId().split("\\|");
                        for (String optionId : optionIds) {
                            //组卷题目选项
                            Options option = new Options();
                            OsmMultipleChoiceModel choiceModel = choiceService.findOptionById(optionId);
                            option.setOption(choiceModel.getOptions());
                            option.setOptionId(optionId);
                            options.add(option);
                        }
                    }
                    examPaper.setOptions(options);
                    examPapers.add(examPaper);
                }
                //判断题 主观题
                if(dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"判断题").equals(examPaperAllQue.getQuestionType().toString()) || dictUtils.getKey(DictEnum.QUESTION_TYPE.getCode(),"主观题").equals(examPaperAllQue.getQuestionType().toString())){
                    OsmQuestionBankRecordModel quesModel = questionBankRecordService.findQuesById(examPaperAllQue.getQuestionId());
                    //组卷试卷
                    examPaper.setQuestionType(examPaperAllQue.getQuestionType().toString());
                    examPaper.setQuestionTypeName(dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), examPaperAllQue.getQuestionType().toString()));
                    examPaper.setFileUrl(StringUtils.isBlank(quesModel.getUploadFileName()) ? "" : prefixUrl + quesModel.getUploadFileName());
                    examPaper.setQuestionId(quesModel.getId());
                    //题目答案
                    examPaper.setCorrectAnswer(null != quesModel.getCorrectAnswer() ? quesModel.getCorrectAnswer().toString() : "");
                    examPaper.setReferenceAnswer(null != quesModel.getReferenceAnswer() ? quesModel.getReferenceAnswer() : "");
                    examPaper.setQuestion(quesModel.getTitleContent());
                    examPapers.add(examPaper);
                }
            }
        }
        return examPapers;
    }

    /**
     * 查询试卷配置
     * @param examId
     * @return
     */
    private ExamPaperModifyResp getExamPaperConfig(String examId) {
        //查询试卷
        ExamPaperModifyResp examPaperModifyResp = new ExamPaperModifyResp();
        ExamPreviewInfo examPreviewInfo = osmExamListDao.selectPreviewExam(examId,new Date());
        if(null != examPreviewInfo){
            examPreviewInfo.setScopeUserTran(dictUtils.getValue(DictEnum.SCOPE_USER.getCode(),examPreviewInfo.getScopeUser().toString()));
            examPreviewInfo.setScopeAppliactionTran(dictUtils.getValue(DictEnum.SCOPE_APPLIACTION.getCode(),examPreviewInfo.getScopeAppliaction().toString()));
            examPreviewInfo.setExamStatueTran(dictUtils.getValue(DictEnum.EXAM_STATE.getCode(),examPreviewInfo.getExamStatue()));
        }
        BeanUtils.copyProperties(examPreviewInfo, examPaperModifyResp);
        examPaperModifyResp.setPaperQuesPreviewType(examPreviewInfo.getExamConfig());
        return examPaperModifyResp;
    }

    /**
     * 考试管理试卷修改 (回显)
     * @param examId
     * @return
     */
    @Override
    public ResultData modifyExamEcho(String examId) {
        ExamPaperModifyResp examPaperModifyResp = getExamPaperConfig(examId);
        return ResultUtil.success(examPaperModifyResp);
    }

    @Override
    public int findExamState(String eId) {
        return osmExamListDao.findExamState(eId, new Date());
    }

    @Override
    public ResultData qryMyExamList(MyExamManageListReq entity) {
        if (entity == null) {
            entity = new MyExamManageListReq();
        }
        entity.setNowDate(new Date());
        Integer pageNum = entity.getPageNum() == null ? 1 : entity.getPageNum();
        Integer pageSize = entity.getPageSize() == null ? 10 : entity.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<MyExamManageListResp> examManageList = osmExamListDao.qryMyExamList(entity);
        examManageList.stream().forEach(examManage ->{
            examManage.setExamStateTran(dictUtils.getValue(DictEnum.MYEXAM_STATE.getCode(),examManage.getExamState()));
        });
        return ResultUtil.success( new PageInfo<MyExamManageListResp>(examManageList));
    }

    @Override
    public List<JudAdminResp> findjudAdmin(JudAdminReq entity) {
        return osmExamListDao.findjudAdmin(entity);
    }

    private void decryptPreviewExam( List<ExamPreviewResp> examPreviewPaper) {
        try {
            for (ExamPreviewResp examPaperResp : examPreviewPaper) {
                examPaperResp.setQuestion(StringUtils.isBlank(examPaperResp.getQuestion()) ? "" : RSAUtils.decrypt2(examPaperResp.getQuestion(), DailyConstant.privateKey));
                examPaperResp.setReferenceAnswer(StringUtils.isBlank(examPaperResp.getReferenceAnswer()) ? "" : RSAUtils.decrypt2(examPaperResp.getReferenceAnswer(), DailyConstant.privateKey));
                List<Options> options = examPaperResp.getOptions();
                if(!CollectionUtils.isEmpty(options)){
                    for (Options option : options) {
                        option.setOption(StringUtils.isBlank(option.getOption()) ? "" : RSAUtils.decrypt2(option.getOption(), DailyConstant.privateKey));
                    }
                }
            }
        } catch (Exception e) {
            log.error("试卷解密失败-->{}",e.getMessage(),e);
        }
    }

    /**
       * 组卷校验
       * @param entity
       */
      private ResultData checkCreatePaper(ExamCreateReq entity) {
          //获取题型
          Map<String, String> questionTypeEntry = dictUtils.getEntry(DictEnum.QUESTION_TYPE.getCode());
          //校验题型题量是否足够
          ExamQuesNumCheck examQuesNumCheck = new ExamQuesNumCheck();
          BeanUtils.copyProperties(entity, examQuesNumCheck);
          for (String key : questionTypeEntry.keySet()) {
              examQuesNumCheck.setQuestionType(key);
              String value = questionTypeEntry.get(key);
              //获取对应题型数量
              String questionTypeNum = dictUtils.getValue(DictEnum.QUESTION_TYPE_NUM.getCode(), value);
              //题型实际数量
              int actualNum = questionBankRecordService.findQuestionTypeNum(examQuesNumCheck);
              if(Integer.valueOf(questionTypeNum) > actualNum){
                  return ResultUtil.error("创建失败,请扩充"+value+"题库!");
              }
          }
          return  ResultUtil.success();
      }
  }