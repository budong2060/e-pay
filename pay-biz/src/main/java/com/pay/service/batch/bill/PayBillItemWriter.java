package com.pay.service.batch.bill;

import com.pay.domain.PayBillItem;
import com.pay.mybatis.PayBillItemMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2016/8/17.
 */
public class PayBillItemWriter implements ItemWriter<PayBillItem> {

    @Autowired
    private PayBillItemMapper payBillItemMapper;

    @Override
    public void write(List items) throws Exception {
        if(null != items && items.size() > 0) {
            payBillItemMapper.batchInsert(items);
        }
    }
}
