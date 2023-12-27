package com.icbc.exam.entity.pojo.result;

import com.icbc.exam.common.enums.ResultEnum;

/**
 * @author cyt
 * @title: ResultUtil
 */
public class ResultUtil {

    public static ResultData success(Object object){
        ResultData result=new ResultData();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }

    public static ResultData success(){
        return success(null);
    }

    public static ResultData error(){
        ResultData result=new ResultData();
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMsg(ResultEnum.ERROR.getMsg());
        return result;
    }

    public static ResultData error(String message){
        ResultData result=new ResultData();
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMsg(message);
        return result;
    }

    public static ResultData error(Integer code,String message){
        ResultData result=new ResultData();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    public static ResultData paramError(String message){
        ResultData result=new ResultData();
        result.setCode(ResultEnum.PARAM_ERROR.getCode());
        result.setMsg(message);
        return result;
    }

    public static ResultData cannotError(String message) {
        ResultData result=new ResultData();
        result.setCode(ResultEnum.CAN_NOT_OPERATE.getCode());
        result.setMsg(message);
        return result;
    }

    public static ResultData paramListError(Object object) {
        ResultData result=new ResultData();
        result.setCode(ResultEnum.PARAM_ERROR.getCode());
        result.setMsg(ResultEnum.PARAM_ERROR.getMsg());
        result.setData(object);
        return result;
    }

}
