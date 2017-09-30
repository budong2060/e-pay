package com.pay.service.impl;

import com.pay.common.Pager;
import com.pay.domain.PayBalance;
import com.pay.domain.PayBalanceItem;
import com.pay.domain.PayBillItem;
import com.pay.domain.PayConfig;
import com.pay.enums.PayWay;
import com.pay.enums.TradeType;
import com.pay.mybatis.*;
import com.pay.service.PayBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import util.DateUtils;
import util.IdUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2016/8/17.
 */
@Service
public class PayBillServiceImpl implements PayBillService {

    private static final Logger logger = LoggerFactory.getLogger(PayBillServiceImpl.class);

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private Job payBillJob;

    @Autowired
    private PayConfigMapper payConfigMapper;

    @Autowired
    private PayBillItemMapper payBillItemMapper;

    @Autowired
    private PayPaymentMapper payPaymentMapper;

    @Autowired
    private PayRefundMapper payRefundMapper;

    @Autowired
    private PayBalanceMapper payBalanceMapper;

    @Autowired
    private PayBalanceItemExtendMapper payBalanceItemExtendMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    private ExecutorService executorService;


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

    @Transactional(timeout = 100, rollbackFor = Exception.class)
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

    /***********************************************************************************************
        对账逻辑：
        1、获取平台、第三方支付、退费数据
        2、统计平台、第三方支付、退费总额度
        3、过滤平台、第三方支付相同账单(商户订单号+第三方流水号+支付金额+退款金额)记录；（只关注异常单）
        4、以平台商户号为准，筛选出平台、第三方账户商户流水号一致切其他信息不同的记录
        5、处理余下平台、第三方没有关联的数据
     ***********************************************************************************************/
    @Override
    public void execBalance(String mchId, Date billDate) {
        int nThreads = Runtime.getRuntime().availableProcessors();
        List<PayConfig> payConfigs = payConfigMapper.findByBill(mchId);
        executorService = Executors.newFixedThreadPool(nThreads);
        for (PayConfig payConfig : payConfigs) {
            executorService.execute(new ExecBalance(billDate, payConfig));
        }
    }

