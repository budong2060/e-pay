package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.enums.TradeStatus;
import com.pay.exception.PayException;
import com.pay.biz.handler.result.PayResult;
import com.pay.mybatis.PayPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import util.BeanUtil;

/**
 * Created by admin on 2017/7/18.
 */
public class PayFinishNode extends AbstractNode<BaseDomain, PayResult> {

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain baseDomain) {
        PayPayment domain = (PayPayment) baseDomain;
        //校验当前订单是否已经支付过
        PayPayment payPayment = payPaymentMapper.findByTradeNo(domain.getTradeNo());
        if (null == payPayment) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT);
        }
        if (payPayment.getTradeStatus() == TradeStatus.TRADE_SUCCESS.code()) {
            context.setDone(true);
            return;
        }

        BeanUtil.copyProperties(domain, payPayment, true);
        context.setDomain(payPayment);
    }
}
