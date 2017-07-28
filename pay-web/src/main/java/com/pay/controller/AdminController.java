package com.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by admin on 2017/7/21.
 */
@Controller
public class AdminController extends BaseController {

    @RequestMapping("/menu")
    public String menu() {
        return "menu";
    }

    @RequestMapping("/index")
    public String index() {

        return "index";
    }

    @RequestMapping("/vm")
    public String test() {
        return "test";
    }

}
