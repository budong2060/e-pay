package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayPayment;
import com.pay.domain.PayRefund;
import com.pay.enums.PayResultEnum;
import com.pay.enums.RefundStatus;
import com.pay.enums.TradeStatus;
import com.pay.exception.PayException;
import com.pay.mybatis.PayPaymentMapper;
import com.pay.mybatis.PayRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import util.IdUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2017/7/19.
 */
public class RefundCheckNode extends AbstractNode<BaseDomain, PayResult> {

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Autowired
    private PayRefundMapper payRefundMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        PayRefund refund = (PayRefund) domain;
        PayPayment payPayment = payPaymentMapper.findByTradeNo(refund.getTradeNo());
        if (null == payPayment || TradeStatus.TRADE_SUCCESS.code() != payPayment.getTradeStatus()) {
            throw new PayException(PayResultEnum.INVALID_TRADE_NO);
        }
        List<PayRefund> refunds = payRefundMapper.findByTradeNo(refund.getTradeNo());
        if (sumRefundAmount(refunds).compareTo(payPayment.getThirdTradeAmount()) > 0) {
            throw new PayException(PayResultEnum.REFUND_AMOUNT_OVERDUE);
        }
        refund.setTradeNo(IdUtils.genId());
        refund.setUserId(payPayment.getUserId());
        refund.setPayWay(payPayment.getPayWay());
        refund.setThirdTradeNo(payPayment.getThirdTradeNo());
        refund.setRefundStatus(RefundStatus.REFUND_APPLY.code());
        payRefundMapper.save(refund);
    }

    /**
     * 统计所有的退款金额
     * @param refunds
     * @return
     */
    private BigDecimal sumRefundAmount(List<PayRefund> refunds) {
        BigDecimal totalAmount = new BigDecimal(0.00);
        for (PayRefund refund : refunds) {
            totalAmount = totalAmount.add(refund.getRefundAmount());
        }
        return totalAmount;
    }
}
