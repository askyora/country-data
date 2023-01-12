package com.yora.graalvm.config;


import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnExpression("${process-time-log.enabled:true}")
public class ProcessTimeLoggerAdvice {

    protected static Logger log = LogManager.getLogger(ProcessTimeLoggerAdvice.class);

    @Around("within(com.yora.graalvm.web.*)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = createStopWatch(point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        Object object = point.proceed();
        logExecution(point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), stopWatch);
        return object;
    }

    private void logExecution(String name, String method, StopWatch stopWatch) {
        stopWatch.stop();
        log.info("{}.{} Ended in : {}.", name, method, stopWatch.formatTime());
    }


    private StopWatch createStopWatch(String name, String method) {
        StopWatch stopWatch = StopWatch.createStarted();
        log.info("{}.{} Started. {} ", name, method, stopWatch.getStartTime());
        return stopWatch;
    }
}
