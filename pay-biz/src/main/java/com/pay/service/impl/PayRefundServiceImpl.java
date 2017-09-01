package com.pay.service.impl;

import com.framework.process.SimpleJob;
import com.pay.domain.PayRefund;
import com.pay.biz.handler.result.PayResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by admin on 2017/7/20.
 *
 * 退款业务
 */
@Service
public class PayRefundServiceImpl implements PayRefundService {

    @Resource(name = "refundJob")
    private SimpleJob refundJob;

    @Transactional
    @Override
    public PayResult applyRefund(PayRefund refund) {
        PayResult result = (PayResult) refundJob.execute(refund);
        return result;
    }
}
