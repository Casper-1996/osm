package com.icbc.exam.common.enums;

/**
 * @author cyt
 * @title: ResultEnum
 * @projectName plm_mgmt_npl
 * @description: 返回值枚举
 * @date 2020/5/14 16:05
 */
public enum ResultEnum {

    //成功
    SUCCESS(200,"成功"),
    //内部错误
    ERROR(500,"内部错误"),
    //登录异常
    NOT_AUTH(401,"登录异常"),
    //参数校验错误
    PARAM_ERROR(501,"参数校验错误"),
    //不允许操作
    CAN_NOT_OPERATE(502,"不允许操作");

    private Integer code;
    private String msg;

    private ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static Integer getCodeByMsg(String msg){
        for(ResultEnum enumitem : ResultEnum.values()){
            if(enumitem.getMsg().equals(msg)){
                return enumitem.getCode();
            }
        }
        return null;
    }

    public static String getMsgByCode(Integer code){
        for(ResultEnum enumitem : ResultEnum.values()){
            if(enumitem.getCode().equals(code)){
                return enumitem.getMsg();
            }
        }
        return null;
    }
}
