package com.pay.service.impl;

import com.pay.domain.PayRefund;
import com.pay.logic.result.PayResult;

/**
 * Created by admin on 2017/7/5.
 */
public interface PayRefundService {
    /**
     *
     * @param refund
     */
    PayResult applyRefund(PayRefund refund);

}
