package com.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by admin on 2016/7/7.
 * 商户账号配置信息
 */
@Alias("payConfig")
public class PayConfig extends BaseDomain {
    /**
     * 账户名称
     */
    @NotEmpty(message = "商户名称不能为空")
    private String mchName;
    /**
     * 账户
     */
    @NotEmpty(message = "商户编号不能为空")
    private String mchId;
    /**
     * 支付方式
     */
    private Integer payWay = 14;
    /**
     * 支付场景
     */
    private Integer payScene = 1;
    /**
     * 状态0-关闭   1-启用
     */
    private Integer status = 1;
    /**
     * 账户信息配置
     */
    @NotNull(message = "第三方账户配置信息不能为空")
    private Map<String, String> config;

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getPayScene() {
        return payScene;
    }

    public void setPayScene(Integer payScene) {
        this.payScene = payScene;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
