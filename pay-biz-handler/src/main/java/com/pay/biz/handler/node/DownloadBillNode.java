package com.pay.biz.handler.node;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayBillItem;
import com.pay.domain.PayConfig;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.mybatis.PayConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by admin on 2017/8/23.
 * 第三方支付对账单下载抽象节点
 */
public abstract class DownloadBillNode extends AbstractNode<BaseDomain, PayResult> {

    protected final Logger logger = LoggerFactory.getLogger(DownloadBillNode.class);

    @Autowired
    private PayConfigMapper payConfigMapper;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain domain) {
        PayBillItem payBill = (PayBillItem) domain;
        if (null == payBill.getBillDate()) {
            throw new PayException(PayResultEnum.ILLEGAL_ARGUMENTS, "微信下载账单日不能为空");
        }
        PayConfig payConfig = payConfigMapper.findOnly(payBill.getMchId(), payBill.getPayWay(), 1, 1);
        if(null == payConfig) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT, "第三方账户不存在");
        }
        Map<String, String> config = payConfig.getConfig();
        if(null == config) {
            throw new PayException(PayResultEnum.DATA_EXCEPTION, "账户配置错误");
        }
        logger.info(">>账单数据下载开始......");
        List<PayBillItem> items = doDownLoad(config, payBill);
        logger.info(">>账单数据下载成功,总数量:{}条", null == items ? 0 : items.size());
        context.setResult(new PayResult(items));
        return;
    }

    protected abstract List<PayBillItem> doDownLoad(Map<String, String> config, PayBillItem payBillItem);


}
