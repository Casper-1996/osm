package  com.icbc.exam.entity.po;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @author: liurong
 * @title: OsmScopeUserModel
 * @projectName: plm_mgmt_npl 
 * @description: 题库使用范围表(OSM_SCOPE_USER)实体类
 * @data: 2021-04-06 14:07:02
 */
@Data
@TableName("OSM_SCOPE_USER")
public class OsmScopeUserModel implements Serializable {
    private static final long serialVersionUID = -78570258825340911L;
    
    /**
    * 题目id
    */    
    @TableField("BANK_RECORD_ID")
    private String bankRecordId;
    /**
    * 使用范围
    */    
    @TableField("SCOPE_USER")
    private String scopeUser;



}