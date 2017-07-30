package com.pay.client;

import com.pay.client.result.NotifyResult;
import com.pay.client.vo.NotifyVo;

/**
 * Created by admin on 2017/7/18.
 */
public interface NotifyClient {
    /**
     *
     * @param notifyVo
     * @return
     */
    NotifyResult notify(String url, NotifyVo notifyVo);

}
