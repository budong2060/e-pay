//package com.pay.service.batch.balance;
//
//import com.pay.domain.PayBalance;
//import com.pay.domain.PayBalanceItem;
//import com.pay.domain.PayBillItem;
//import com.pay.domain.PayConfig;
//import com.pay.enums.PayWay;
//import com.pay.enums.TradeType;
//import com.pay.mybatis.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import util.IdUtils;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * 对账
// * Created by admin on 2017/9/28.
// */
//@Service
//public class PayBalanceService {
//
//    private final Logger logger = LoggerFactory.getLogger(PayBalanceService.class);
//
//    @Autowired
//    private PayConfigMapper payConfigMapper;
//
//    @Autowired
//    private PayBillItemMapper payBillItemMapper;
//
//    @Autowired
//    private PayPaymentMapper payPaymentMapper;
//
//    @Autowired
//    private PayRefundMapper payRefundMapper;
//
//    @Autowired
//    private PayBalanceMapper payBalanceMapper;
//
//    @Autowired
//    private PayBalanceItemExtendMapper payBalanceItemExtendMapper;
//
//    private ExecutorService executorService;
//
//    /**
//     *
//     * @param mchId
//     * @param billDate
//     */
//    public void execBalance(String mchId, Date billDate) {
//        int nThreads = Runtime.getRuntime().availableProcessors();
//        List<PayConfig> payConfigs = payConfigMapper.findByBill(mchId);
//        executorService = Executors.newFixedThreadPool(nThreads);
//        for (PayConfig payConfig : payConfigs) {
//            executorService.execute(new ExecBalance(billDate, payConfig));
//        }
//    }
//
//    /**
//     * @param billDate
//     * @param payConfig
//     */
//    public void execBalance(Date billDate, PayConfig payConfig) {
//        PayWay payWay = PayWay.getByCode(payConfig.getPayWay());
//        //获取账单参数数据
//        PayBillItem paramItem = new PayBillItem();
//        paramItem.setMchId(payConfig.getMchId());
//        paramItem.setPayChannel(payWay.getChannel());
//        paramItem.setBillDate(billDate);
//
//        //对账结果对象
//        PayBalance balance = new PayBalance();
//        String balanceNo = IdUtils.genId();
//        balance.setBalanceNo(balanceNo);
//        balance.setMchId(payConfig.getMchId());
//        balance.setPayChannel(payWay.getChannel());
//        balance.setBalanceDate(paramItem.getBillDate());
//
//        //支付对账异常item
//        List<PayBalanceItem> payBalanceItems = calcPayMoney(balance, paramItem);
//        //退款对账异常item
//        List<PayBalanceItem> refundBalanceItems = calcRefundMoney(balance, paramItem);
//        //合并
//        payBalanceItems.addAll(refundBalanceItems);
//        if (payBalanceItems.size() > 0) { //判断对账是否有异常数据
//            balance.setBalanceStatus(1);
//        }
//        payBalanceMapper.insert(balance);
//        payBalanceItemExtendMapper.batchInsert(payBalanceItems);
//    }
//
//    /**
//     * 支付对账
//     * @param balance
//     * @param paramItem
//     * @return
//     */
//    private List<PayBalanceItem> calcPayMoney(PayBalance balance, PayBillItem paramItem) {
//        //实际第三方账户支付收款记录
//        paramItem.setBillType(TradeType.PAY.code());
//        List<PayBillItem> payItems = payBillItemMapper.pageQuery(paramItem, 1, Integer.MAX_VALUE);
//        BigDecimal actualCollectMoney = payItems.stream().parallel()
//                .map(item -> item.getTradeAmount())
//                .reduce(BigDecimal::add).get();
//        //系统收款流水
//        List<PayBillItem> sysPayItems = payPaymentMapper.findByTradeDate(paramItem.getMchId(), paramItem.getPayChannel(), paramItem.getBillDate(), 1, Integer.MAX_VALUE);
//        BigDecimal sysCollectMoney = sysPayItems.stream().parallel()
//                .map(item -> item.getTradeAmount())
//                .reduce(BigDecimal::add).get();
//
//        balance.setActualCollectAmount(actualCollectMoney);
//        balance.setSysCollectAmount(sysCollectMoney);
//        //支付对账异常item
//        return diff(balance, sysPayItems, payItems);
//    }
//
//    /**
//     * 支付对账
//     * @param balance
//     * @param paramItem
//     * @return
//     */
//    private List<PayBalanceItem> calcRefundMoney(PayBalance balance, PayBillItem paramItem) {
//        //实际第三方账户支付收款记录
//        paramItem.setBillType(TradeType.REFUND.code());
//        List<PayBillItem> refundItems = payBillItemMapper.pageQuery(paramItem, 1, Integer.MAX_VALUE);
//        BigDecimal actualRefundMoney = refundItems.stream().parallel()
//                .map(item -> item.getTradeAmount())
//                .reduce(BigDecimal::add).get();
//        List<PayBillItem> sysRefundItems = payRefundMapper.findByTradeDate(paramItem.getMchId(), paramItem.getPayChannel(), paramItem.getBillDate(), 1, Integer.MAX_VALUE);
//        BigDecimal sysRefundMoney = refundItems.stream().parallel()
//                .map(item -> item.getTradeAmount())
//                .reduce(BigDecimal::add).get();
//
//        balance.setActualRefundAmount(actualRefundMoney);
//        balance.setSysRefundAmount(sysRefundMoney);
//
//        //退款对账异常item
//        return diff(balance, sysRefundItems, refundItems);
//    }
//
//    /**
//     *
//     * @param balance
//     * @param sources
//     * @param targets
//     * @return
//     */
//    private List<PayBalanceItem> diff(PayBalance balance, List<PayBillItem> sources, List<PayBillItem> targets) {
//        List<PayBalanceItem> balanceItems = new ArrayList<>();
//
//        //过滤平台-第三方账单相同的数据
//        sources.removeIf(source -> targets.removeIf(target -> source == target));
//        //以平台交易流水为准,过滤相同流水号的,并将相同流水且其他数据不一致的放入PayBalanceItem中
//        sources.removeIf(source ->
//                targets.removeIf(target -> {
//                    if (source.getTradeNo().equals(target.getTradeNo())) {
//                        balanceItems.add(createItem(balance, source, target));
//                        return true;
//                    }
//                    return false;
//                })
//        );
//        //将平台流水号与第三方流水不一致的放入PayBalanceItem中
//        sources.forEach(source -> balanceItems.add(createItem(balance, source, null)));
//        targets.forEach(target -> balanceItems.add(createItem(balance, target, null)));
//        return balanceItems;
//    }
//
//    /**
//     *
//     * @param balance
//     * @param source
//     * @param target
//     * @return
//     */
//    private PayBalanceItem createItem(PayBalance balance, PayBillItem source, PayBillItem target) {
//        PayBalanceItem item = new PayBalanceItem();
//        item.setPayChannel(balance.getPayChannel());
//        item.setMchId(balance.getMchId());
//        item.setBalanceNo(balance.getBalanceNo());
//        if (null != source) {
//            item.setPayWay(source.getPayWay());
//            item.setBillType(source.getBillType());
//            if (source.getBillType() == TradeType.PAY.getCode()) {
//                item.setTradeNo(source.getTradeNo());
//                item.setTradeAmount(source.getTradeAmount());
//                item.setTradeTime(source.getTradeTime());
//            } else {
//                item.setTradeNo(source.getRefundNo());
//                item.setTradeAmount(source.getRefundAmount());
//                item.setTradeTime(source.getRefundTime());
//            }
//        }
//        if (null != target) {
//            item.setPayWay(source.getPayWay());
//            item.setBillType(source.getBillType());
//            if (target.getBillType() == TradeType.PAY.getCode()) {
//                item.setThirdTradeNo(target.getThirdTradeNo());
//                item.setThirdTradeAmount(target.getTradeAmount());
//                item.setThirdTradeTime(target.getTradeTime());
//            } else {
//                item.setThirdTradeNo(target.getThirdRefundNo());
//                item.setThirdTradeAmount(target.getRefundAmount());
//                item.setThirdTradeTime(target.getRefundFinishTime());
//            }
//        }
//        return item;
//    }
//
//    /**
//     * 启动线程执行对账
//     */
//    final class ExecBalance implements Runnable {
//        /**
//         * 对账渠道
//         */
//        private final PayConfig payConfig;
//        /**
//         * 对账日期
//         */
//        private final Date date;
//
//        public ExecBalance(Date date, PayConfig payConfig) {
//            this.date = date;
//            this.payConfig = payConfig;
//        }
//
//        @Override
//        public void run() {
//            long start = System.currentTimeMillis();
//            logger.info(">>线程:{},执行开始对账;账户信息:{}", Thread.currentThread().getName(), payConfig);
//            execBalance(date, payConfig);
//            long end = System.currentTimeMillis();
//            logger.info(">>线程:{},执行开始对账结束;账户信息:{},耗时:{}", Thread.currentThread().getName(), payConfig, (end - start));
//        }
//
//    }
//}
