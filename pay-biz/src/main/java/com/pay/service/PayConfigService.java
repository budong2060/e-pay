package com.pay.service;


import com.pay.common.Pager;
import com.pay.domain.PayConfig;

import java.util.List;

/**
 * Created by admin on 2016/7/7.
 */
public interface PayConfigService {
    /**
     *
     * @param payConfig
     * @return
     */
    PayConfig save(PayConfig payConfig);

    /**
     *
     * @param id
     * @return
     */
    PayConfig get(int id);

    /**
     *
     * @param payConfig
     * @return
     */
    PayConfig update(PayConfig payConfig);

    /**
     *
     * @param id
     * @return
     */
    Integer delete(Integer id);

    /**
     *
     * @param account
     * @param payWay
     * @return
     */
    PayConfig getOnly(String account, Integer payWay, Integer status, Integer payScene);

    /**
     *
     * @param mchId
     * @param page
     * @param size
     * @return
     */
    public Pager<PayConfig> getByPage(String mchId, int page, int size);
}
