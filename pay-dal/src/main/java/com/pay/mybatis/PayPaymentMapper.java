package com.pay.mybatis;

import com.pay.domain.PayBillItem;
import com.pay.domain.PayPayment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/7/4.
 */
@Repository
public interface PayPaymentMapper extends BaseMapper<PayPayment> {
    /**
     * 根据orderNo mchId查询订单数据
     * @param orderNo
     * @param mchId
     * @return
     */
    PayPayment findByOrderNo(@Param("orderNo") String orderNo, @Param("mchId") String mchId);

    /**
     * 根据tradeno查询支付记录
     * @param tradeNo
     * @return
     */
    PayPayment findByTradeNo(@Param("tradeNo") String tradeNo);

    /**
     *
     * @param mchId
     * @param payChannel
     * @param tradeDate
     * @return
     */
    List<PayBillItem> findByTradeDate(@Param("mchId") String mchId, @Param("payChannel") String payChannel, @Param("tradeDate") Date tradeDate,
                                      @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

}

