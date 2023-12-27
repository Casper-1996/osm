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
 * @title: OsmExamInfoModel
 * @projectName: plm_mgmt_npl 
 * @description: 考试记录表(OSM_EXAM_INFO)实体类
 * @data: 2021-04-07 16:48:40
 */
@Data
@TableName("OSM.OSM_EXAM_INFO")
public class OsmExamInfoModel implements Serializable {
    private static final long serialVersionUID = 697334739267834180L;

    /**
     * 主键ID
     */
    @TableId("INFO_ID")
    private String infoId;

    /**
    * 考试ID
    */    
    @TableField("EXAM_ID")
    private String examId;
    /**
    * 用户ID
    */    
    @TableField("USER_ID")
    private String userId;
    /**
    * 开始时间
    */    
    @TableField("START_TIME")
    private Date startTime;
    /**
    * 结束时间
    */    
    @TableField("END_TIME")
    private Date endTime;
    /**
    * 客观题分数
    */    
    @TableField("OBJECT_SCORE")
    private BigDecimal objectScore;
    /**
    * 主观题分数
    */    
    @TableField("SUBJECT_SCORE")
    private BigDecimal subjectScore;



}