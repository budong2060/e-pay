package com.pay.logic.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.client.NotifyClient;
import com.pay.client.result.NotifyResult;
import com.pay.client.vo.NotifyVo;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayPayment;
import com.pay.logic.result.PayResult;
import com.pay.mybatis.PayPaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import util.BeanUtil;

/**
 * Created by admin on 2017/7/18.
 */
public class PayNotifyNode extends AbstractNode<BaseDomain, PayResult> {

    private static final Logger logger = LoggerFactory.getLogger(PayNotifyNode.class);

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Autowired
    private NotifyClient notifyClient;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain baseDomain) {
        PayPayment domain = (PayPayment) baseDomain;
        try {
            NotifyVo notifyVo = new NotifyVo();
            BeanUtil.copyProperties(domain, notifyVo);
            NotifyResult result = notifyClient.notify(domain.getNotifyUrl(), notifyVo);
            if ("SUCCESS".equals(result.getReturnCode())) {
                domain.setNotifyCode(1);
            }
        } catch (Exception e) {}
        payPaymentMapper.update(domain);
    }

}
