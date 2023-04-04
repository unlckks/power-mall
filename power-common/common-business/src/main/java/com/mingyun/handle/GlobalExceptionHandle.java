package com.mingyun.handle;

import com.mingyun.constant.BusinessEnum;
import com.mingyun.ex.BusinessException;
import com.mingyun.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 18:41
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    /**
     *  业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> businessExHandler(BusinessException e){
        return Result.fail(BusinessEnum.OPERATION_FAIL.getCode(), e.getMessage());
    }

    /**
     * 异常记录
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> runtimeExHandler(RuntimeException e){
        log.error("-----------runtimeEx",e);
        return Result.fail(BusinessEnum.SERVER_INNER_ERROR);
    }
    /**
     * 方法参数无效异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> methodArgumentNotValidExExHandler(MethodArgumentNotValidException e){
        List<ObjectError> allErrors = e.getAllErrors();
        StringBuilder sb = new StringBuilder();
        allErrors.forEach(objectError -> {
            String messages = objectError.getDefaultMessage();
            sb.append(messages+"\n");
        });
        String realMsg =sb.delete(sb.lastIndexOf("\n"),sb.length()).toString();
        return  Result.fail(BusinessEnum.OPERATION_FAIL.getCode(),realMsg);
    }

}
