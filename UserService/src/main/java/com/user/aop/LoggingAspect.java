package com.user.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAspect {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Pointcut(value="execution(* com.user.service.*.*(..) )")
	public void servicePointcut() {
	}


	
	@Around("servicePointcut()")
	public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().toString();
		Object[] array = pjp.getArgs();
		log.info("Method_invoked -> "  + methodName +" "+ " Class_Name -> " + " " + className +" Request object Arguments ->  " + " "
				+ mapper.writeValueAsString(array));
		Object object = pjp.proceed();
		log.info("Method Name -> "+methodName + "  Class_Name -> " + " " + className + "  Response  Object -> " + " " + mapper.writeValueAsString(object));
		return object;
	}
	
	@AfterThrowing(pointcut = "servicePointcut() ", throwing = "ex")
	public void logAfterThrowing(JoinPoint pjp,Throwable ex) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().toString();
		Object[] array = pjp.getArgs();
		log.error("Method_invoked -> "  + methodName +" "+ " Class_Name -> " + " " + className +" Request object Arguments ->  " + " "
				+ mapper.writeValueAsString(array) +" "+"Cause "+ex.getCause()+" ");
	}
	
	 
}
