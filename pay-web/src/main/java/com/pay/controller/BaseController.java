package com.pay.controller;

import com.pay.client.WxPayClient;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.biz.handler.result.PayResult;
import com.pay.service.PayAccountService;
import com.pay.service.PayPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Halbert on 2017/7/1.
 */
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private PayAccountService payAccountService;

    @Autowired
    private PayPaymentService payPaymentService;

    @Autowired
    private WxPayClient wxPayClient;

//    @RequestMapping("/test")
    public Object test() {
        Map<Object, Object> map = new HashMap<Object, Object>(){{
            put("test", "test");
            put("date", new Date());
        }};
//        PayAccount account = new PayAccount();
//        account.setAccountId("halbert");
//        account.setUserId("hyb");
//        account.setUserName("halbert");
//        account = payAccountService.save(account);
//        logger.info("test=================================={}", account);
        return map;
    }

//    @RequestMapping("/paytest")
    public Object pay() {
        PayPayment payPayment = new PayPayment();
        payPayment.setMchId("999999");
        payPayment.setOrderNo("2017071414310000");
        payPayment.setOrderType(1);
        payPayment.setPayWay(16);
        payPayment.setNotifyUrl("http://www.payjava.com");
        payPayment.setTradeAmount(new BigDecimal(20.00));
        payPayment.setTradeDesc("支付测试");
        payPayment.setTradeTime(new Date());
        payPayment.setUserId("100000");
        payPayment.setRequestId("127.0.0.1");
        PayResult result = payPaymentService.prepay(payPayment);
        return result;
    }

//    @RequestMapping("/pay1")
    public Object pay1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.sendRedirect("/pay2");
        return "test11122222";
    }

//    @RequestMapping("/pay2")
    public Object pay2(HttpServletRequest request, HttpServletResponse response) {

        return "test111";
    }

    /**
     *
     * @param response
     * @param obj
     */
    public void writer(HttpServletResponse response, String obj) {
        PrintWriter pw = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            pw = response.getWriter();
            pw.print(obj);
            pw.flush();
        } catch (IOException e) {
            logger.error(">>PrintWriter写入数据失败，原因", e);
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }

     /**
     * 参数校验异常处理
     * @param error
     * @return
     */
     @ExceptionHandler(MethodArgumentNotValidException.class)
     public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
         PayResult result = new PayResult();
         result.setResultEnum(PayResultEnum.ILLEGAL_ARGUMENTS);
         result.setMessage(error.getMessage());
         BindingResult bindingResult = error.getBindingResult();
         if(null != bindingResult) {
             FieldError fieldError = bindingResult.getFieldError();
             if(null != fieldError) {
                 result.setMessage(fieldError.getDefaultMessage());
             }
         }
         return result;
     }

}






















