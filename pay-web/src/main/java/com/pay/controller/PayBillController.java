package com.pay.controller;

import com.pay.biz.handler.result.PayResult;
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
public class PayBillController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PayBillController.class);

    @Autowired
    private PayBillService payBillService;

    @RequestMapping(value = "/pay/bill/task/download")
    @ResponseBody
    public Object download(final Date startDate, final Date endDate, final String mchId) {
        PayResult result = new PayResult();
        if(null != startDate && null != endDate) {
            if(DateUtils.compare(startDate, endDate) == 1) {
                result.setResultEnum(PayResultEnum.ILLEGAL_ARGUMENTS);
                result.setMessage("下载对账单开始日期不能大于结束日期");
                return result;
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                payBillService.downLoad(startDate, endDate, mchId);
            }
        }).start();
        result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
        return result;
    }

}
