package com.pay.service;

import com.pay.domain.PayPayment;
import com.pay.biz.handler.result.PayResult;

/**
 * Created by admin on 2017/7/5.
 */
public interface PayPaymentService {
    /**
     *
     * @param payPayment
     */
    PayResult prepay(PayPayment payPayment);

    /**
     *
     * @param payPayment
     * @return
     */
    PayResult dealPay(PayPayment payPayment);

}
