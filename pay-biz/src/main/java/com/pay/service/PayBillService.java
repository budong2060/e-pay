package com.pay.service;

import com.pay.common.Pager;
import com.pay.domain.PayBillItem;
import com.pay.domain.PayConfig;

import java.util.Date;

/**
 * Created by admin on 2016/8/17.
 */
public interface PayBillService {
    /**
     * 拉取第三方支付账单数据
     * @param startDate
     * @param endDate
     * @param accountNo
     */
    void downLoad(Date startDate, Date endDate, String accountNo);

    /**
     * 分页查询
     * @param unitId
     * @param payWay
     * @param page
     * @param size
     * @return
     */
    Pager<PayBillItem> query(String unitId, Integer[] payWay, Integer[] orderType, Date billDate, Integer page, Integer size);

    /**
     * 对账处理
     * @param mchId
     * @param billDate
     */
    void execBalance(String mchId, Date billDate);

    /**
     *
     * @param billDate
     * @param payConfig
     */
    void execBalance(Date billDate, PayConfig payConfig);
}
