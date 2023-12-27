package com.icbc.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icbc.exam.common.constant.DailyConstant;
import com.icbc.exam.common.enums.DictEnum;
import com.icbc.exam.common.enums.FlagEnum;
import com.icbc.exam.common.util.other.CopyPropertiesUtils;
import com.icbc.exam.common.util.other.DictUtils;
import com.icbc.exam.common.util.other.RSAUtils;
import com.icbc.exam.dao.OsmExamRelDao;
import com.icbc.exam.dao.OsmMultipleChoiceDao;
import com.icbc.exam.dao.OsmScopeUserDao;
import com.icbc.exam.entity.bo.ExamQuesNumCheck;
import com.icbc.exam.entity.bo.ModuleQuesNum;
import com.icbc.exam.entity.bo.ModuleScale;
import com.icbc.exam.entity.bo.QuestionTypeNum;
import com.icbc.exam.entity.po.OsmMultipleChoiceModel;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import com.icbc.exam.dao.OsmQuestionBankRecordDao;


import com.icbc.exam.entity.po.OsmScopeUserModel;
import com.icbc.exam.entity.pojo.excel.JudgmentExcelModel;
import com.icbc.exam.entity.pojo.excel.MultipleChoiceExcelModel;
import com.icbc.exam.entity.pojo.excel.RowNumObject;
import com.icbc.exam.entity.pojo.excel.SubjectiveExcelModel;
import com.icbc.exam.entity.vo.OsmAddOrModeifyReq;
import com.icbc.exam.entity.vo.OsmMultipleDetailsResp;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordReq;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordResp;
import com.icbc.exam.service.OsmMultipleChoiceService;
import com.icbc.exam.service.OsmQuestionBankRecordService;
import com.icbc.exam.service.OsmScopeUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: liurong
 * @title: OcmQuestionBankRecordServiceImpl
 * @projectName: plm_mgmt_baddebt
 * @description: 题库记录表(OCM_QUESTION_BANK_RECORD)表服务实现类
 * @data: 2021-04-01 17:28:17
 */

