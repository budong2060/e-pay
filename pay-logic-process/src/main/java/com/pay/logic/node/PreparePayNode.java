package com.pay.logic.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.domain.BaseDomain;
import com.pay.logic.result.PayResult;

/**
 * Created by admin on 2017/7/5.
 */
public class PreparePayNode extends AbstractNode<BaseDomain, PayResult> {

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        System.out.print("==============================");
    }
}
