package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;

/**
 * Created by admin on 2017/7/5.
 */
public class PreparePayNode extends AbstractNode<BaseDomain, PayResult> {

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        System.out.print("==============================");
    }
}
