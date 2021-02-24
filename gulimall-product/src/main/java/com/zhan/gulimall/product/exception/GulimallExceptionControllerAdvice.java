package com.zhan.gulimall.product.exception;

import com.zhan.common.exception.BizCodeEnume;
import com.zhan.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZHAN jgZHAN
 * @create 2021-01-18 14:40
 */
@Slf4j
@RestControllerAdvice("com.zhan.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();

        Map<String, String> map=new HashMap<>();
        result.getFieldErrors().forEach((item)->{
            String message=item.getDefaultMessage();
            String field=item.getField();
            map.put(field,message);

        });
        log.error("数据校验出现问题{},异常类型{}",e.getMessage(),e.getClass());

        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.error("错误异常：",throwable);
        return R.error(BizCodeEnume.UNKONW_EXCEPTION.getCode(),BizCodeEnume.UNKONW_EXCEPTION.getMsg());
    }


}
