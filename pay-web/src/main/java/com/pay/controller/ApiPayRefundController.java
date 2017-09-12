package com.pay.controller;

import com.pay.biz.handler.result.PayResult;
import com.pay.common.Pager;
import com.pay.domain.PayPayment;
import com.pay.domain.PayRefund;
import com.pay.service.PayRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by admin on 2017/7/20.
 * 退款相关controller
 */
@RestController
public class ApiPayRefundController extends BaseController {

    @Autowired
    private PayRefundService payRefundService;

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public Object refund(@Valid @RequestBody PayRefund payRefund) {
        return payRefundService.applyRefund(payRefund);
    }

    @RequestMapping("/refund/{pageNum}/{pageSize}")
    public Object query(PayRefund payment, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize")int pageSize) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = Pager.DEFAULT_PAGE_NUM;
        }
        Pager<PayRefund> pager = payRefundService.query(payment, pageNum, pageSize);
        return new PayResult<>(pager);
    }

}
