package com.pay.controller;

import com.pay.domain.PayRefund;
import com.pay.service.PayPaymentService;
import com.pay.service.impl.PayRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by admin on 2017/7/20.
 * 退款相关controller
 */
@RestController
public class PayRefundController extends BaseController {

    @Autowired
    private PayRefundService payRefundService;

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public Object pay(@Valid @RequestBody PayRefund payRefund) {
        return payRefundService.applyRefund(payRefund);
    }

}
