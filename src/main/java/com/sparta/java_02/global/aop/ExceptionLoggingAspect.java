package com.sparta.java_02.global.aop;

import com.sparta.java_02.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

  @Pointcut("execution(* com.sparta.java_02.domain..service..*(..))")
  private void allServiceMethods() {
  }

  @AfterThrowing(pointcut = "allServiceMethods()", throwing = "exception")
  public void logServiceException(ServiceException exception) {
    log.error("Service Layer Exception: Code = [{}], Message = [{}]",
        exception.getCode(), exception.getMessage());
  }


}
