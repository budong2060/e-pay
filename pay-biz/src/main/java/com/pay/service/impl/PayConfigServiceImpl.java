package com.pay.service.impl;

import com.pay.common.Pager;
import com.pay.domain.PayConfig;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.mybatis.PayConfigMapper;
import com.pay.service.PayConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Created by admin on 2016/7/7.
 */
@Service
public class PayConfigServiceImpl implements PayConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayConfigServiceImpl.class);

    @Autowired
    private PayConfigMapper payConfigMapper;

    @CachePut(value = "guava", key = "#payConfig.mchId + #payConfig.payWay + #payConfig.payScene")
    @Transactional
    @Override
    public PayConfig save(PayConfig payConfig) {
        payConfigMapper.save(payConfig);
        return payConfig;
    }

    @CacheEvict(value = "guava", key = "#payConfig.mchId + #payConfig.payWay + #payConfig.payScene")
    @Transactional
    @Override
    public PayConfig update(PayConfig payConfig) {
        PayConfig config = get(payConfig.getId());
        if(null == config) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT);
        }
        payConfigMapper.update(payConfig);
        return payConfig;
    }

    @Override
    public PayConfig get(int id) {
        return payConfigMapper.get(id);
    }

    @Override
    public Integer delete(Integer id) {
        PayConfig config = get(id);
        if(null == config) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT);
        }
        config.setStatus(0);
        update(config);
        return id;
    }

    @CacheEvict(value = "guava", key = "#mchId + #payWay + #payScene")
    @Override
    public PayConfig getOnly(String mchId, Integer payWay, Integer status, Integer payScene) {
        return payConfigMapper.findOnly(mchId, payWay, status, payScene);
    }

    @Override
    public Pager<PayConfig> getByPage(String mchId, int page, int size) {
        long totalCount = payConfigMapper.count(mchId);
        if(totalCount > 0) {
            return new Pager<>(page, size, totalCount, payConfigMapper.findByPage(mchId, (page - 1) * size, size));
        }
        return new Pager<>(page, size);
    }
}
