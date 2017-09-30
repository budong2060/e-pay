package com.pay.service.batch.bill;

import com.pay.enums.PayWay;
import com.pay.mybatis.PayBillItemMapper;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 2016/8/17.
 * 初始化账单表
 */
public class FirstTasklet implements Tasklet {

    @Autowired
    private PayBillItemMapper payBillItemMapper;

    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
            throws Exception {
        Map<String, Object> params = arg1.getStepContext().getJobParameters();
        Date date = (Date) params.get("loadDate");
        String mchId = (String) params.get("mchId");
//        Long payWay = (Long) params.get("payWay");
        String dateTime = DateFormatUtils.format(date, "yyyy-MM-dd");
        PayWay payWay = PayWay.getByCode(((Long) params.get("payWay")).intValue());
        payBillItemMapper.deleteByDate(mchId, payWay.getChannel(), dateTime);
        return RepeatStatus.FINISHED;
    }

}
