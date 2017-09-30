package com.pay.biz.handler.node.ali;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.pay.Constants;
import com.pay.biz.handler.node.DownloadBillNode;
import com.pay.domain.PayBillItem;
import com.pay.enums.PayResultEnum;
import com.pay.enums.PayWay;
import com.pay.enums.RefundStatus;
import com.pay.enums.TradeStatus;
import com.pay.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import util.DateUtils;
import util.PropertiesUtil;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by admin on 2017/8/31.
 * 支付宝账单下载
 */
public class AliPayDownLoadBillNode extends DownloadBillNode {

    private final Logger logger = LoggerFactory.getLogger(AliPayDownLoadBillNode.class);

    @Override
    protected List<PayBillItem> doDownLoad(Map<String, String> config, PayBillItem payBillItem) {
        String url = PropertiesUtil.getProperty(Constants.ALI_GATEWAY);
        String appid = config.get(Constants.ALI_APP_ID);
        AlipayClient alipayClient = new DefaultAlipayClient(url, appid, config.get(Constants.ALI_PRIVATEKEY),
                "json", Constants.CHARSET, config.get(Constants.ALI_PUBLICKEY), config.get(Constants.RSA) == null ? "RSA" : config.get(Constants.RSA));
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();//创建API对应的request类
        AlipayDataDataserviceBillDownloadurlQueryModel model  = new AlipayDataDataserviceBillDownloadurlQueryModel();

        String billDate = DateUtils.format(payBillItem.getBillDate(), "yyyy-MM-dd");
        model.setBillType("trade");
        model.setBillDate(billDate);
        request.setBizModel(model);
        try {
            logger.info(">>获取支付宝账单地址参数:{}", model.toString());
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
            logger.info(">>获取支付宝账单地址结果:是否成功:{}, 描述:{}, 账单地址:{}", response.isSuccess(), response.getMsg(), response.getBillDownloadUrl());
            if (response.isSuccess()) {
                logger.info(">>开始解析支付宝账单.....");
                payBillItem.setThirdMchId(appid);
                String billText = getBillText(response.getBillDownloadUrl());
                List<PayBillItem> list = parseText(billText, payBillItem);
                logger.info(">>开始解析支付宝账单结束.....");
                return list;
            }
        } catch (Exception e) {
            logger.error(">>获取支付宝账单地址失败,原因:", e);
            throw new PayException(PayResultEnum.FAIL_DOWNLOAD_BILL);
        }
        return null;
    }

    /**
     * #支付宝业务明细查询
     * #账号：[20885212319120680156]
     * #起始日期：[2017年08月31日 00:00:00]   终止日期：[2017年09月01日 00:00:00]
     * #-----------------------------------------业务明细列表----------------------------------------
     * 支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号/请求号,服务费（元）,分润（元）,备注
     * 2017083121001004720299453898	,cd6ece976b514130977cb06048359684	,交易	,支付宝APP支付,2017-08-31 00:06:14,2017-08-31 00:06:15,	,	,	,	,*杨(137****2600)	,20.00,20.00,0.00,0.00,0.00,0.00,0.00,,0.00	,0.00,	,0.00,0.00,挂号付费-XXX医院 XX xxxxx 2017-09-01 10:30-11:00 订单编号：71676691
     * 2017083121001004900267628590	,02d2e6d6cf524395b8799b5d97d521d6	,交易	,支付宝APP支付,2017-08-31 00:06:16,2017-08-31 00:06:17,	,	,	,	,*杨(137****2600)	,20.00,20.00,0.00,0.00,0.00,0.00,0.00,,0.00	,0.00,	,0.00,0.00,挂号付费-XXX医院 XX xxxxx 2017-09-06 10:30-11:00 订单编号：72840250
     * @param billText
     * @return
     */
    private List<PayBillItem> parseText(String billText, PayBillItem payBill) {
        if (StringUtils.isEmpty(billText)) {
            return null;
        }
        List<PayBillItem> billItems = new ArrayList<>();
        String [] texts = billText.split("\n");
        if (texts.length > 5) {
            for (int i = 5; i < texts.length; i++) {
                String text = regExp(texts[i]);
                if ("#".equals(text.substring(0, 1))) {
                    continue;
                }
                String[] ss = text.split(",");
                billItems.add(createBillItem(ss, payBill));
            }
        }
        return billItems;
    }

