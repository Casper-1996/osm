package com.icbc.exam.resource;


import com.icbc.exam.common.constant.DailyConstant;
import com.icbc.exam.common.enums.DictEnum;
import com.icbc.exam.common.util.excel.ExcelUtils;
import com.icbc.exam.common.util.other.DictUtils;
import com.icbc.exam.common.util.other.MultipartFileToFile;
import com.icbc.exam.common.util.other.RSAUtils;
import com.icbc.exam.common.util.other.UnZipUtils;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import com.icbc.exam.entity.po.OsmScopeUserModel;
import com.icbc.exam.entity.pojo.excel.JudgmentExcelModel;
import com.icbc.exam.entity.pojo.excel.MultipleChoiceExcelModel;
import com.icbc.exam.entity.pojo.excel.RowNumObject;
import com.icbc.exam.entity.pojo.excel.SubjectiveExcelModel;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.pojo.result.ResultUtil;
import com.icbc.exam.service.OsmMultipleChoiceService;
import com.icbc.exam.service.OsmQuestionBankRecordService;
import com.icbc.exam.service.OsmScopeUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.BatchExecutorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.sql.BatchUpdateException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/upload")
@Slf4j
public class QuestionUploadResource {

    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private OsmQuestionBankRecordService oqbrService;
    @Autowired
    private OsmMultipleChoiceService omcService;
    @Autowired
    private OsmScopeUserService osuService;
    @Value("${topic.fileUrl}")
    private String fileUrl;

