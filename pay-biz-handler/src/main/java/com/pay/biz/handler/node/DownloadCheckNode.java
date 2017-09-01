package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayBillItem;
import com.pay.mybatis.PayPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import util.AssertUtil;

/**
 * Created by admin on 2017/7/5.
 */
public class DownloadCheckNode extends AbstractNode<BaseDomain, PayResult> {

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain baseDomain) {
        PayBillItem domain = (PayBillItem) baseDomain;
        AssertUtil.hasText(domain.getMchId(), "商户号不能为空");
        AssertUtil.notNull(domain.getBillDate(), "账单日不能为空");
    }
}
