package com.pay.biz.handler.node.wx;

import com.pay.Constants;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.enums.PayWay;
import com.pay.exception.PayException;
import org.springframework.util.StringUtils;
import util.AssertUtil;

import java.util.Map;

/**
 * Created by admin on 2017/7/25.
 * 微信公众号
 */
public class WXPublicPayNode extends WXPayNode {

    @Override
    protected Map<String, String> buildParams(Map<String, String> config, PayPayment payPayment) {
        if (payPayment.getPayWay() == PayWay.WX_PAY.code()) {
            AssertUtil.hasText(payPayment.getCode(), "微信授权code不能为空");
        }
        Map<String, String> params = super.buildParams(config, payPayment);
        //设置code
        if(StringUtils.hasLength(payPayment.getCode())) {
            params.put("openid", getOpenId(config, payPayment.getCode()));
        }
        return params;
    }


    /**
     * 获取微信OpenId
     * @return
     */
    private String getOpenId(Map<String, String> config, String code) {
        Map<String, String> result = wxPayClient.accessToken(config.get(Constants.WX_APPID), config.get(Constants.APPSECRET), code);
        String openId = result.get("openid");
        if (StringUtils.isEmpty(openId)) {
            throw new PayException(PayResultEnum.PAY_PREPAY_FAIL, "获取微信openid失败");
        }
        return openId;
    }
}