@Slf4j
@Service
@Transactional
public class OsmQuestionBankRecordServiceImpl
        extends ServiceImpl<OsmQuestionBankRecordDao, OsmQuestionBankRecordModel> implements OsmQuestionBankRecordService {
    @Autowired
    private OsmQuestionBankRecordDao oqbrDao;
    @Autowired
    private OsmMultipleChoiceDao omcDao;
    @Autowired
    private OsmScopeUserDao osuDao;
    @Autowired
    private OsmExamRelDao oerDao;
    @Autowired
    private DictUtils dictUtils;

    @Autowired
    private OsmQuestionBankRecordDao ocmQuestionBankRecordDao;

    @Autowired
    private OsmQuestionBankRecordService service;
    @Autowired
    private OsmScopeUserService osuService;
    @Autowired
    private OsmMultipleChoiceService omcService;

    @Value("${topic.fileUrl}")
    private String topic;

    @Value("${prefix.url}")
    private String prefixUrl;


    @Override
    public int findQuestionTypeNum(ExamQuesNumCheck examQuesNumCheck) {
        return ocmQuestionBankRecordDao.findQuestionTypeNum(examQuesNumCheck);
    }

    @Override
    public QuestionTypeNum findModuleQuesNum(ModuleScale moduleScale) {
        return ocmQuestionBankRecordDao.findModuleQuesNum(moduleScale);
    }


    @Override
    public PageInfo<OsmQuestionBankRecordResp> displayList(OsmQuestionBankRecordReq req) throws Exception {
        if (req == null) {
            req = new OsmQuestionBankRecordReq();
        }
        Integer pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        Integer pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);

        if (req.getQuestionType() != null && req.getQuestionType() == 4) {
            List<OsmQuestionBankRecordResp> result = oqbrDao.ccQuestions(req);

            for (OsmQuestionBankRecordResp resp : result) {
                resp.setTitleContent(RSAUtils.decrypt2(resp.getTitleContent(), DailyConstant.privateKey));
                //是否可被修改
                int num = oerDao.selectByIdCount(resp.getId());
                if (num > 1) {
                    resp.setFlag(0);
                } else {
                    resp.setFlag(1);
                }
            }
            return new PageInfo<OsmQuestionBankRecordResp>(result);

        }

        //查询结果
        List<OsmQuestionBankRecordResp> result;
        //判断请求参数中是存在了适用范围条件
        if (req.getScopeUsers().size() < 1) {
            result = oqbrDao.noExistingQuestions(req);
        } else {
            result = oqbrDao.existingQuestions(req);
        }

        for (OsmQuestionBankRecordResp resp : result) {
            //模块类型
            if (resp.getModuleType() != null) {
                resp.setModuleTypeStr(
                        dictUtils.getValue(DictEnum.MODULE_TYPE.getCode(), resp.getModuleType().toString()));
            }
            //试题标志是否模拟题 0:否，1:是)
            if (resp.getQuestionMark() != null) {
                resp.setQuestionMarkStr(
                        dictUtils.getValue(DictEnum.WHETHER.getCode(), resp.getQuestionMark().toString()));
            }
            //适用层级
            if (resp.getScopeAppliaction() != null) {
                resp.setScopeAppliactionStr(
                        dictUtils.getValue(DictEnum.SCOPE_APPLIACTION.getCode(), resp.getScopeAppliaction().toString()));
            }
            //题目类型
            if (resp.getQuestionType() != null) {
                resp.setQuestionTypeStr(
                        dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), resp.getQuestionType().toString()));
            }
            //题目答案(0:错，1:对)
            if (resp.getCorrectAnswer() != null) {
                resp.setCorrectAnswerStr(
                        dictUtils.getValue(DictEnum.ANSWER.getCode(), resp.getCorrectAnswer().toString()));
            }

            //查询使用范围
            StringBuilder scopeUser = new StringBuilder();
            List<OsmScopeUserModel> list = osuDao.selectScopeUser(resp.getId());
            for (OsmScopeUserModel model : list) {
                scopeUser.append(dictUtils.getValue(DictEnum.SCOPE_USER.getCode(), model.getScopeUser()));
                scopeUser.append("|");
            }
            log.info("scopeUser:{}", scopeUser);
            if (StringUtils.isNotEmpty(scopeUser.toString())) {
                resp.setScopeUser(scopeUser.substring(0, scopeUser.length() - 1));
            }

            //是否可被修改
            int num = oerDao.selectByIdCount(resp.getId());
            if (num > 1) {
                resp.setFlag(0);
            } else {
                resp.setFlag(1);
            }

        }

        return new PageInfo<OsmQuestionBankRecordResp>(result);
    }

    @Override
    public OsmMultipleDetailsResp nonMultiple(String topicId) throws Exception {
        OsmQuestionBankRecordModel model = oqbrDao.selectById(topicId);
        OsmMultipleDetailsResp resp = CopyPropertiesUtils.copy(model, OsmMultipleDetailsResp.class);
        //题目内容
        resp.setTitleContent(RSAUtils.decrypt2(model.getTitleContent(), DailyConstant.privateKey));
        if (resp.getReferenceAnswer() != null) {
            //参考答案
            resp.setReferenceAnswer(RSAUtils.decrypt2(model.getReferenceAnswer(), DailyConstant.privateKey));
        }

        //模块类型
        if (resp.getModuleType() != null) {
            resp.setModuleTypeStr(
                    dictUtils.getValue(DictEnum.MODULE_TYPE.getCode(), resp.getModuleType().toString()));
        }
        //试题标志是否模拟题 0:否，1:是)
        if (resp.getQuestionMark() != null) {
            resp.setQuestionMarkStr(
                    dictUtils.getValue(DictEnum.QUESTION_MARK.getCode(), resp.getQuestionMark().toString()));
        }
        //适用层级
        if (resp.getScopeAppliaction() != null) {
            resp.setScopeAppliactionStr(
                    dictUtils.getValue(DictEnum.SCOPE_APPLIACTION.getCode(), resp.getScopeAppliaction().toString()));
        }
        //题目类型
        if (resp.getQuestionType() != null) {
            resp.setQuestionTypeStr(
                    dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), resp.getQuestionType().toString()));
        }
        //题目答案(0:错，1:对)
        if (resp.getCorrectAnswer() != null) {
            resp.setCorrectAnswerStr(
                    dictUtils.getValue(DictEnum.ANSWER.getCode(), resp.getCorrectAnswer().toString()));
        }
        if (StringUtils.isNotEmpty(resp.getUploadFileName())) {
            resp.setUploadFileName(prefixUrl + resp.getUploadFileName());
        }

        //查询使用范围
        List<String> scopeUser = new ArrayList<>();
        List<OsmScopeUserModel> list = osuDao.selectScopeUser(resp.getId());
        for (OsmScopeUserModel osuModel : list) {
            scopeUser.add(osuModel.getScopeUser());
        }
        resp.setScopeUser(scopeUser);

        return resp;
    }

    @Override
    public OsmMultipleDetailsResp multiple(String topicId) throws Exception {
        OsmQuestionBankRecordModel model = oqbrDao.selectById(topicId);
        OsmMultipleDetailsResp resp = CopyPropertiesUtils.copy(model, OsmMultipleDetailsResp.class);
        List<OsmMultipleChoiceModel> list = omcDao.selectByPrimaryId(topicId);
        //选项
        for (OsmMultipleChoiceModel omcModel : list) {
            if (omcModel.getOptions() == null) {
                continue;
            }
            omcModel.setOptions(RSAUtils.decrypt2(omcModel.getOptions(), DailyConstant.privateKey));
        }
        //题目内容
        resp.setTitleContent(RSAUtils.decrypt2(resp.getTitleContent(), DailyConstant.privateKey));
        //模块类型
        if (resp.getModuleType() != null) {
            resp.setModuleTypeStr(
                    dictUtils.getValue(DictEnum.MODULE_TYPE.getCode(), resp.getModuleType().toString()));
        }
        //试题标志是否模拟题 0:否，1:是)
        if (resp.getQuestionMark() != null) {
            resp.setQuestionMarkStr(
                    dictUtils.getValue(DictEnum.QUESTION_MARK.getCode(), resp.getQuestionMark().toString()));
        }
        //适用层级
        if (resp.getScopeAppliaction() != null) {
            resp.setScopeAppliactionStr(
                    dictUtils.getValue(DictEnum.SCOPE_APPLIACTION.getCode(), resp.getScopeAppliaction().toString()));
        }
        //题目类型
        if (resp.getQuestionType() != null) {
            resp.setQuestionTypeStr(
                    dictUtils.getValue(DictEnum.QUESTION_TYPE.getCode(), resp.getQuestionType().toString()));
        }
        //题目答案(0:错，1:对)
        if (resp.getCorrectAnswer() != null) {
            resp.setCorrectAnswerStr(
                    dictUtils.getValue(DictEnum.ANSWER.getCode(), resp.getCorrectAnswer().toString()));
        }
        if (StringUtils.isNotEmpty(resp.getUploadFileName())) {
            resp.setUploadFileName(prefixUrl + resp.getUploadFileName());
        }

        //查询使用范围
        List<String> scopeUser = new ArrayList<>();
        List<OsmScopeUserModel> lists = osuDao.selectScopeUser(resp.getId());
        for (OsmScopeUserModel osuModel : lists) {
            scopeUser.add(osuModel.getScopeUser());
        }
        resp.setScopeUser(scopeUser);

        resp.setOmcModelList(list);

        return resp;
    }

    @Override
    public boolean deleteById(String topicId) {
        boolean flag = false;
        //是否存在考试表中
        int i = oerDao.selectByIdCount(topicId);
        if (i > 0) {
            return flag;
        }
        int num = oqbrDao.deleteById(topicId);
        if (num > 0) {
            flag = true;
            //选择数量
            int count = omcService.selectByIdCount(topicId);
            if (count > 0) {
                flag = omcService.removeByPrimaryId(topicId);
            }
            log.info("删除：{},成功", topicId);
        }

        return flag;
    }

    @Override
    public boolean updateTopic(OsmAddOrModeifyReq req) throws Exception {
        boolean flag = false;
        int i = oerDao.selectByIdCount(req.getId());
        if (i > 0) {
            return flag;
        }
        if (StringUtils.isNotEmpty(req.getUploadFileName())) {
            int index = req.getUploadFileName().indexOf(prefixUrl);
            if (index >= 0) {
                req.setUploadFileName(req.getUploadFileName().replace(prefixUrl, ""));
            }
        }
        //加密题目内容
        if (StringUtils.isNotEmpty(req.getTitleContent())) {
            req.setTitleContent(RSAUtils.encrypt2(req.getTitleContent(), DailyConstant.publicKey));
        }
        //加密参考答案
        if (StringUtils.isNotEmpty(req.getReferenceAnswer())) {
            req.setReferenceAnswer(RSAUtils.encrypt2(req.getReferenceAnswer(), DailyConstant.publicKey));
        }
        req.setUpdateTime(new Date());
        log.info("数据操作前的flag：{}", flag);
        if (req.getQuestionType() == 2 || req.getQuestionType() == 3) {
            flag = dataProcessing(req);
        }
        if (req.getQuestionType() == 0) {
            flag = dataProcessingSub(req);
        }
        if (req.getQuestionType() == 1) {
            flag = dataProcessingJud(req);
        }
        if (req.getQuestionType() == 4) {
            flag = dataProcessingCC(req);
        }
        log.info("数据操作后的flag：{}", flag);
        if (flag) {
            //修改适用范围
            List<String> scopeUsers = req.getScopeUsers();
            List<String> scopeUsersList = osuService.selectById(req.getId());

            //添加新增
            for (String scopeUser : scopeUsers) {
                boolean contains = scopeUsersList.contains(scopeUser);
                if (!contains) {
                    OsmScopeUserModel model = new OsmScopeUserModel();
                    model.setScopeUser(scopeUser);
                    model.setBankRecordId(req.getId());
                    osuService.save(model);
                }
            }
            //删除多余 （数据库查询与 上传 取差集）
            scopeUsersList.removeAll(scopeUsers);
            for (String scopeUser : scopeUsersList) {
                osuService.removeOld(scopeUser, req.getId());
            }
        }

        return flag;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTopic(OsmAddOrModeifyReq req) throws Exception {
        if (req == null) {
            req = new OsmAddOrModeifyReq();
        }
        //使用范围
        List<OsmScopeUserModel> osuModelList = new ArrayList<>();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        req.setId(id);

        //加密题目内容
        if (StringUtils.isNotEmpty(req.getTitleContent())) {
            req.setTitleContent(RSAUtils.encrypt2(req.getTitleContent(), DailyConstant.publicKey));
        }
        //加密参考答案
        if (StringUtils.isNotEmpty(req.getReferenceAnswer())) {
            req.setReferenceAnswer(RSAUtils.encrypt2(req.getReferenceAnswer(), DailyConstant.publicKey));
        }
        //使用范围
        scopeUserTreat(id, req.getScopeUsers(), osuModelList);

        boolean flag = false;
        List<OsmMultipleChoiceModel> omcList = new ArrayList<>();
        //选择题处理
        if (req.getQuestionType() == 2 || req.getQuestionType() == 3) {
            omcList = multipleChoice(id, req.getOmcModelList());
            flag = true;
        }
        req.setInsertTime(new Date());
        req.setUpdateTime(new Date());
        //题库入库
        service.save(req);
        //使用范围入库
        osuService.saveBatch(osuModelList);
        //选择题入库
        if (omcList.size() > 0 && flag) {
            omcService.saveBatch(omcList);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = "";
        String filePath = topic + "importMaterial/";
        File dic = new File(filePath);
        if (!dic.exists()) {
            dic.mkdirs();
        }
        fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffixName;

        File file1 = new File(filePath + fileName);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return "importMaterial/" + fileName;
    }

    @Override
    public OsmQuestionBankRecordModel findQuesById(String questionId) {
        return ocmQuestionBankRecordDao.selectById(questionId);
    }

    /**
     * 新增方法 使用范围
     **/
    private void scopeUserTreat(String id, List<String> scopeUsers, List<OsmScopeUserModel> osuList) {
        if (scopeUsers.size() < 1) {
            return;
        }
        for (String scopeUser : scopeUsers) {
            OsmScopeUserModel model = new OsmScopeUserModel();
            model.setBankRecordId(id);
            model.setScopeUser(scopeUser);
            osuList.add(model);
        }
        log.info("osuList.size():{}", osuList.size());
    }

    /**
     * 新增方法 选择题 选项处理
     **/
    private List<OsmMultipleChoiceModel> multipleChoice(String id, List<OsmMultipleChoiceModel> multipleChoice) throws Exception {
        List<OsmMultipleChoiceModel> result = new ArrayList<>();
        if (multipleChoice.size() < 1) {
            return result;
        }
        for (OsmMultipleChoiceModel model : multipleChoice) {
            model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            model.setPrimaryId(id);
            model.setOptions(RSAUtils.encrypt2(model.getOptions(), DailyConstant.publicKey));
            if (StringUtils.isNotEmpty(model.getOptions())) {
                result.add(model);
            }
        }
        return result;
    }

    /**
     * 选择题数据处理 单选多选
     **/
    boolean dataProcessing(OsmAddOrModeifyReq req) throws Exception {
        boolean flag = false;
        //选项部分
        List<OsmMultipleChoiceModel> omcModelList = req.getOmcModelList();

        for (OsmMultipleChoiceModel model : omcModelList) {
            boolean inOrUp = false;
            //id 题目id 都空
            if (StringUtils.isEmpty(model.getId())
                    && StringUtils.isEmpty(model.getPrimaryId())
                    && StringUtils.isEmpty(model.getOptions())) {
                continue;
            }
            //新增选项 id为空 选项不为空
            if (StringUtils.isEmpty(model.getId()) && StringUtils.isNotEmpty(model.getOptions())) {
                model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                model.setPrimaryId(req.getId());
                log.info("新增选项：{},Id:{}", model.getOptions(), model.getId());
                //加密
                inOrUp = true;
            }

            //删除选项 id不为空 选项为空
            if (StringUtils.isEmpty(model.getOptions()) && StringUtils.isNotEmpty(model.getId())) {
                omcService.removeById(model.getId());
            }
            model.setOptions(RSAUtils.encrypt2(model.getOptions(), DailyConstant.publicKey));
            if (inOrUp) {
                omcService.save(model);
            } else {
                omcService.updateById(model);
            }

        }
        flag = service.updateById(req);

        return flag;
    }

    /**
     * 主观题数据处理
     **/
    boolean dataProcessingSub(OsmAddOrModeifyReq req) {
        boolean flag = false;
        //主观题0
        req.setQuestionType(0);

        flag = service.updateById(req);
        return flag;
    }


    /**
     * 判断题数据处理
     **/
    boolean dataProcessingJud(OsmAddOrModeifyReq req) {
        boolean flag = false;
        //判断题1
        req.setQuestionType(1);

        flag = service.updateById(req);
        return flag;
    }

    /**
     * 汉字录入数据处理
     **/
    boolean dataProcessingCC(OsmAddOrModeifyReq req) {
        boolean flag = false;
        //判断题1
        req.setQuestionType(4);

        flag = service.updateById(req);
        return flag;
    }

}
