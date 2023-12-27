package  com.icbc.exam.entity.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @author: liurong
 * @title: OsmExamListModel
 * @projectName: osm-mgmt-exam
 * @description: 考试试卷管理(OSM_EXAM_LIST)实体类
 * @data: 2021-04-07 11:57:42
 */
@Data
@TableName("OSM.OSM_EXAM_LIST")
public class OsmExamListModel implements Serializable {
    private static final long serialVersionUID = 903596939692274610L;
    
    /**
    * 试卷id
    */    
    @TableId("EXAM_ID")
    private String examId;
    /**
    * 考试名称
    */    
    @TableField("EXAM_NAME")
    private String examName;
    /**
    * 使用范围
    */    
    @TableField("SCOPE_USER")
    private Integer scopeUser;
    /**
    * 适用层级
    */    
    @TableField("SCOPE_APPLIACTION")
    private Integer scopeAppliaction;
    /**
    * 考试开始时间
    */    
    @TableField("EXAM_START_DATE")
    private String examStartDate;
    /**
    * 考试结束时间
    */    
    @TableField("EXAM_END_DATE")
    private String examEndDate;
    /**
    * 考试时长
    */    
    @TableField("EXAM_DURATION")
    private String examDuration;
    /**
    * 创建时间
    */    
    @TableField("CREATE_TIME")
    private Date createTime;
    /**
    * 更新时间
    */    
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /**
     * 试卷配置
     */
    @TableField("EXAM_CONFIG")
    private String examConfig;
    /**
     * 考试状态 1未开始  2(考试中)已开始  3未分配判卷 4判卷中 5已完成
     */
    @TableField("EXAM_STATE")
    private String examState;
    /**
     * 题型比例
     */
    @TableField("MODULE_TYPE_SCALE")
    private String moduleTypeScale;





}