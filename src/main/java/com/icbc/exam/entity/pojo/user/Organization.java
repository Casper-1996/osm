
package com.icbc.exam.entity.pojo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 组织机构
 * </p>
 *
 *
 * @since 2018-02-05
 */
@Data
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 组织名
	 */
	private String name;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 编号
	 */
	private String code;
	/**
	 * 图标
	 */
	@TableField("icon_cls")
	private String iconCls;
	/**
	 * 父级主键
	 */
	private Integer pid;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 状态[0:失效,1:正常]
	 */
	@TableLogic
	private Integer status;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;

    /**
     * 级别
     */
    private Integer orgLevel;
}
