
package com.icbc.exam.entity.pojo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色
 * </p>
 *
 *
 * @since 2018-01-29
 */
@Data
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 角色名
	 */
	private String name;
	/**
	 * 排序号
	 */
	private Integer seq;
	/**
	 * 简介
	 */
	private String description;
	/**
	 * 图标
	 */
	private String iconCls;
	/**
	 * 状态[0:失效,1:正常]
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
}
