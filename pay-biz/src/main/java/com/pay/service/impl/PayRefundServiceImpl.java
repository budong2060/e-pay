package com.pay.service.impl;

import com.framework.process.SimpleJob;
import com.pay.domain.PayRefund;
import com.pay.logic.result.PayResult;

import javax.annotation.Resource;

/**
 * Created by admin on 2017/7/20.
 *
 * 退款业务
 */
public class PayRefundServiceImpl implements PayRefundService {

    @Resource(name = "refundJob")
    private SimpleJob refundJob;

    @Override
    public PayResult applyRefund(PayRefund refund) {
        PayResult result = (PayResult) refundJob.execute(refund);
        return result;
    }
}
