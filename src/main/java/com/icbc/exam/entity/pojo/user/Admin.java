
package com.icbc.exam.entity.pojo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 *
 * @since 2018-04-01
 */
@Data
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 用户类别[0:管理员,1:普通员工]
	 */
	private Integer userType;
	/**
	 * 组织id
	 */
	private Integer organizationId;
	/**
	 * 是否锁定[0:正常,1:锁定]
	 */
	private Integer locked;
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
