package cn.myfreecloud.spit.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 专门来处理异常的类
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        e.printStackTrace();

        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
