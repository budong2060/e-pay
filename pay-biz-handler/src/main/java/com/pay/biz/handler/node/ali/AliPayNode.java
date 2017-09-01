package com.pay.biz.handler.node.ali;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayConfig;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.mybatis.PayConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by admin on 2017/8/29.
 * alipay相关文档地址
 * https://docs.open.alipay.com/270/106759
 */
public abstract class AliPayNode extends AbstractNode<BaseDomain, PayResult> {

    private final Logger logger = LoggerFactory.getLogger(AliPayNode.class);

    @Autowired
    private PayConfigMapper payConfigMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain baseDomain) {
        PayResult result = new PayResult();
        PayPayment domain = (PayPayment) baseDomain;
        PayConfig payConfig = payConfigMapper.findOnly(domain.getMchId(), domain.getPayWay(), 1, 1);
        if(null == payConfig) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT, "第三方账户不存在");
        }
        Map<String, String> config = payConfig.getConfig();
        result.setData(doProcess(config, domain));
        result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
        context.setResult(result);
    }

    protected abstract Object doProcess(Map<String, String> config, PayPayment payPayment);

}
