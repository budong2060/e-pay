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
public class PayPaymentController extends BaseController {

    @Autowired
    private PayPaymentService payPaymentService;

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Object pay(@Valid @RequestBody PayPayment payPayment) {
        return payPaymentService.prepay(payPayment);
    }

    /**
     * 根据订单号
     * @return
     */
    @RequestMapping(value = "/pay/{mchId}/{orderNo}", method = RequestMethod.GET)
    public Object queryByOrderNo(@PathVariable("orderNo") String orderNo, @PathVariable("mchId") String mchId) {
        return payPaymentService.queryByOrderNo(orderNo, mchId);
    }

}
