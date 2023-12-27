package  com.icbc.exam.entity.po;

import java.math.BigDecimal;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @author: liurong
 * @title: OsmMultipleChoiceModel
 * @projectName: plm_mgmt_npl 
 * @description: 选择题(OSM_MULTIPLE_CHOICE)实体类
 * @data: 2021-04-01 17:04:22
 */
@Data
@TableName("OSM_MULTIPLE_CHOICE")
public class OsmMultipleChoiceModel implements Serializable {
    private static final long serialVersionUID = -91783314620415480L;
    
    /**
    * 选择题id
    */    
    @TableId("ID")
    private String id;
    /**
    * 主表id
    */    
    @TableField("PRIMARY_ID")
    private String primaryId;
    /**
    * 选项
    */    
    @TableField("OPTIONS")
    private String options;
    /**
    * 是否正确答案(0:否，1:是)
    */    
    @TableField("CORRECT_ANSWER")
    private Integer correctAnswer;



}