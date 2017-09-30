package com.pay.mybatis;

import com.pay.domain.PayBillItem;
import com.pay.domain.PayRefund;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/7/4.
 */
@Repository
public interface PayRefundMapper extends BaseMapper<PayRefund> {

    /**
     * 根据tradeno查询支付记录
     * @param tradeNo
     * @return
     */
    List<PayRefund> findByTradeNo(@Param("tradeNo") String tradeNo);

    /**
     * 根据退款商户流水号查询
     * @param refundNo
     * @return
     */
    PayRefund findByRefundNo(@Param("refundNo") String refundNo);

    /**
     *
     * @param mchId
     * @param payChannel
     * @param tradeDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<PayBillItem> findByTradeDate(@Param("mchId") String mchId, @Param("payChannel") String payChannel, @Param("tradeDate") Date tradeDate,
                                      @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

}

