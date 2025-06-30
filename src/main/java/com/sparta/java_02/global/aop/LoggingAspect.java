package com.sparta.java_02.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Pointcut("execution(* com.sparta.java_02.domain..controller..*(..))")
  private void allServiceMethods() {
  }

  @Before("allServiceMethods()")
  public void logBeforeAPiExecution() {
    log.info("[API-execution] API 메서드 실행 전 로그");
  }

  @Before("within(com.sparta.java_02.domain..*)")
  public void logBeforeWithin() {
    log.info("[within] domain 패키지 내부 메서드 실행 전 로그");
  }

  @Before("@annotation(com.sparta.java_02.common.annotation.Loggable)")
  public void logBeforeAnnotation() {
    log.info("[@annotation] @Loggable 어노테이션 적용된 메서드 실행 전 로그");
  }

  @Before("allServiceMethods()")
  public void logMethodDetails(JoinPoint joinpoint) {
    log.info("[JoinPoint 활용] 실행된 메서드 이름 : {}", joinpoint.getSignature().getName());
    Object[] args = joinpoint.getArgs();
    if (args.length > 0) {
      log.info("[JoinPoint 활용] 전달된 파라미터: {}", args);
    }
  }


}
