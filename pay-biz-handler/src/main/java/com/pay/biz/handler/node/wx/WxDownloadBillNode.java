package com.pay.biz.handler.node.wx;

import com.pay.Constants;
import com.pay.biz.handler.node.DownloadBillNode;
import com.pay.client.WxPayClient;
import com.pay.domain.PayBillItem;
import com.pay.enums.PayWay;
import com.pay.enums.RefundStatus;
import com.pay.enums.TradeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import util.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by admin on 2017/8/23.
 * 下载微信对账单
 */
public class WxDownloadBillNode extends DownloadBillNode {

    protected final Logger logger = LoggerFactory.getLogger(WxDownloadBillNode.class);

    @Autowired
    private WxPayClient client;

    @Override
    protected List<PayBillItem> doDownLoad(Map<String, String> config, PayBillItem payBillItem) {
        //1.构造账单参数
        Map<String, String> billParams = buildParams(config, payBillItem.getBillDate());
        //2.签名
        String sign = WXUtil.getSign(billParams, config.get(Constants.WX_PARTNER_KEY));
        billParams.put("sign", sign);
        //3.下载对账单xml参数
        String downloadXml = MapUtil.map2Xml(billParams);
        String billData = client.downloadBill(downloadXml);
        //4.解析账单数据
        List<PayBillItem> items = parseBill(payBillItem, billData);
        return items;
    }

    protected Map<String, String> buildParams(Map<String, String> config, Date billDate) {
        Map<String, String> sortParams = new TreeMap<>();
        sortParams.put("appid", config.get(Constants.WX_APPID));
        sortParams.put("mch_id", config.get(Constants.WX_MCH_ID));
        sortParams.put("bill_date", DateUtils.format(billDate, "yyyyMMdd"));
        sortParams.put("bill_type", "ALL");
        sortParams.put("nonce_str", WXUtil.getNonceStr());
        return sortParams;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 解析帐单数据
     * @param billData
     * @return
     */
    private  List<PayBillItem> parseBill(PayBillItem payBillItem, String billData) {
        if (null == billData) {
            return null;
        }
        logger.info(">>商户:{},微信账单数据解析解析开始......", payBillItem);
        billData = billData.replace("`", "");
        List<String> list = Arrays.asList(billData.split("\r\n"));
        if(null != list && list.size() > 2) {
            int length = list.size() - 2;
            List<PayBillItem> items = new ArrayList<>();
            for (int i = 1; i < length; i++) {
                List<String> beanList = Arrays.asList(list.get(i).split(","));
                items.add(createBillItem(payBillItem, beanList));
            }
            return items;
        }
        logger.info(">>商户:{},微信账单数据解析解析结束......", payBillItem);
        return null;
    }

    /**
     * 组装BillItem
     * @param beanList
     * @return
     */
    private PayBillItem createBillItem(PayBillItem payBill, List<String> beanList) {
        PayBillItem payBillItem = new PayBillItem();
        payBillItem.setMchId(payBill.getMchId());  //平台分配商户号
        payBillItem.setBillDate(payBill.getBillDate());
        payBillItem.setTradeTime(DateUtils.parse(beanList.get(0)));
        //APP JSPAY  NATIVE
//        PayWay payWay = PayWay.getByType(beanList.get(8));
//        payBillItem.setPayWay(null == payWay ? PayWay.WX_PAY.code() : payWay.code());
        payBillItem.setPayWay(payBill.getPayWay());
        payBillItem.setThirdMchId(beanList.get(2));    //第三方支付商户
        payBillItem.setThirdSubMchId(beanList.get(3)); //第三方支付子商户
        payBillItem.setTradeNo(beanList.get(6));       //平台支付单号
        payBillItem.setThirdTradeNo(beanList.get(5));  //第三方支付单号
        PayWay payWay = PayWay.getByCode(payBill.getPayWay());
        payBillItem.setPayChannel(payWay.getChannel());

        if("SUCCESS".equals(beanList.get(9))) { //支付
            payBillItem.setBillType(1);
            payBillItem.setTradeStatus(TradeStatus.TRADE_SUCCESS.code());
            payBillItem.setTradeAmount(new BigDecimal(beanList.get(13)));
        }
        if("REFUND".equals(beanList.get(9))) {             //退款
            payBillItem.setRefundNo(beanList.get(15));     //平台退款单号
            payBillItem.setThirdRefundNo(beanList.get(14));//第三方退款单号
            payBillItem.setBillType(2);
            payBillItem.setRefundTime(DateUtils.parse(beanList.get(0)));
            payBillItem.setRefundAmount(new BigDecimal(beanList.get(16)));
            payBillItem.setRefundCouponAmount(new BigDecimal(beanList.get(17)));
            String refundState = beanList.get(19);
            if("SUCCESS".equals(refundState)) {
                payBillItem.setBillType(2);
                payBillItem.setRefundStatus(RefundStatus.REFUND_SUCCESS.code());
            }
        }
        payBillItem.setTradeDesc(beanList.get(20));
//        payBillItem.setUnitId(balanceCashItem.getAccountNo());
//        payBillItem.setPayWay(balanceCashItem.getPayWay());
        return payBillItem;
    }
}
