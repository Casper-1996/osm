package com.icbc.exam.resource;

import com.github.pagehelper.PageInfo;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.pojo.result.ResultUtil;
import com.icbc.exam.entity.vo.OsmAddOrModeifyReq;
import com.icbc.exam.entity.vo.OsmMultipleDetailsResp;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordReq;
import com.icbc.exam.entity.vo.OsmQuestionBankRecordResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.icbc.exam.entity.po.OsmQuestionBankRecordModel;
import org.springframework.web.bind.annotation.*;
import com.icbc.exam.service.OsmQuestionBankRecordService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author: liurong
 * @title: OcmQuestionBankRecordResource
 * @projectName: plm_mgmt_baddebt
 * @description: 题库记录表(OCM_QUESTION_BANK_RECORD)表控制层
 * @data: 2021-04-01 17:28:17
 */
@Slf4j
@RestController
@RequestMapping("/oqbRecord")
public class OsmQuestionBankRecordResource {
    /**
     * 服务对象
     */
    @Autowired
    private OsmQuestionBankRecordService service;


    /**
     * 列表展示
     **/
    @PostMapping("/displayList")
    public ResultData displayList(@RequestBody OsmQuestionBankRecordReq req) {
        PageInfo<OsmQuestionBankRecordResp> respPageInfo;
        try {
            respPageInfo = service.displayList(req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultUtil.success();
        }
        return ResultUtil.success(respPageInfo);
    }


    /**
     * 非选择题详情展示
     **/
    @GetMapping("/nonMultiple/findById/{topicId}")
    public ResultData nonMultiple(@PathVariable("topicId") String topicId) {
        OsmMultipleDetailsResp resp;
        try {
            resp = service.nonMultiple(topicId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultUtil.success();
        }
        return ResultUtil.success(resp);
    }

    /**
     * 选择题详情展示
     **/
    @GetMapping("/multiple/findById/{topicId}")
    public ResultData multiple(@PathVariable("topicId") String topicId) {
        OsmMultipleDetailsResp model;
        try {
            model = service.multiple(topicId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultUtil.success();
        }
        return ResultUtil.success(model);
    }

    /**
     * 删除题库
     **/
    @GetMapping("/delete/{topicId}")
    public ResultData delete(@PathVariable("topicId") String topicId) {
        boolean state = service.deleteById(topicId);
        if (!state) {
            return ResultUtil.error("本题无法被删除");
        }
        return ResultUtil.success();
    }


    /**
     * 修改题目
     **/
    @PostMapping("/update")
    public ResultData update(@RequestBody OsmAddOrModeifyReq req) {
        String resultStr = verificationParam(req);
        if (StringUtils.isNotEmpty(resultStr)) {
            return ResultUtil.error(resultStr);
        }
        try {
            boolean flag = service.updateTopic(req);
            if (!flag) {
                return ResultUtil.error("本题无法被修改");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return ResultUtil.success();
    }

    /**
     * 手动新增题目
     **/
    @PostMapping("/insert")
    public ResultData insert(@RequestBody OsmAddOrModeifyReq req) {
        //验证参数
        String resultStr = verificationParam(req);
        if (StringUtils.isNotEmpty(resultStr)) {
            return ResultUtil.error(resultStr);
        }
        try {
            service.saveTopic(req);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        return ResultUtil.success();
    }

    /**
     * 上传材料
     **/
    @PostMapping("/importMaterial")
    public ResultData insert(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultUtil.error("请上传材料");
        }
        String fileName = service.uploadFile(file);
        if (StringUtils.isEmpty(fileName)) {
            return ResultUtil.error("文件上传失败");
        }
        return ResultUtil.success(fileName);
    }

    /**
     * 验证参数
     **/
    private String verificationParam(OsmAddOrModeifyReq req) {
        String resultStr = null;
        if (req == null) {
            req = new OsmAddOrModeifyReq();
        }

        //汉字录入数据验证
        if (req.getQuestionType() == 4) {
            if (StringUtils.isEmpty(req.getTitleName())) {
                resultStr = "题目名称不能为空";
            }
            if (StringUtils.isEmpty(req.getTitleContent())) {
                resultStr = "题目内容不能为空";
            }
            if (req.getQuestionType() == null) {
                resultStr = "题目类型不能为空";
            }

            return resultStr;
        }


        if (StringUtils.isEmpty(req.getTitleName())) {
            resultStr = "题目名称不能为空";
        }
        if (StringUtils.isEmpty(req.getTitleContent())) {
            resultStr = "题目内容不能为空";
        }
        if (req.getQuestionType() == null) {
            resultStr = "题目类型不能为空";
        }
        if (req.getQuestionType() == 0 || req.getQuestionType() == 1) {
            if (req.getCorrectAnswer() == null) {
                resultStr = "题目答案不能为空";
            }
        }
        if (req.getQuestionMark() == null) {
            resultStr = "试题标志不能为空";
        }
        if (req.getScopeAppliaction() == null) {
            resultStr = "适用层级不能为空";
        }
        if (req.getScopeUsers() == null || req.getScopeUsers().size() < 1) {
            resultStr = "使用范围不能为空";
        }
        if (req.getModuleType() == null) {
            resultStr = "模块类型不能为空";
        }
        return resultStr;

    }
}