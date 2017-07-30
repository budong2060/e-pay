package com.pay.client.impl;

import com.pay.Constants;
import com.pay.client.NotifyClient;
import com.pay.client.result.NotifyResult;
import com.pay.client.vo.NotifyVo;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import util.PropertiesUtil;
import util.XMLUtil;

/**
 * Created by admin on 2017/7/18.
 */
public class NotifyClientImpl implements NotifyClient {

    private final Logger logger = LoggerFactory.getLogger(NotifyClientImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public NotifyResult notify(String url, NotifyVo notifyVo) {
        try {
            logger.info(">>支付回调业务请求参数:{}", notifyVo);
            NotifyResult result = restTemplate.postForObject(url, notifyVo, NotifyResult.class);
            logger.info(">>支付回调业务请求结果:{}", result);
            return result;
        } catch (Exception e) {
            logger.error(">>支付回调业务请求异常,原因:{}", e);
            throw new PayException(PayResultEnum.NETWORK_EXCEPTION);
        }
    }
}
