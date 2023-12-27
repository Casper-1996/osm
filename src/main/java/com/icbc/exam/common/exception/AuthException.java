package com.icbc.exam.common.exception;

import com.icbc.exam.common.enums.ResultEnum;

/**
 * @author cyt
 * @title: AuthException
 * @projectName plm_mgmt_npl
 * @description: 登录异常
 * @date 2020/7/15 13:25
 */
public class AuthException extends RuntimeException{
    private Integer code;

    public AuthException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
