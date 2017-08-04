package com.pay.controller.admin;

import com.pay.common.Pager;
import com.pay.controller.BaseController;
import com.pay.domain.PayPayment;
import com.pay.service.PayPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by admin on 2017/7/31.
 */
@Controller
public class AdminPaypaymentController extends BaseController {

    @Autowired
    private PayPaymentService payPaymentService;

    @RequestMapping("/admin/pay/{pageNum}/{pageSize}")
    public ModelAndView login(PayPayment payment, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize")int pageSize) {
        ModelAndView view = new ModelAndView();
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = Pager.DEFAULT_PAGE_NUM;
        }
        Pager<PayPayment> pager = payPaymentService.query(payment, pageNum, pageSize);
        view.addObject("pager", pager);
        view.setViewName("/views/payment");
        return view;
    }

}
