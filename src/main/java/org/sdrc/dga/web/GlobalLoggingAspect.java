/**
 * 
 */
package org.sdrc.dga.web;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * Created on 26-Nov-2018
 */
@Aspect
public class GlobalLoggingAspect implements ThrowsAdvice {

	private static final Logger logger = LoggerFactory.getLogger(GlobalLoggingAspect.class);

	  @AfterThrowing(pointcut = "execution(* org.sdrc.dga.*.*.*(..))", throwing = "e")
	  public void afterThrowing(JoinPoint joinPoint, Throwable e) {

		  if(!(e instanceof BadCredentialsException || e instanceof AccessDeniedException))
		  {
	    Signature signature = joinPoint.getSignature();
	    String methodName = signature.getName();
	    CodeSignature codeSignature = (CodeSignature) signature;
	    
	    String argumentSignature =" with arguments ";
	    
	    for(int i=0;i<joinPoint.getArgs().length;i++)
	    {
	    	argumentSignature +=codeSignature.getParameterNames()[i]+" : "+joinPoint.getArgs()[i]+", ";
	    	
	    }
	    logger.error("We have caught exception in method: "
	        + methodName + "\n "+argumentSignature +" in class :"+signature.getDeclaringTypeName()+ "\n the exception is: "
	        + e.getMessage(), e);
	    
	    
		  }
	  }
}