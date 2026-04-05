package com.ThreadPractise.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditLoggingAspect.class);

    @Around("execution(* com.example.ordermgmt.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        log.info("Entering method: {} with args: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("Exiting method: {} in {} ms with result: {}",
                    joinPoint.getSignature().toShortString(),
                    duration,
                    result);

            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;

            log.error("Exception in method: {} after {} ms. Message: {}",
                    joinPoint.getSignature().toShortString(),
                    duration,
                    ex.getMessage());

            throw ex;
        }
    }
}
