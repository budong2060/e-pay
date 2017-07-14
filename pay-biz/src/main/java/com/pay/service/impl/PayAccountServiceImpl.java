package com.pay.service.impl;

import com.pay.domain.PayAccount;
import com.pay.mybatis.PayAccountMapper;
import com.pay.service.PayAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by admin on 2017/7/4.
 */
@Service
public class PayAccountServiceImpl implements PayAccountService {

    @Autowired
    private PayAccountMapper payAccountMapper;

    @Transactional
    @Override
    public PayAccount save(PayAccount account) {
        payAccountMapper.save(account);

        account.setUserName("232323fas");
        payAccountMapper.save(account);

        account.setUserName("fsdfasdfasdfas");
        payAccountMapper.update(account);
//        throw new RuntimeException("============");
        return account;
    }
}
