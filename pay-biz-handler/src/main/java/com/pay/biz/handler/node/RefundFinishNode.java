package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayRefund;
import com.pay.enums.PayResultEnum;
import com.pay.enums.RefundStatus;
import com.pay.mybatis.PayRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/8/19.
 */
public class RefundFinishNode extends AbstractNode<BaseDomain, PayResult> {

    @Autowired
    private PayRefundMapper payRefundMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        PayRefund refund = (PayRefund) domain;
        payRefundMapper.save(refund);
        PayResult result = new PayResult();
        if (refund.getRefundStatus() == RefundStatus.REFUND_SUCCESS.code()) {
            result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
        } else {
            result.setResultEnum(PayResultEnum.REFUND_FAIL);
            result.setMessage(refund.getRefundDesc());
        }
        result.setData(refund);
        context.setResult(result);
        return;
    }
}
