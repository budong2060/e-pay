package com.pay.biz.handler.node.wx;

import com.pay.Constants;
import com.pay.biz.handler.node.BaseRefundNode;
import com.pay.client.WxPayClient;
import com.pay.domain.PayRefund;
import com.pay.enums.RefundStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import util.WXUtil;
import util.XMLUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by admin on 2017/7/19.
 * 微信退款节点
 */
public class WXRefundNode extends BaseRefundNode {

    private final Logger logger = LoggerFactory.getLogger(WXRefundNode.class);

    @Autowired
    private WxPayClient wxPayClient;

    @Override
    protected Object doProcess(Map<String, String> config, PayRefund refund) {
        String requestXml = genRequestXml(config, refund);
        Map<String, String> refundResult = wxPayClient.doRefund(config.get(Constants.CERT_PATH), config.get(Constants.CERT_PWD), requestXml);
        if (!"SUCCESS".equals(refundResult.get("result_code"))) {
//            throw new PayException(PayResultEnum.REFUND_FAIL, refundResult.get("err_code_des"));
            refund.setRefundStatus(RefundStatus.REFUND_FAIL.code());
            refund.setRefundDesc(refund.getRefundDesc() + "【" + refundResult.get("err_code_des") + "】");
        } else {
            String refundId = refundResult.get("refund_id");
            refund.setRefundStatus(RefundStatus.REFUND_SUCCESS.code());
            refund.setThirdRefundNo(refundId);
            refund.setRefundFinishTime(new Date());
        }
        return refund;
    }

    /**
     * 构造微信退款请求xml
     * @param config
     * @param refund
     * @return
     */
    private String genRequestXml(Map<String, String> config, PayRefund refund) {
        SortedMap<String, String> map = new TreeMap<>();
        String nonceStr = WXUtil.getNonceStr();
        map.put("appid", config.get(Constants.WX_APPID));
        map.put("mch_id", config.get(Constants.WX_MCH_ID));
        map.put("nonce_str", nonceStr);
        map.put("out_trade_no", refund.getTradeNo());
        map.put("transaction_id", refund.getThirdTradeNo());
        map.put("out_refund_no", refund.getRefundNo());
        //计算金额，元转分
        BigDecimal refundAmount = refund.getRefundAmount();
        int refundFee = refundAmount.multiply(new BigDecimal(100)).intValue();
        map.put("refund_fee", String.valueOf(refundFee));
        BigDecimal totalAmount = refund.getTotalAmount();
        int totalFee = totalAmount.multiply(new BigDecimal(100)).intValue();
        map.put("total_fee", String.valueOf(totalFee));
        map.put("sign", WXUtil.getSign(map, config.get(Constants.WX_PARTNER_KEY)));
        return XMLUtil.getParamXml(map);
    }

}
