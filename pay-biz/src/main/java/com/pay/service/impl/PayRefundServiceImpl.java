package com.pay.service.impl;

import com.framework.process.SimpleJob;
import com.pay.common.Pager;
import com.pay.domain.PayRefund;
import com.pay.biz.handler.result.PayResult;
import com.pay.mybatis.PayRefundMapper;
import com.pay.service.PayRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by admin on 2017/7/20.
 *
 * 退款业务
 */
@Service
public class PayRefundServiceImpl implements PayRefundService {

    @Resource(name = "refundJob")
    private SimpleJob refundJob;

    @Autowired
    private PayRefundMapper payRefundMapper;

    @Transactional
    @Override
    public PayResult applyRefund(PayRefund refund) {
        PayResult result = (PayResult) refundJob.execute(refund);
        return result;
    }

    @Override
    public Pager<PayRefund> query(PayRefund refund, int pageNum, int pageSize) {
        long count = payRefundMapper.count(refund);
        List<PayRefund> list = null;
        if (count > 0) {
            list = payRefundMapper.query(refund, (pageNum - 1) * pageSize, pageSize);
        }
        return new Pager<>(pageNum, pageSize, count, list);
    }
}
