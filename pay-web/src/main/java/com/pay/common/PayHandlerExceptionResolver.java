package com.pay.common;

import com.framework.process.exception.JobException;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.biz.handler.result.PayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import util.JsonUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Halbert on 2017/7/2.
 * 统一异常处理类
 */
public class PayHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

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
		logger.error(">>进入异常统一异常处理:{}", ex.getMessage());
		if (request.getHeader("Content-Type").contains("application/json")) {
			PayResult result = new PayResult();
			//处理job异常
			if (ex instanceof JobException) {
				Throwable cause = ex.getCause();
				if (null != cause) {
					ex = cause.getCause() == null ? ex : (RuntimeException) cause.getCause();
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
			ServletOutputStream os = null;
			try {
				response.setContentType("application/json; charset=UTF-8");
				String json = JsonUtil.toString(result);
				os = response.getOutputStream();
				os.print(new String(json.getBytes("UTF-8"), "ISO-8859-1"));
				os.flush();
				return null;
			} catch (Exception e) {
				logger.error(">>统一异常处理IO异常:{}", e);
			} finally {
				try {
					os.close();
				}catch (Exception ee) {}
			}
		}
		return new ModelAndView("error.vm");
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
}

