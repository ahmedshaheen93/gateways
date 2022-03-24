package com.shaheen.gateways.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Arrays;

@Aspect
@Order(1)
@ControllerAdvice
@Slf4j
public class LoggerAdvice {
    @Pointcut(value = "execution(* com.shaheen.gateways.controller..*(..))")
    public void loggerAdvicePointCuts() {
        // empty body because its a reference to point cut
    }

    @Around("loggerAdvicePointCuts()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // before advice
        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        String parameters = Arrays.toString(joinPoint.getArgs());
        log.info("START:{}.{}({})", controllerName, methodName, parameters);
        Object result;
        try {
            // proceed method Or not depend on need
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Exception exception) {
            //AfterThrowing
            log.error("EXCEPTION: {}", exception.getMessage(), exception);
            throw exception;
        }
        // after returning
        if (result != null) {
            log.info("END:{}", result);
        }
        return result;
    }
}

