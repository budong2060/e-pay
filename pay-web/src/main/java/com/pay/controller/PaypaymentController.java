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
     * 根据订单号和类型查询
     * @return
     */
    @RequestMapping(value = "/pay/{orderNo}/{orderType}", method = RequestMethod.GET)
    public Object queryByOrderNo(@PathVariable("orderNo") String orderNo, @PathVariable Integer orderType) {

        return null;
    }

    /**
     * 根据商户流水号查询
     * @return
     */
    @RequestMapping(value = "/pay/{tradeNo}", method = RequestMethod.GET)
    public Object queryTradeNo(@PathVariable("tradeNo") String tradeNo) {

        return null;
    }

}
