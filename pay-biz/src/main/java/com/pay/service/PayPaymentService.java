package com.pay.service;

import com.pay.common.Pager;
import com.pay.domain.PayPayment;
import com.pay.biz.handler.result.PayResult;

/**
 * Created by admin on 2017/7/5.
 */
public interface PayPaymentService {
    /**
     * 预支付
     * @param payPayment
     */
    PayResult prepay(PayPayment payPayment);

    /**
     * 第三方通知回调处理
     * @param payPayment
     * @return
     */
    PayResult finishPay(PayPayment payPayment);

    /**
     * 更新
     * @param payPayment
     * @return
     */
    PayResult update(PayPayment payPayment);

    /**
     * 根据orderNo查询
     * @param orderNo
     * @param mchId
     * @return
     */
    PayResult<PayPayment> queryByOrderNo(String orderNo, String mchId);

    /**
     * 根据条件查询
     * @param payPayment
     * @return
     */
    Pager<PayPayment> query(PayPayment payPayment, int pageNum, int pageSize);

}
