package com.pay.service;

import com.pay.common.Pager;
import com.pay.domain.PayRecord;

/**
 * Created by admin on 2017/9/5.
 */
public interface PayRecordService {
    /**
     * 交易记录
     * @param userId
     * @param userType
     * @param pageNum
     * @param pageSize
     * @return
     */
    Pager<PayRecord> findByUser(String userId, String userType, int pageNum, int pageSize);

}
