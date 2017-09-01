package com.pay.service.impl;

import com.pay.common.Pager;
import com.pay.domain.PayBillItem;
import com.pay.domain.PayConfig;
import com.pay.enums.PayWay;
import com.pay.mybatis.PayConfigMapper;
import com.pay.service.PayBillService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/8/17.
 */
@Service
public class PayBillServiceImpl implements PayBillService {

    private static final Logger logger = LoggerFactory.getLogger(PayBillServiceImpl.class);

    @Autowired
    private PayConfigMapper payConfigMapper;

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private Job payBillJob;

    @Override
    public void downLoad(Date startDate, Date endDate, String mchId) {
        if(null == endDate) {
            endDate = DateUtils.addDay(new Date(), -1);
        }
        if(null == startDate) {
            startDate = endDate;
        }

        List<PayConfig> configs = payConfigMapper.findByBill(mchId);
        while (DateUtils.compare(startDate, endDate) != 1) {
            for(PayConfig config : configs) {
                try {
                    doDownLoad(config, startDate);
                } catch (Exception e) {
                    logger.error("账户:{}，拉取{}渠道账单失败，账单日{}, 失败原因:{}", config.getMchId(), config.getPayWay(), startDate, e);
                }
            }
            startDate = DateUtils.addDay(startDate, 1);
        }

    }

    @Transactional(timeout = 60, rollbackFor = Exception.class)
    private void doDownLoad(PayConfig config, Date startDate) throws Exception {
        JobExecution execution = launcher.run(payBillJob, new JobParametersBuilder()
                .addString("mchId", config.getMchId())
                .addLong("payWay", Long.valueOf(config.getPayWay()))
                .addDate("loadDate", startDate)
                .addLong("payScene", (long)config.getPayScene())
//                .addString("version", "1.0")
                .toJobParameters());
        PayWay payWay = PayWay.getByCode(config.getPayWay());
        logger.info("jobId:{},状态:{};账户:{}，拉取{}渠道账单所用时间{}", execution.getJobId(), execution.getStatus(),
                config.getMchId(), payWay.getMessage(), (execution.getEndTime().getTime() - execution.getStartTime().getTime()));
    }

    @Override
    public Pager<PayBillItem> query(String unitId, Integer[] payWay, Integer[] orderType, Date billDate, Integer page, Integer size) {
//        String dateTime = DateFormatUtils.format(billDate, "yyyy-MM-dd");
//        int count = payBillItemDao.countByUnitId(unitId, payWay, orderType, dateTime);
//        List<PayBillItem> list = null;
//        if(count > 0) {
//            list = payBillItemDao.pageQuery(unitId, payWay, orderType, dateTime, (page - 1) * size, size);
//        }
//        return new MyPage<>(page, size, count, list);
        return null;
    }

}
