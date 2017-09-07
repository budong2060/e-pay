package com.pay.service.impl;

import com.pay.common.Pager;
import com.pay.domain.PayRecord;
import com.pay.service.PayRecordService;

/**
 * Created by admin on 2017/9/5.
 */
public class PayRecordServiceImpl implements PayRecordService {

    @Override
    public Pager<PayRecord> findByUser(String userId, String userType, int pageNum, int pageSize) {
        return null;
    }
}
