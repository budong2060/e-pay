package com.pay.controller;

import com.pay.domain.PayPayment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2017/9/5.
 */
@RestController
public class ApiPayRecordController extends BaseController {

    /**
     * 查询用户交易记录
     * @return
     */
    @RequestMapping(value = "/record/{userId}/{userType}", method = RequestMethod.GET)
    public Object pay() {
        return null;
    }




}