    /**
     * 选择题导入 2单选 3多选
     *
     * @param file
     * @return
     */
    @PostMapping(path = "/multipleChoice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional(rollbackFor = Exception.class)
    public ResultData importMultipleChoice(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "annex", required = false) MultipartFile annex) {
        if (file.isEmpty()) {
            return ResultUtil.error("请上传excel");
        }
        String url = "";
        if (annex != null) {
            url = unZip(annex);
            if (StringUtils.isEmpty(url)) {
                return ResultUtil.error("文件解压失败请重新上传");
            }
        }

        List<RowNumObject<MultipleChoiceExcelModel>> multipleChoiceExcelModels = null;
        try (InputStream inputStream = file.getInputStream()) {
            multipleChoiceExcelModels = ExcelUtils.validateHeadReadExcel(inputStream, MultipleChoiceExcelModel.class);
        } catch (Exception e) {
            log.error("文件解析错误: ", e.getMessage());
            return ResultUtil.error("文件解析错误:" + e.getMessage());
        }
        if (null == multipleChoiceExcelModels || multipleChoiceExcelModels.size() == 0) {
            return ResultUtil.error("文件内容解析失败或文件内容为空");
        }

        boolean flag = false;
        try {
            //数据处理并入库
            flag = dataProcessing(multipleChoiceExcelModels, url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.error("本次上传题库失败!");
    }

    /**
     * 主观题导入 0
     *
     * @param file
     * @return
     */
    @PostMapping(path = "/subjective", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional(rollbackFor = Exception.class)
    public ResultData importSubjective(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "annex", required = false) MultipartFile annex) {
        if (file.isEmpty()) {
            return ResultUtil.error("请上传excel");
        }
        String url = "";
        if (annex != null) {
            url = unZip(annex);
            if (StringUtils.isEmpty(url)) {
                return ResultUtil.error("文件解压失败请重新上传");
            }
        }


        List<RowNumObject<SubjectiveExcelModel>> excelModelList = null;

        try (InputStream inputStream = file.getInputStream()) {
            excelModelList = ExcelUtils.validateHeadReadExcel(inputStream, SubjectiveExcelModel.class);
        } catch (Exception e) {
            log.error("文件解析错误: ", e.getMessage());
            return ResultUtil.error("文件解析错误:" + e.getMessage());
        }
        if (null == excelModelList || excelModelList.size() == 0) {
            return ResultUtil.error("文件内容解析失败或文件内容为空");
        }

        boolean flag = false;
        try {
            flag = dataProcessingSub(excelModelList, url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.error("本次上传题库失败!");

    }

    /**
     * 判断题导入 1
     *
     * @param file
     * @return
     */
    @PostMapping(path = "/judgment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional(rollbackFor = Exception.class)
    public ResultData importJudgment(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "annex", required = false) MultipartFile annex) {

        if (file.isEmpty()) {
            return ResultUtil.error("请上传excel");
        }
        String url = "";
        if (annex != null) {
            url = unZip(annex);
            if (StringUtils.isEmpty(url)) {
                return ResultUtil.error("文件解压失败请重新上传");
            }
        }
        List<RowNumObject<JudgmentExcelModel>> excelModelList = null;

        try (InputStream inputStream = file.getInputStream()) {
            excelModelList = ExcelUtils.validateHeadReadExcel(inputStream, JudgmentExcelModel.class);
        } catch (Exception e) {
            log.error("文件解析错误: ", e.getMessage());
            return ResultUtil.error("文件解析错误:" + e.getMessage());
        }
        if (null == excelModelList || excelModelList.size() == 0) {
            return ResultUtil.error("文件内容解析失败或文件内容为空");
        }

        boolean flag = false;
        try {
            flag = dataProcessingJud(excelModelList, url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.error("本次上传题库失败!");

    }

    /**
     * 解压上传文件
     */
    private String unZip(MultipartFile multipartFile) {
        String uid = "";
        try {
            File file1 = MultipartFileToFile.multipartFileToFile(multipartFile);
            uid = UnZipUtils.unZipFiles(file1, fileUrl);
            MultipartFileToFile.delteTempFile(file1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return uid;
    }


    /**
     * @description: 判断题数据处理 题目内容加密
     * @author: cxk
     * @date: 2021/4/1 17:34
     **/
    @Transactional(rollbackFor = Exception.class)
    boolean dataProcessingJud(List<RowNumObject<JudgmentExcelModel>> models, String url) throws Exception {

        List<JudgmentExcelModel> collect = models.stream().sorted(Comparator.comparing(RowNumObject::getRowNum))
                .map(RowNumObject::getObject).collect(Collectors.toList());

        //题库记录-汇总
        List<OsmQuestionBankRecordModel> oqbModelList = new ArrayList<>();
        //使用范围记录
        List<OsmScopeUserModel> osuModelList = new ArrayList<>();
        for (JudgmentExcelModel jeModel : collect) {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            //题库记录
            OsmQuestionBankRecordModel oqbModel = new OsmQuestionBankRecordModel();
            //使用范围记录
            scopeUserAnnal(osuModelList, id, jeModel.getScopeUser());
            //id
            oqbModel.setId(id);
            //题目名称
            oqbModel.setTitleName(jeModel.getTitleName());
            //题目内容
            oqbModel.setTitleContent(RSAUtils.encrypt2(jeModel.getTitleContent(), DailyConstant.publicKey));
            //题目答案
            oqbModel.setCorrectAnswer(Integer.valueOf(
                    dictUtils.getKey(DictEnum.ANSWER.getCode(), jeModel.getAnswer())));
            //模块类型
            oqbModel.setModuleType(Integer.valueOf(
                    dictUtils.getKey(DictEnum.MODULE_TYPE.getCode(), jeModel.getModuleType())));
            //试题标志
            oqbModel.setQuestionMark(Integer.valueOf(
                    dictUtils.getKey(DictEnum.WHETHER.getCode(), jeModel.getQuestionMark())));
            //上传附件名
            oqbModel.setUploadFileName(
                    StringUtils.isNotEmpty(jeModel.getUploadFileName()) ? url + jeModel.getUploadFileName() : null);
            //适用层级
            oqbModel.setScopeAppliaction(Integer.valueOf(
                    dictUtils.getKey(DictEnum.SCOPE_APPLIACTION.getCode(), jeModel.getScopeAppliaction())));

            //题目类型-判断题1
            oqbModel.setQuestionType(1);
            oqbModel.setInsertTime(new Date());
            oqbModel.setUpdateTime(new Date());

            oqbModelList.add(oqbModel);
        }
        boolean oqb = false;
        boolean osu = false;
        try {
            oqb = oqbrService.saveBatch(oqbModelList);
            osu = osuService.saveBatch(osuModelList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //不需要手工抛运行时异常
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }
        return oqb && osu;
    }

    private String scopeUser(String scopeUser) {
        String scopeUserStr = null;
        if (StringUtils.isEmpty(scopeUser)) {
            return scopeUserStr;
        }
        //使用范围
        String[] split = scopeUser.split("\\|");
        scopeUserStr = dictUtils.getKey(DictEnum.SCOPE_USER.getCode(), split[0]);
        for (int i = 1; i < split.length; i++) {
            scopeUserStr = scopeUserStr + "|" + dictUtils.getKey(DictEnum.SCOPE_USER.getCode(), split[i]);
        }
        return scopeUserStr;
    }


    /**
     * @description: 选择题数据处理 题目内容 选项加密
     * @author: cxk
     * @date: 2021/4/1 17:34
     **/
    @Transactional(rollbackFor = Exception.class)
    boolean dataProcessing(List<RowNumObject<MultipleChoiceExcelModel>> models, String url) throws Exception {

        List<MultipleChoiceExcelModel> collect = models.stream().sorted(Comparator.comparing(RowNumObject::getRowNum))
                .map(RowNumObject::getObject).collect(Collectors.toList());
        //题库录入记录
        List<OsmQuestionBankRecordModel> qbrModelList = new ArrayList<>();
        //选择题录入
        List<OsmMultipleChoiceModel> mcModelList = new ArrayList<>();
        //使用范围记录
        List<OsmScopeUserModel> osuModelList = new ArrayList<>();
        String id = "";

        log.info("collect.size():{}", collect.size());

        for (MultipleChoiceExcelModel model : collect) {
            id = UUID.randomUUID().toString().replaceAll("-", "");

            //题库
            OsmQuestionBankRecordModel qbrModel = new OsmQuestionBankRecordModel();
            //id
            qbrModel.setId(id);
            //题目名称
            qbrModel.setTitleName(model.getTitleName());
            //题目内容
            qbrModel.setTitleContent(RSAUtils.encrypt2(model.getTitleContent(), DailyConstant.publicKey));
            //模块类型
            qbrModel.setModuleType(Integer.valueOf(
                    dictUtils.getKey(DictEnum.MODULE_TYPE.getCode(), model.getModuleType())));
            //试题标志
            qbrModel.setQuestionMark(Integer.valueOf(
                    dictUtils.getKey(DictEnum.WHETHER.getCode(), model.getQuestionMark())));
            //上传附件名
            qbrModel.setUploadFileName(
                    StringUtils.isNotEmpty(model.getUploadFileName()) ? url + model.getUploadFileName() : null);
            //适用层级
            qbrModel.setScopeAppliaction(Integer.valueOf(
                    dictUtils.getKey(DictEnum.SCOPE_APPLIACTION.getCode(), model.getScopeAppliaction())));
            //题目类型(0：主观题，1：判断题，2：单选题，3：多选题）
            if (model.getCorrectAnswer().length() > 1) {
                qbrModel.setQuestionType(3);
            } else {
                qbrModel.setQuestionType(2);
            }
            //插入时间
            qbrModel.setInsertTime(new Date());
            //更新时间
            qbrModel.setUpdateTime(new Date());


            String[] split = model.getCorrectAnswer().split("");
            //选择题A
            OsmMultipleChoiceModel mcModelA = new OsmMultipleChoiceModel();
            mcModelA.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            mcModelA.setPrimaryId(id);
            mcModelA.setCorrectAnswer(0);
            mcModelA.setOptions(RSAUtils.encrypt2(model.getOptionA(), DailyConstant.publicKey));

            //选择题B
            OsmMultipleChoiceModel mcModelB = new OsmMultipleChoiceModel();
            mcModelB.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            mcModelB.setPrimaryId(id);
            mcModelB.setCorrectAnswer(0);
            mcModelB.setOptions(RSAUtils.encrypt2(model.getOptionB(), DailyConstant.publicKey));

            //选择题C
            OsmMultipleChoiceModel mcModelC = new OsmMultipleChoiceModel();
            mcModelC.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            mcModelC.setPrimaryId(id);
            mcModelC.setCorrectAnswer(0);
            mcModelC.setOptions(RSAUtils.encrypt2(model.getOptionC(), DailyConstant.publicKey));

            //选择题D
            OsmMultipleChoiceModel mcModelD = new OsmMultipleChoiceModel();
            mcModelD.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            mcModelD.setPrimaryId(id);
            mcModelD.setCorrectAnswer(0);
            mcModelD.setOptions(RSAUtils.encrypt2(model.getOptionD(), DailyConstant.publicKey));


            for (String answer : split) {
                if ("A".equals(answer.toUpperCase())) {
                    mcModelA.setCorrectAnswer(1);
                } else if ("B".equals(answer.toUpperCase())) {
                    mcModelB.setCorrectAnswer(1);
                } else if ("C".equals(answer.toUpperCase())) {
                    mcModelC.setCorrectAnswer(1);
                } else if ("D".equals(answer.toUpperCase())) {
                    mcModelD.setCorrectAnswer(1);
                }
            }
            int num = 0;
            if (StringUtils.isNotEmpty(mcModelA.getOptions())) {
                mcModelList.add(mcModelA);
                num += 1;
            }
            if (StringUtils.isNotEmpty(mcModelB.getOptions())) {
                mcModelList.add(mcModelB);
                num += 1;
            }
            if (StringUtils.isNotEmpty(mcModelC.getOptions())) {
                mcModelList.add(mcModelC);
                num += 1;
            }
            if (StringUtils.isNotEmpty(mcModelD.getOptions())) {
                mcModelList.add(mcModelD);
                num += 1;
            }
            if (num >= 1) {
                qbrModelList.add(qbrModel);
            }

            //使用范围记录
            scopeUserAnnal(osuModelList, id, model.getScopeUser());

        }

        boolean oqb = false;
        boolean osu = false;
        boolean mc = false;
        try {
            oqb = oqbrService.saveBatch(qbrModelList);
            osu = osuService.saveBatch(osuModelList);
            mc = omcService.saveBatch(mcModelList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //不需要手工抛运行时异常
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return oqb && osu && mc;

    }


    /**
     * @description: 主观题数据处理 题目内容，参考答案加密
     * @author: cxk
     * @date: 2021/4/1 17:34
     **/
    @Transactional(rollbackFor = Exception.class)
    boolean dataProcessingSub(List<RowNumObject<SubjectiveExcelModel>> models, String url) throws Exception {

        List<SubjectiveExcelModel> collect = models.stream().sorted(Comparator.comparing(RowNumObject::getRowNum))
                .map(RowNumObject::getObject).collect(Collectors.toList());

        //题库记录-汇总
        List<OsmQuestionBankRecordModel> oqbModelList = new ArrayList<>();
        //使用范围记录
        List<OsmScopeUserModel> osuModelList = new ArrayList<>();
        for (SubjectiveExcelModel seModel : collect) {
            //题库记录
            OsmQuestionBankRecordModel oqbModel = new OsmQuestionBankRecordModel();

            String id = UUID.randomUUID().toString().replaceAll("-", "");

            //使用范围记录
            scopeUserAnnal(osuModelList, id, seModel.getScopeUser());

            //id
            oqbModel.setId(id);
            //题目名称
            oqbModel.setTitleName(seModel.getTitleName());
            //题目内容
            oqbModel.setTitleContent(RSAUtils.encrypt2(seModel.getTitleContent(), DailyConstant.publicKey));
            //模块类型
            oqbModel.setModuleType(Integer.valueOf(
                    dictUtils.getKey(DictEnum.MODULE_TYPE.getCode(), seModel.getModuleType())));
            //试题标志
            oqbModel.setQuestionMark(Integer.valueOf(
                    dictUtils.getKey(DictEnum.WHETHER.getCode(), seModel.getQuestionMark())));
            //上传附件名
            oqbModel.setUploadFileName(
                    StringUtils.isNotEmpty(seModel.getUploadFileName()) ? url + seModel.getUploadFileName() : null);
            //适用层级
            oqbModel.setScopeAppliaction(Integer.valueOf(
                    dictUtils.getKey(DictEnum.SCOPE_APPLIACTION.getCode(), seModel.getScopeAppliaction())));
            //题目类型-主观题0
            oqbModel.setQuestionType(0);
            //题目答案
            oqbModel.setCorrectAnswer(Integer.valueOf(
                    dictUtils.getKey(DictEnum.ANSWER.getCode(), seModel.getCorrectAnswer())));
            //参考答案
            oqbModel.setReferenceAnswer(RSAUtils.encrypt2(seModel.getReferenceAnswer(), DailyConstant.publicKey));
            oqbModel.setInsertTime(new Date());
            oqbModel.setUpdateTime(new Date());
            oqbModelList.add(oqbModel);
        }
        boolean oqb = false;
        boolean osu = false;
        try {
            oqb = oqbrService.saveBatch(oqbModelList);
            osu = osuService.saveBatch(osuModelList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //不需要手工抛运行时异常
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return oqb && osu;
    }

    /**
     * 处理使用范围存在多个时
     **/
    @Transactional(rollbackFor = Exception.class)
    void scopeUserAnnal(List<OsmScopeUserModel> osuModelList, String id, String scopeUserStr) {
        String[] scopeUser = scopeUserStr.split("\\|");

        for (String sUser : scopeUser) {
            //使用范围
            OsmScopeUserModel osuModel = new OsmScopeUserModel();
            osuModel.setBankRecordId(id);
            osuModel.setScopeUser(dictUtils.getKey(DictEnum.SCOPE_USER.getCode(), sUser));
            osuModelList.add(osuModel);
        }
    }


}
