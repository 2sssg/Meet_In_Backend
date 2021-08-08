package com.HALEEGO.meetin.AOP;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("@annotation(LogExecution)")
    public Object returnlog(ProceedingJoinPoint joinPoint) throws Throwable{
        ObjectMapper mapper = new ObjectMapper();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("시작 : "+joinPoint.getSignature());
        log.info("입력값 : "+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(joinPoint.getArgs()));

        Object proceed = joinPoint.proceed();

        log.info("return Value : \n"+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(proceed));
        log.info("끝 : "+joinPoint.getSignature());
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return proceed;
    }

}
