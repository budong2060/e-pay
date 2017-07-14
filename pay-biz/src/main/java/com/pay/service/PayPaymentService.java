package com.pay.service;

import com.pay.domain.PayPayment;
import com.pay.logic.result.PayResult;

/**
 * Created by admin on 2017/7/5.
 */
public interface PayPaymentService {
    /**
     *
     * @param payPayment
     */
    PayResult prepay(PayPayment payPayment);

}
