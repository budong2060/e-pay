package com.pay.service.impl;

import com.framework.process.SimpleJob;
import com.pay.domain.PayPayment;
import com.pay.biz.handler.result.PayResult;
import com.pay.service.PayPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by admin on 2017/7/5.
 */
@Service
public class PayPaymentServiceImpl implements PayPaymentService {

    @Resource(name = "payJob")
    private SimpleJob payJob;

    @Transactional
    @Override
    public PayResult prepay(PayPayment payPayment) {
        PayResult result = (PayResult) payJob.execute(payPayment);
        return result;
    }

    @Override
    public PayResult dealPay(PayPayment payPayment) {
        return null;
    }
}
