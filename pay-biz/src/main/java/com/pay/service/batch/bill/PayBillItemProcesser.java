package com.pay.service.batch.bill;

import com.pay.domain.PayBillItem;
import com.pay.domain.PayPayment;
import com.pay.mybatis.PayPaymentMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by admin on 2016/7/29.
 */
public class PayBillItemProcesser implements ItemProcessor<PayBillItem, PayBillItem> {

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Override
    public PayBillItem process(PayBillItem item) throws Exception {
        if (null != item && StringUtils.hasLength(item.getTradeNo())) {
            PayPayment payPayment = payPaymentMapper.findByTradeNo(item.getTradeNo());
            if (payPayment != null) {
                item.setOrderNo(payPayment.getOrderNo());
                item.setOrderType(payPayment.getOrderType());
                item.setPayWay(payPayment.getPayWay());
            }
        }
        return item;
    }
}
