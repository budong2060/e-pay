package com.pay.mybatis;

import com.pay.domain.PayPayment;
import com.pay.domain.PayRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 */
@Repository
public interface PayRecordMapper extends BaseMapper<PayPayment> {
    /**
     *
     * @param record
     * @return
     */
    int count(@Param("record") PayRecord record);

    /**
     *
     * @param record
     * @param page
     * @param size
     * @return
     */
    List<PayRecord> find(@Param("record") PayRecord record, @Param("page")int page, @Param("size")int size);

}

