package com.pay.service.batch.bill;

import com.pay.domain.PayBillItem;
import com.pay.mybatis.PayBillItemMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by admin on 2016/7/29.
 */
public class PayBillItemProcesser implements ItemProcessor<PayBillItem, PayBillItem> {

    @Autowired
    private PayBillItemMapper payBillItemMapper;

    @Override
    public PayBillItem process(PayBillItem item) throws Exception {

//        if(null != item && !StringUtil.isEmpty(item.getOutTradeNo())) {
//            //支付，补全充值订单号
//            PayPayments payPayments = payPaymentsDao.findByRechargeId(item.getOutTradeNo());
//            if(payPayments  != null){
//                item.setOrderNo(payPayments.getOrderNo());
//                item.setOrderType(payPayments.getOrderType());
//                item.setOutTradeNo(payPayments.getId());
//                //退款
//                if(2 == item.getBillType()) {
//                    List<PayRefund> refunds = payRefundDao.findByOrderNoOrderType(payPayments.getOrderNo(), payPayments.getOrderType());
//                    if(null != refunds && refunds.size() > 0) {
//                        PayRefund refund = refunds.get(0);
//                        item.setCancelOrderNo(refund.getCancelOrderNo());
//                        item.setOutRefundNo(refund.getId());
//                    }
//                }
//            }else {
//            	item.setOrderNo("");
////                item.setOrderType(1);
//                item.setOutTradeNo("");
//            }
//
//            Date date = new Date();
//            item.setCreateTime(date);
//            item.setUpdateTime(date);
//        }
        return item;
    }
}
