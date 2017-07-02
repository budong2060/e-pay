package com.pay.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Halbert on 2017/7/1.
 */
@RestController
public class BaseController {

    @RequestMapping("/test")
    public Object test() {
        Map<Object, Object> map = new HashMap<Object, Object>(){{
            put("test", "test");
            put("date", new Date());
        }};
        return map;
    }

}






















