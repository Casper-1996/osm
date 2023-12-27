package  com.icbc.exam.entity.po;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @author: liurong
 * @title: OsmExamDetailModel
 * @projectName: plm_mgmt_npl 
 * @description: 考试明细表(OSM_EXAM_DETAIL)实体类
 * @data: 2021-04-09 11:29:55
 */
@Data
@TableName("OSM.OSM_EXAM_DETAIL")
public class OsmExamDetailModel implements Serializable {
    private static final long serialVersionUID = 377742454538384641L;
    
    /**
    * 关系ID
    */    
    @TableField("REL_ID")
    private Integer relId;
    /**
    * 试卷id
    */    
    @TableField("EXAM_ID")
    private String examId;
    /**
    * 题目id
    */    
    @TableField("QUESTION_ID")
    private String questionId;
    /**
    * 答案
    */    
    @TableField("ANSWER")
    private String answer;
    /**
    * 题型
    */    
    @TableField("QUESTION_TYPE")
    private Integer questionType;
    /**
    * 主观题答案
    */    
    @TableField("SUBJECT_ANSWER")
    private String subjectAnswer;
    /**
    * 分数
    */    
    @TableField("SCORE")
    private BigDecimal score;
    /**
    * 主观题分数1--判卷人1
    */    
    @TableField("FIRST_SUBJECT_SCORE1")
    private BigDecimal firstSubjectScore1;
    /**
    * 主观题分数2--判卷人1
    */    
    @TableField("FIRST_SUBJECT_SCORE2")
    private BigDecimal firstSubjectScore2;
    /**
    * 用户id
    */    
    @TableField("USER_ID")
    private String userId;
    /**
    * 主观题分数1--判卷人2
    */    
    @TableField("SECOND_SUBJECT_SCORE1")
    private BigDecimal secondSubjectScore1;
    /**
    * 主观题分数2--判卷人2
    */    
    @TableField("SECOND_SUBJECT_SCORE2")
    private BigDecimal secondSubjectScore2;
    /**
    * 主观题分数1--判卷人3
    */    
    @TableField("THIRD_SUBJECT_SCORE1")
    private BigDecimal thirdSubjectScore1;
    /**
    * 主观题分数2--判卷人3
    */    
    @TableField("THIRD_SUBJECT_SCORE2")
    private BigDecimal thirdSubjectScore2;
    /**
    * 判卷人1用户ID
    */    
    @TableField("FIRST_USER_ID")
    private String firstUserId;
    /**
    * 判卷人1判卷时间
    */    
    @TableField("FIRST_TIME")
    private Date firstTime;
    /**
    * 判卷人2用户ID
    */    
    @TableField("SECOND_USER_ID")
    private String secondUserId;
    /**
    * 判卷人2判卷时间
    */    
    @TableField("SECOND_TIME")
    private Date secondTime;
    /**
    * 判卷人3用户ID
    */    
    @TableField("THIRD_USER_ID")
    private String thirdUserId;
    /**
    * 判卷人3判卷时间
    */    
    @TableField("THIRD_TIME")
    private Date thirdTime;



}