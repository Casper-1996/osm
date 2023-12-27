package  com.icbc.exam.entity.po;

import java.math.BigDecimal;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @author: liurong
 * @title: OsmExamRelModel
 * @projectName: plm_mgmt_npl 
 * @description: 试卷题目表(OSM_EXAM_REL)实体类
 * @data: 2021-04-09 14:10:05
 */
@Data
@TableName("OSM.OSM_EXAM_REL")
public class OsmExamRelModel implements Serializable {
    private static final long serialVersionUID = 273598256674117660L;
    
    /**
    * 主键
    */    
    @TableId("REL_ID")
    private String relId;
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
    * 选项 ( 1|2|3|4)
    */    
    @TableField("OPTION_ID")
    private String optionId;
    /**
    * 答案id
    */    
    @TableField("ANSWER_ID")
    private String answerId;
    /**
    * 题型
    */    
    @TableField("QUESTION_TYPE")
    private Integer questionType;
    /**
    * 题目顺序
    */    
    @TableField("QUESTION_SEQ")
    private Integer questionSeq;



}