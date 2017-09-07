package com.pay.test.controller;

import com.pay.domain.PayPayment;
import com.pay.test.ApplicationTest;
import org.junit.Test;
import util.AssertUtil;
import util.JsonUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2017/9/6.
 */
public class PayPaymentControllerTest extends ApplicationTest {

    @Test
    public void test() throws Exception {
        String orderNo = String.valueOf(Math.random() * 1000000);
        PayPayment payPayment = new PayPayment();
        payPayment.setMchId("999999");
        payPayment.setUserId("123456");
        payPayment.setOrderNo(orderNo);
        payPayment.setOrderType(1);
        payPayment.setPayWay(16);
        payPayment.setTradeDesc("支付测试 " + orderNo);
        payPayment.setTradeTime(new Date());
        payPayment.setNotifyUrl("www.pay.com");
        payPayment.setTradeAmount(new BigDecimal(20));
        payPayment.setThirdTradeAmount(new BigDecimal(20));

        String result = postWithJson("/pay", JsonUtil.toString(payPayment));
        AssertUtil.notNull(result, "data null");
    }


}
