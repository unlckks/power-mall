package com.mingyun.aspect;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-03 14:49
 */
@Component
@Aspect
@Slf4j
public class MyLogAspect {
    // 切点
    public static final String POINT_CUT = "execution (* com.mingyun.controller.*.*(..))";

    // 通知
    @Around(value = POINT_CUT)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String api = request.getRequestURI();
        String ip = request.getRemoteAddr();
        Object[] args = joinPoint.getArgs();
        // 拿类型和方法名称
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        Class<?> aClass = method.getDeclaringClass();
        String typeName = aClass.getTypeName();
        // 拿这个方法是干啥的？
        String desc = "";
        boolean flag = method.isAnnotationPresent(ApiOperation.class);
        if (flag) {
            ApiOperation operation = method.getDeclaredAnnotation(ApiOperation.class);
            desc = operation.value();
        }
        long start = System.currentTimeMillis();
        // 执行目标方法
        result = joinPoint.proceed(args);
        long end = System.currentTimeMillis();
        long spendTime = end - start;
        String finalArgs = null;
        if (!ObjectUtils.isEmpty(args) && args[0] instanceof MultipartFile) {
            finalArgs = "file";
        } else {
            finalArgs = JSON.toJSONString(args);
        }
        log.info("调用时间:{},路径为:{},ip为:{},方法为:{},执耗时为:{},描述:{},参数为:{},结果为:{}",
                new Date(),
                api,
                ip,
                typeName + "." + methodName,
                spendTime,
                desc,
                finalArgs,
                JSON.toJSONString(result)
        );
        return result;
    }
}
