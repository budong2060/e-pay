package com.pay.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 *
 */
public class AllInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AllInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {

		MDC.put("log-key", UUID.randomUUID().toString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
						   HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
		System.out.println("===================================================");
		logger.info(">>=======================================================");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		MDC.remove("log-key");
	}
	

}
