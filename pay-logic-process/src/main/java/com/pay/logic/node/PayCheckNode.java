package com.pay.logic.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.enums.PayWay;
import com.pay.enums.TradeStatus;
import com.pay.exception.PayException;
import com.pay.logic.result.PayResult;
import com.pay.mybatis.PayPaymentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import util.AssertUtil;
import util.BeanUtil;
import util.IdUtils;

/**
 * Created by admin on 2017/7/5.
 */
public class PayCheckNode extends AbstractNode<BaseDomain, PayResult> {

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain baseDomain) {
        PayPayment domain = (PayPayment) baseDomain;

        if (domain.getPayWay() == PayWay.WX_PAY.code()) {
            AssertUtil.hasText(domain.getCode(), "微信授权code不能为空");
        }

        domain.setTradeStatus(TradeStatus.TRADE_PROCEEDING.code());
        //校验当前订单是否已经支付过
        PayPayment payPayment = payPaymentMapper.findByOrderNo(domain.getOrderNo(), domain.getMchId());
        if (null != payPayment) {
            if (TradeStatus.TRADE_SUCCESS.code() == payPayment.getTradeStatus()) {
                throw new PayException(PayResultEnum.PAY_HAS_FINISH);
            }
            if (TradeStatus.TRADE_CLOSED.code() == payPayment.getTradeStatus()) {
                throw new PayException(PayResultEnum.PAY_FAIL, TradeStatus.TRADE_CLOSED.getMessage());
            }
            BeanUtil.copyProperties(domain, payPayment, true, new String[] { "tradeNo" });
            payPaymentMapper.update(payPayment);
        } else {
            domain.setTradeNo(IdUtils.genId());
            payPaymentMapper.save(domain);
        }
    }
}
