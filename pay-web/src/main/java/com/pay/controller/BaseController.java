package com.pay.controller;

import com.pay.domain.PayAccount;
import com.pay.domain.PayPayment;
import com.pay.logic.result.PayResult;
import com.pay.service.PayAccountService;
import com.pay.service.PayPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Halbert on 2017/7/1.
 */
@RestController
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private PayAccountService payAccountService;

    @Autowired
    private PayPaymentService payPaymentService;

    @RequestMapping("/test")
    public Object test() {
        Map<Object, Object> map = new HashMap<Object, Object>(){{
            put("test", "test");
            put("date", new Date());
        }};
        PayAccount account = new PayAccount();
        account.setAccountId("halbert");
        account.setUserId("hyb");
        account.setUserName("halbert");
        account = payAccountService.save(account);
        logger.info("test=================================={}", account);
        return map;
    }

    @RequestMapping("/pay")
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

    @RequestMapping("/pay1")
    public Object pay1() {
        PayPayment payPayment = new PayPayment();
        payPayment.setMchId("999999");
        payPayment.setOrderNo("201707141431012130");
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

}






















