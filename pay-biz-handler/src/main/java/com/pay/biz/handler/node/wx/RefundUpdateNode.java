package com.pay.biz.handler.node.wx;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayRefund;
import com.pay.enums.PayResultEnum;
import com.pay.biz.handler.result.PayResult;
import com.pay.mybatis.PayRefundMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/7/20.
 * 更新退款订单状态
 */
public class RefundUpdateNode extends AbstractNode<BaseDomain, PayResult> {

    private final Logger logger = LoggerFactory.getLogger(RefundUpdateNode.class);

    @Autowired
    private PayRefundMapper payRefundMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        PayRefund refund = (PayRefund) domain;
        //try catch目的：不因数据更新失败回滚而影响原来的执行
        PayResult result = new PayResult();
        try {
            payRefundMapper.update(refund);
            result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
            result.setData(refund);
        } catch (Exception e) {
            logger.error(">>更新退款订单失败，原因:{}", e);
            result.setMessage("微信退款成功，但商户状态更新失败");
            result.setResultEnum(PayResultEnum.REFUND_FAIL);
        }
        context.setResult(result);
        return;
    }
}
