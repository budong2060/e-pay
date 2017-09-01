package com.pay.service.batch.bill;

import com.framework.process.SimpleJob;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.PayBillItem;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/8/17.
 */
public class PayBillItemReader extends PayBillReader<PayBillItem> {
    /**
     * 需下载数据的商户ID
     */
    private String mchId;
    /**
     * 下载账单日期
     */
    private Date loadDate;
    /**
     * 支付方式
     */
    private Long payWay;

    private Integer payScene;

    @Resource(name = "downloadJob")
    private SimpleJob downloadJob;

    @Override
    public void doDownLoad() {
        PayBillItem billItem = new PayBillItem();
        billItem.setPayWay(payWay.intValue());
        billItem.setMchId(mchId);
        billItem.setBillDate(loadDate);
        PayResult billResult = (PayResult) downloadJob.execute(billItem);
        if (null != billResult) {
            result = (List<PayBillItem>) billResult.getData();
        }
    }

    @Override
    public PayBillItem read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return doReader();
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Long getPayWay() {
        return payWay;
    }

    public void setPayWay(Long payWay) {
        this.payWay = payWay;
    }

    public Integer getPayScene() {
        return payScene;
    }

    public void setPayScene(Integer payScene) {
        this.payScene = payScene;
    }
}
