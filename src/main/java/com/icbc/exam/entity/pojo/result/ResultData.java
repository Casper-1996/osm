package com.icbc.exam.entity.pojo.result;

/**
 * @author cyt
 * @title: ResultData
 * @projectName viaspolice
 * @date 2019/6/19 13:43
 */
public class ResultData {
    private Integer code;
    private String msg;
    private Object data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
