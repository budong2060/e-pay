package com.pay.biz.handler.node.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.pay.Constants;
import com.pay.biz.handler.node.BaseRefundNode;
import com.pay.domain.PayRefund;
import com.pay.enums.PayResultEnum;
import com.pay.enums.RefundStatus;
import com.pay.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PropertiesUtil;

import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 2017/9/4.
 * 支付宝退款请求
 */
public class AliRefundNode extends BaseRefundNode {

    private final Logger logger = LoggerFactory.getLogger(AliRefundNode.class);

    @Override
    protected Object doProcess(Map<String, String> config, PayRefund refund) {
        String url = PropertiesUtil.getProperty(Constants.ALI_GATEWAY);
        String appid = config.get(Constants.ALI_APP_ID);
        AlipayClient alipayClient = new DefaultAlipayClient(url, appid, config.get(Constants.ALI_PRIVATEKEY),
                "json", Constants.CHARSET, config.get(Constants.ALI_PUBLICKEY), config.get(Constants.RSA) == null ? "RSA" : config.get(Constants.RSA));
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutRequestNo(refund.getRefundNo());
        model.setTradeNo(refund.getThirdTradeNo());
        model.setOutTradeNo(refund.getTradeNo());
        model.setRefundAmount(refund.getTotalAmount().toString());
        model.setRefundReason(refund.getRefundDesc());
        request.setBizModel(model);

        try {
            logger.info(">>支付宝退款申请入参，appid:{}, tradeNo:{}, outTradeNo:{}, outRequestNo:{}, refundAmount:{}",
                    appid, model.getTradeNo(), model.getOutTradeNo(), model.getOutRequestNo(), model.getRefundAmount());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            logger.info(">>支付宝退款申请结果:是否成功:{}, 描述:{}", response.isSuccess(), response.getMsg());
            if (response.isSuccess()) {
                refund.setRefundStatus(RefundStatus.REFUND_SUCCESS.code());
                refund.setRefundFinishTime(new Date());
            } else {
                refund.setRefundStatus(RefundStatus.REFUND_FAIL.code());
                refund.setRefundDesc(refund.getRefundDesc() + "【" + response.getMsg() + "】");
            }
        } catch (AlipayApiException e) {
            logger.error(">>发起支付宝申请失败，原因:", e);
            throw new PayException(PayResultEnum.REFUND_FAIL, "支付宝退款申请失败");
        }
        return refund;
    }
}
