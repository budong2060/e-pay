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
        payAccountMapper.insert(account);

        account.setUserName("232323fas");
        payAccountMapper.insert(account);

        account.setUserName("fsdfasdfasdfas");
        payAccountMapper.updateByPrimaryKey(account);
//        throw new RuntimeException("============");
        return account;
    }
}
