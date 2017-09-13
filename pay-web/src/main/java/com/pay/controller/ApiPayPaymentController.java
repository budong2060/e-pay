package com.pay.controller;

import com.pay.biz.handler.result.PayResult;
import com.pay.common.Pager;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.service.PayPaymentService;
import com.pay.vo.PaymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by admin on 2017/7/20.
 */
@RestController
public class ApiPayPaymentController extends BaseController {

    @Autowired
    private PayPaymentService payPaymentService;

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Object pay(@Valid @RequestBody PaymentVo paymentVo) {
        if (!paymentVo.verify("key")) {
            throw new PayException(PayResultEnum.SIGN_VERIFY_FIAL);
        }
        return payPaymentService.prepay(paymentVo.getPayPayment());
    }

    /**
     * 根据订单号
     * @return
     */
    @RequestMapping(value = "/pay/{mchId}/{orderNo}", method = RequestMethod.GET)
    public Object queryByOrderNo(@PathVariable("orderNo") String orderNo, @PathVariable("mchId") String mchId) {
        return payPaymentService.queryByOrderNo(orderNo, mchId);
    }

    /**
     * 用户端查询支付记录
     * @param payment
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/pay/{pageNum}/{pageSize}")
    public Object query(PayPayment payment, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize")int pageSize) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = Pager.DEFAULT_PAGE_NUM;
        }
        Pager<PayPayment> pager = payPaymentService.query(payment, pageNum, pageSize);
        return new PayResult<>(pager);
    }

}
