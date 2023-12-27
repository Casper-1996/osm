package com.icbc.exam.entity.pojo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T> 泛型标记
 * @author L.cm
 */
@Data
public class R<T> implements Serializable {
	private static final long serialVersionUID = -1160662278280275915L;

	private int code;
	private String msg;
	private T data;

}