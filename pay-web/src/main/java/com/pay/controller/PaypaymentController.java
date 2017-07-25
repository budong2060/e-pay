package com.pay.controller;

import com.pay.domain.PayPayment;
import com.pay.service.PayPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by admin on 2017/7/20.
 */
@RestController
public class PaypaymentController extends BaseController {

    @Autowired
    private PayPaymentService payPaymentService;

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Object pay(@Valid @RequestBody PayPayment payPayment) {
//        payPaymentService.prepay(payPayment);
        return payPaymentService.prepay(payPayment);
    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public Object payNotify() {

        return null;
    }

}
