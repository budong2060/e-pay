package com.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.framework.process.domain.Domain;

import java.util.Date;

/**
 * Created by admin on 2017/7/4.
 */
public class BaseDomain implements Domain {

    private Integer id;

    @JsonIgnore
    private String requestId;
    /**
     * 请求渠道
     */
    private String payChannel;
    /**
     * 创建时间
     */
    private Date createTime = new Date();
    /**
     * 更新时间
     */
    private Date updateTime = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseDomain{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
