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

		String traceId = UUID.randomUUID().toString().replace("-", "");
		MDC.put("log-key", traceId);
		logger.info(">>收到请求，请求地址:{}", request.getRequestURI());

		String authorization = request.getHeader("Authorization");
		logger.info(">>收到请求，请求authorization:{}", authorization);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
						   HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		MDC.remove("log-key");
	}
	

}
