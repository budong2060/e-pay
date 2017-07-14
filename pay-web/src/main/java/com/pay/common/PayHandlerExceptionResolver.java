package com.pay.common;

import com.framework.process.exception.JobException;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.logic.result.PayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 *
 */
public class PayHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PayHandlerExceptionResolver.class);

	/**
	 * 统一异常处理，
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @return
	 * @see HandlerExceptionResolver#resolveException(HttpServletRequest,
	 * HttpServletResponse, Object, Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
										 Object handler, Exception ex) {
		logger.error(">>统一异常处理:{}", ex);

//		if (request.getHeader("Content-Type").contains("application/json")) {
			PayResult result = new PayResult();
			//处理job异常
			if (ex instanceof JobException) {
				Throwable cause = ex.getCause();
				if (null != cause) {
					ex = (RuntimeException) cause.getCause();
				}
			}
			if (ex instanceof PayException) {
				PayException pe = (PayException) ex;
				result.setResultEnum(pe.getResultEnum());
				result.setMessage(ex.getMessage());
			} else {
				result.setResultEnum(PayResultEnum.SYSTEM_EXCEPTION);
				result.setMessage(ex.getMessage());
			}
			PrintWriter writer = null;
			try {
				response.setContentType("application/json; charset=UTF-8");
				writer = response.getWriter();
				writer.write(JsonUtil.toString(result));
				writer.flush();
				return null;
			} catch (Exception e) {
				logger.error(">>统一异常处理IO异常:{}", e);
			} finally {
				writer.close();
			}
//		}
		return new ModelAndView("error.vm");
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
}

