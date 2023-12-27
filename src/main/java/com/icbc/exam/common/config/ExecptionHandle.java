package com.icbc.exam.common.config;

import com.icbc.exam.common.exception.AuthException;
import com.icbc.exam.entity.pojo.result.ResultData;
import com.icbc.exam.entity.pojo.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cyt
 * @title: ExecptionHandle
 * @projectName pes-mgmt-main
 * @description: 统一异常处理
 * @date 2021/1/14 16:01
 */
@Slf4j
@ControllerAdvice
public class ExecptionHandle {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData handle(Exception e) {
        log.error(e.getMessage(),e);
        if(e instanceof AuthException){
            AuthException authExcepton =(AuthException)e;
            return ResultUtil.error(authExcepton.getCode(),authExcepton.getMessage());
        }
        return ResultUtil.error();
    }
}
