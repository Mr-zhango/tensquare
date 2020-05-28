package cn.myfreecloud.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//定义异常处理的Controller
@RestControllerAdvice
public class BaseExceptionHandler {

    //注解就是标识处理那些异常
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        e.printStackTrace();

        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

}
