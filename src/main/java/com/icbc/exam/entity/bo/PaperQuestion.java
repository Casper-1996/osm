package com.icbc.exam.entity.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author liurong
 * @title: PaperQuestion
 * @projectName osm-mgmt-exam
 * @description:
 * @date 2021/4/8 17:54
 */
@Data
public class PaperQuestion {

    /**
     * 题目id
     */
    private String id;
    /**
     * 题目内容
     */
    private String titleContent;
    /**
     * 上传附件名
     */
    private String uploadFileName;
    /**
     * 题目答案(选择题 0:错，1:对)
     */
    private Integer correctAnswer;

    /**
     * 参考答案
     */
    private String referenceAnswer;

}
