package com.pay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);


    @RequestMapping("/test")
    public Object test() {
        Map<Object, Object> map = new HashMap<Object, Object>(){{
            put("test", "test");
            put("date", new Date());
        }};
        logger.info("test=================================={}", map);
        return map;
    }

}






















