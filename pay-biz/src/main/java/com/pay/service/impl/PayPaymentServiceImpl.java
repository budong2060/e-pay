package com.pay.service.impl;

import com.framework.process.SimpleJob;
import com.pay.common.Pager;
import com.pay.domain.PayPayment;
import com.pay.biz.handler.result.PayResult;
import com.pay.mybatis.PayPaymentMapper;
import com.pay.service.PayPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by admin on 2017/7/5.
 */
@Service
public class PayPaymentServiceImpl implements PayPaymentService {

    @Resource(name = "payJob")
    private SimpleJob payJob;

    @Resource(name = "finishJob")
    private SimpleJob finishJob;

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Transactional
    @Override
    public PayResult prepay(PayPayment payPayment) {
        PayResult result = (PayResult) payJob.execute(payPayment);
        return result;
    }

    @Override
    public PayResult finishPay(PayPayment payPayment) {
        PayResult result = (PayResult) finishJob.execute(payPayment);
        return result;
    }

    @Override
    public Pager<PayPayment> query(PayPayment payPayment, int pageNum, int pageSize) {
        long count = payPaymentMapper.count(payPayment);
        List<PayPayment> list = null;
        if (count > 0) {
            list = payPaymentMapper.query(payPayment, (pageNum - 1) * pageSize, pageSize);
        }
        return new Pager<>(pageNum, pageSize, count, list);
    }
}
