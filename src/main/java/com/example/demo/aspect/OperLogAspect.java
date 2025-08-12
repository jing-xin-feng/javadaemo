package com.example.demo.aspect;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
//日志切面
@Aspect
@Component
@Slf4j
public class OperLogAspect {
    @Around("execution(* com.example.demo.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        // 添加请求参数日志
        log.info("开始执行: {}. 参数: {}", methodName, Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("执行完成: {}. 耗时: {}ms", methodName, System.currentTimeMillis() - startTime);
            return result;
        } catch (Exception e) {
            log.error("执行异常: {}. 原因: {}", methodName, e.getMessage(), e);
            throw e;
        }
    }
}