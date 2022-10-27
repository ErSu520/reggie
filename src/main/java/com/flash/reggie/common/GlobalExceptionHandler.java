package com.flash.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());
        if(exception.getMessage().contains("Duplicate entry")){
            String[] errors = exception.getMessage().split(" ");
            return R.error(errors[2] + "用户已经存在");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        return R.error(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }

}