    /**
     *
     * @param str
     * @return
     */
    private String regExp(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        Pattern pattern = Pattern.compile("\t|\r");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    /**
     *
     * @param ss
     * @param payBill
     * @return
     */
    private PayBillItem createBillItem(String[] ss, PayBillItem payBill) {
        PayBillItem payBillItem = new PayBillItem();
        payBillItem.setMchId(payBill.getMchId());
        payBillItem.setBillDate(payBill.getBillDate());
        PayWay payWay = PayWay.getByCode(payBill.getPayWay());
        payBillItem.setPayChannel(payWay.getChannel());
        payBillItem.setPayWay(payBill.getPayWay());
        payBillItem.setThirdMchId(payBill.getThirdMchId());

        payBillItem.setThirdTradeNo(ss[0]);
        payBillItem.setTradeNo(ss[1]);
        if ("交易".equals(ss[2])) {
            payBillItem.setBillType(1);
            payBillItem.setTradeStatus(TradeStatus.TRADE_SUCCESS.code());
            payBillItem.setTradeAmount(new BigDecimal(ss[11]));
            payBillItem.setTradeTime(DateUtils.parse(ss[5]));
        } else if ("退款".equals(ss[2])) {
            payBillItem.setBillType(2);
//            payBillItem.setThirdRefundNo();  //支付宝未生成第三方退款流水号
            payBillItem.setRefundNo(ss[21]);  //退款批次号
            payBillItem.setRefundTime(DateUtils.parse(ss[4]));
            payBillItem.setRefundFinishTime(DateUtils.parse(ss[5]));
            payBillItem.setRefundAmount(new BigDecimal(ss[11]).abs());
            payBillItem.setRefundCouponAmount(new BigDecimal(ss[15]));
            payBillItem.setRefundStatus(RefundStatus.REFUND_SUCCESS.code());
        }
        payBillItem.setTradeDesc(ss[3]);
        return payBillItem;
    }

    /**
     * 解析下载支付包的文件 *.csv.zip
     * @param billUrl
     * @return
     */
    private String getBillText(String billUrl) {
        HttpURLConnection httpUrlConnection = null;
        InputStream fis = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        ByteArrayOutputStream bos = null;
        URL url;
        try {
            url = new URL(billUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(5 * 1000);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();
            fis = httpUrlConnection.getInputStream();
            //解析下载的数据流(*.zip)
            ZipInputStream zis = new ZipInputStream(fis, Charset.forName("gbk"));
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null){
                if (entry.isDirectory()) {
                    continue;
                }
                String fileName = entry.getName();
                if (!fileName.contains("汇总")) {
                    if (entry.getSize() > 0) {
                        bos = new ByteArrayOutputStream();
                        byte[] temp = new byte[1024];
                        int length;
                        while ((length = zis.read(temp, 0, temp.length)) != -1) {
                            bos.write(temp, 0, length);
                        }
                        return new String(bos.toByteArray(), Charset.forName("GBK"));
                    }
                }
            }
        } catch (MalformedURLException e) {
            logger.error(">>解析支付宝账单失败，原因", e);
        } catch (IOException e) {
            logger.error(">>解析支付宝账单失败，原因", e);
        } finally {
            try {
                if (br != null) br.close();
                if (fis != null) fis.close();
                if (httpUrlConnection != null)
                    httpUrlConnection.disconnect();
                if (fos != null)
                    fos.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                logger.error(">>解析支付宝账单失败，原因", e);
            }
        }
        return null;
    }

}
