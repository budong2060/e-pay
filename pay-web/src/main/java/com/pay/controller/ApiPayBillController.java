package com.pay.controller;

import com.pay.biz.handler.result.PayResult;
import com.pay.domain.PayConfig;
import com.pay.enums.PayResultEnum;
import com.pay.service.PayBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.DateUtils;

import java.util.Date;

/**
 * Created by admin on 2017/7/17.
 * 微信支付相关
 */
@RestController
public class ApiPayBillController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ApiPayBillController.class);

    @Autowired
    private PayBillService payBillService;

    @RequestMapping(value = "/pay/bill/task/download")
    public Object download(final Date startDate, final Date endDate, final String mchId) {
        PayResult result = new PayResult();
        if(null != startDate && null != endDate) {
            if(DateUtils.compare(startDate, endDate) == 1) {
                result.setResultEnum(PayResultEnum.ILLEGAL_ARGUMENTS);
                result.setMessage("下载对账单开始日期不能大于结束日期");
                return result;
            }
        }
        new Thread(() -> {
            payBillService.downLoad(startDate, endDate, mchId);
        }).start();
        result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
        return result;
    }

    @RequestMapping(value = "/pay/bill/task/balance")
    public Object execBalance(String mchId, Date billDate) {
        if (null == billDate) {
            billDate = new Date();
        }
        payBillService.execBalance(mchId, billDate);
        return new PayResult<>("后台对账功能启动成功...");
    }

}
