
package com.icbc.exam.entity.pojo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 资源
 * </p>
 *
 *
 * @since 2018-01-29
 */
@Data
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 资源名称
	 */
	private String name;
	/**
	 * 资源的权限
	 */
	private String permissions;
	/**
	 * 资源路径
	 */
	private String url;
	/**
	 * 打开方式 ajax,iframe
	 */
	private String openMode;
	/**
	 * 资源介绍
	 */
	private String description;
	/**
	 * 资源图标
	 */
	private String iconCls;
	/**
	 * 父级资源id
	 */
	private Integer pid;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 打开状态
	 */
	private Boolean opened;
	/**
	 * 资源类别
	 */
	private Integer resourceType;
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


    private String resourceClass;



}
