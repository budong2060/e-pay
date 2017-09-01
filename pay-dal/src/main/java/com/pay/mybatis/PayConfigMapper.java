package com.pay.mybatis;

import com.pay.domain.PayConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 */
@Repository
public interface PayConfigMapper extends BaseMapper<PayConfig> {

    PayConfig findOnly(@Param("mchId") String mchId, @Param("payWay") Integer payWay, @Param("status") Integer status, @Param("payScene") Integer payScene);

    /**
     * 根据条件统计
     * @param mchId
     * @return
     */
    long count(@Param("mchId") String mchId);

    /**
     * 根据条件分页
     * @param mchId
     * @param page
     * @param size
     * @return
     */
    List<PayConfig> findByPage(@Param("mchId") String mchId, @Param("page")int page, @Param("size")int size);

    /**
     * 查询需要对账的账户
     * @param mchId
     * @return
     */
    List<PayConfig> findByBill(@Param("mchId") String mchId);
}