    /**
     * @param billDate
     * @param payConfig
     */
    @Override
    public void execBalance(Date billDate, PayConfig payConfig) {
        PayWay payWay = PayWay.getByCode(payConfig.getPayWay());
        //获取账单参数数据
        PayBillItem paramItem = new PayBillItem();
        paramItem.setMchId(payConfig.getMchId());
        paramItem.setPayChannel(payWay.getChannel());
        paramItem.setBillDate(billDate);

        //对账结果对象
        PayBalance balance = new PayBalance();
        String balanceNo = IdUtils.genId();
        balance.setBalanceNo(balanceNo);
        balance.setMchId(payConfig.getMchId());
        balance.setPayChannel(payWay.getChannel());
        balance.setBalanceDate(paramItem.getBillDate());

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            //支付对账异常item
            List<PayBalanceItem> payBalanceItems = calcPayMoney(balance, paramItem);
            //退款对账异常item
            List<PayBalanceItem> refundBalanceItems = calcRefundMoney(balance, paramItem);
            //合并
            payBalanceItems.addAll(refundBalanceItems);
            if (payBalanceItems.size() > 0) { //判断对账是否有异常数据
                balance.setBalanceStatus(1);
            }
            payBalanceMapper.insert(balance);
            payBalanceItemExtendMapper.batchInsert(payBalanceItems);
            txManager.commit(status);
        } catch (Exception e) {
            logger.error(">>对账执行异常,商户:{},渠道:{}", paramItem.getMchId(), paramItem.getPayWay());
            txManager.rollback(status);
            throw new RuntimeException(e);
        }
    }

    /**
     * 支付对账
     * @param balance
     * @param paramItem
     * @return
     */
    private List<PayBalanceItem> calcPayMoney(PayBalance balance, PayBillItem paramItem) {
        //实际第三方账户支付收款记录
        paramItem.setBillType(TradeType.PAY.code());
        List<PayBillItem> payItems = payBillItemMapper.pageQuery(paramItem, 0, Integer.MAX_VALUE);
        BigDecimal actualCollectMoney = new BigDecimal(0.00);
        if (null != payItems && payItems.size() > 0) {
            actualCollectMoney = payItems.stream().parallel()
                    .map(item -> item.getTradeAmount())
                    .reduce(BigDecimal::add).get();
        }
        //系统收款流水
        List<PayBillItem> sysPayItems = payPaymentMapper.findByTradeDate(paramItem.getMchId(), paramItem.getPayChannel(), paramItem.getBillDate(), 0, Integer.MAX_VALUE);
        BigDecimal sysCollectMoney = new BigDecimal(0.00);
        if (null != sysPayItems && sysPayItems.size() > 0) {
            sysCollectMoney = sysPayItems.stream().parallel()
                    .map(item -> item.getTradeAmount())
                    .reduce(BigDecimal::add).get();
        }

        balance.setActualCollectAmount(actualCollectMoney);
        balance.setSysCollectAmount(sysCollectMoney);
        //支付对账异常item
        return diff(balance, sysPayItems, payItems);
    }

    /**
     * 支付对账
     * @param balance
     * @param paramItem
     * @return
     */
    private List<PayBalanceItem> calcRefundMoney(PayBalance balance, PayBillItem paramItem) {
        //实际第三方账户支付收款记录
        paramItem.setBillType(TradeType.REFUND.code());
        List<PayBillItem> refundItems = payBillItemMapper.pageQuery(paramItem, 0, Integer.MAX_VALUE);

        BigDecimal actualRefundMoney = new BigDecimal(0.00);
        if (null != refundItems && refundItems.size() > 0) {
            actualRefundMoney = refundItems.stream().parallel()
                    .map(item -> item.getTradeAmount())
                    .reduce(BigDecimal::add).get();
        }
        List<PayBillItem> sysRefundItems = payRefundMapper.findByTradeDate(paramItem.getMchId(), paramItem.getPayChannel(), paramItem.getBillDate(), 0, Integer.MAX_VALUE);
        BigDecimal sysRefundMoney = new BigDecimal(0.00);
        if (null != sysRefundItems && sysRefundItems.size() > 0) {
            sysRefundMoney = refundItems.stream().parallel()
                    .map(item -> item.getTradeAmount())
                    .reduce(BigDecimal::add).get();
        }
        balance.setActualRefundAmount(actualRefundMoney);
        balance.setSysRefundAmount(sysRefundMoney);

        //退款对账异常item
        return diff(balance, sysRefundItems, refundItems);
    }

    /**
     *
     * @param balance
     * @param sources
     * @param targets
     * @return
     */
    private List<PayBalanceItem> diff(PayBalance balance, List<PayBillItem> sources, List<PayBillItem> targets) {
        List<PayBalanceItem> balanceItems = new ArrayList<>();

        //过滤平台-第三方账单相同的数据
        sources.removeIf(source -> targets.removeIf(target -> source == target));
        //以平台交易流水为准,过滤相同流水号的,并将相同流水且其他数据不一致的放入PayBalanceItem中
        sources.removeIf(source ->
                targets.removeIf(target -> {
                    if (source.getTradeNo().equals(target.getTradeNo()) ||
                            source.getRefundNo().equals(target.getRefundNo())) {
                        balanceItems.add(createItem(balance, source, target));
                        return true;
                    }
                    return false;
                })
        );
        //将平台流水号与第三方流水不一致的放入PayBalanceItem中
        sources.forEach(source -> balanceItems.add(createItem(balance, source, null)));
        targets.forEach(target -> balanceItems.add(createItem(balance, target, null)));
        return balanceItems;
    }

    /**
     *
     * @param balance
     * @param source
     * @param target
     * @return
     */
    private PayBalanceItem createItem(PayBalance balance, PayBillItem source, PayBillItem target) {
        PayBalanceItem item = new PayBalanceItem();
        item.setPayChannel(balance.getPayChannel());
        item.setMchId(balance.getMchId());
        item.setBalanceNo(balance.getBalanceNo());
        if (null != source) {
            item.setPayWay(source.getPayWay());
            item.setBillType(source.getBillType());
            if (source.getBillType() == TradeType.PAY.getCode()) {
                item.setTradeNo(source.getTradeNo());
                item.setTradeAmount(source.getTradeAmount());
                item.setTradeTime(source.getTradeTime());
            } else {
                item.setTradeNo(source.getRefundNo());
                item.setTradeAmount(source.getRefundAmount());
                item.setTradeTime(source.getRefundTime());
            }
        }
        if (null != target) {
            item.setPayWay(source.getPayWay());
            item.setBillType(source.getBillType());
            if (target.getBillType() == TradeType.PAY.getCode()) {
                item.setThirdTradeNo(target.getThirdTradeNo());
                item.setThirdTradeAmount(target.getTradeAmount());
                item.setThirdTradeTime(target.getTradeTime());
            } else {
                item.setThirdTradeNo(target.getThirdRefundNo());
                item.setThirdTradeAmount(target.getRefundAmount());
                item.setThirdTradeTime(target.getRefundFinishTime());
            }
        }
        return item;
    }

    /**
     * 启动线程执行对账
     */
    final class ExecBalance implements Runnable {
        /**
         * 对账渠道
         */
        private final PayConfig payConfig;
        /**
         * 对账日期
         */
        private final Date date;

        public ExecBalance(Date date, PayConfig payConfig) {
            this.date = date;
            this.payConfig = payConfig;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            logger.info(">>线程:{},执行开始对账;账户信息:{}", Thread.currentThread().getName(), payConfig);
            PayBillServiceImpl.this.execBalance(date, payConfig);
            long end = System.currentTimeMillis();
            logger.info(">>线程:{},执行开始对账结束;账户信息:{},耗时:{}", Thread.currentThread().getName(), payConfig, (end - start));
        }

    }

}
