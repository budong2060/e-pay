package com.pay.service.batch.bill;

import com.pay.enums.PayWay;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * Created by admin on 2016/8/17.
 */
public class PayBillJobExecutionDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        Long payWayParam = jobExecution.getJobParameters().getLong("payWay");
        PayWay payWay = PayWay.getByCode(payWayParam.intValue());
        if (null != payWay) {
            return new FlowExecutionStatus(payWay.toString());
        }
        return FlowExecutionStatus.COMPLETED;
    }
}
