package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayConfig;
import com.pay.domain.PayRefund;
import com.pay.enums.PayResultEnum;
import com.pay.enums.RefundStatus;
import com.pay.exception.PayException;
import com.pay.mybatis.PayConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * Created by admin on 2017/7/19.
 * 退款基础节点
 */
public abstract class BaseRefundNode extends AbstractNode<BaseDomain, PayResult> {

    private final Logger logger = LoggerFactory.getLogger(BaseRefundNode.class);

    @Autowired
    private PayConfigMapper payConfigMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        PayRefund refund = (PayRefund) domain;
        PayConfig payConfig = payConfigMapper.findOnly(refund.getMchId(), refund.getPayWay(), 1, 1);
        if(null == payConfig) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT, "第三方账户不存在");
        }
        Map<String, String> config = payConfig.getConfig();
        if (null == config) {
            throw new PayException(PayResultEnum.DATA_EXCEPTION, "第三方账户配置错误");
        }
        try {
            doProcess(config, refund);
        } catch (Exception e) {
            refund.setRefundStatus(RefundStatus.REFUND_EXCEPTION.code());
            logger.error(">>退款申请失败,原因:", e);
        }
    }

    /**
     *
     * @param config
     * @param refund
     * @return
     */
    protected abstract Object doProcess(Map<String, String> config, PayRefund refund);

}
