package com.pay.mybatis;

import com.pay.domain.PayBillItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 */
@Repository
public interface PayBillItemMapper extends BaseMapper<PayBillItem> {

    /**
     * 批量保存数据
     * @param items
     */
    void batchSave(@Param("items") List<PayBillItem> items);


    /**
     * 删除数据
     * @param mchId
     * @param dateTime
     */
    void deleteByDate(@Param("mchId") String mchId, @Param("payWay") Integer payWay, @Param("billDate") String dateTime);

    /**
     *
     * @param mchId
     * @param payWay
     * @return
     */
    int countByMchId(@Param("mchId") String mchId, @Param("payWay") Integer payWay, @Param("billDate")String billDate);

    /**
     *
     * @param mchId
     * @param payWay
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<PayBillItem> pageQuery(@Param("mchId") String mchId, @Param("payWay") Integer payWay, @Param("billDate")String billDate,
                                @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

}
