package com.pay.domain;

import java.math.BigDecimal;
import java.util.Date;

public class PayAccount extends BaseDomain {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.user_id
     *
     * @mbg.generated
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.user_type
     *
     * @mbg.generated
     */
    private Long userType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.user_name
     *
     * @mbg.generated
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.account_id
     *
     * @mbg.generated
     */
    private String accountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.account_pwd
     *
     * @mbg.generated
     */
    private String accountPwd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.account_status
     *
     * @mbg.generated
     */
    private Long accountStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.balance_amount
     *
     * @mbg.generated
     */
    private BigDecimal balanceAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.frozen_amount
     *
     * @mbg.generated
     */
    private BigDecimal frozenAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_account.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.id
     *
     * @return the value of pay_account.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.id
     *
     * @param id the value for pay_account.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.user_id
     *
     * @return the value of pay_account.user_id
     *
     * @mbg.generated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.user_id
     *
     * @param userId the value for pay_account.user_id
     *
     * @mbg.generated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.user_type
     *
     * @return the value of pay_account.user_type
     *
     * @mbg.generated
     */
    public Long getUserType() {
        return userType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.user_type
     *
     * @param userType the value for pay_account.user_type
     *
     * @mbg.generated
     */
    public void setUserType(Long userType) {
        this.userType = userType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.user_name
     *
     * @return the value of pay_account.user_name
     *
     * @mbg.generated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.user_name
     *
     * @param userName the value for pay_account.user_name
     *
     * @mbg.generated
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.account_id
     *
     * @return the value of pay_account.account_id
     *
     * @mbg.generated
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.account_id
     *
     * @param accountId the value for pay_account.account_id
     *
     * @mbg.generated
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.account_pwd
     *
     * @return the value of pay_account.account_pwd
     *
     * @mbg.generated
     */
    public String getAccountPwd() {
        return accountPwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.account_pwd
     *
     * @param accountPwd the value for pay_account.account_pwd
     *
     * @mbg.generated
     */
    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd == null ? null : accountPwd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.account_status
     *
     * @return the value of pay_account.account_status
     *
     * @mbg.generated
     */
    public Long getAccountStatus() {
        return accountStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.account_status
     *
     * @param accountStatus the value for pay_account.account_status
     *
     * @mbg.generated
     */
    public void setAccountStatus(Long accountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.balance_amount
     *
     * @return the value of pay_account.balance_amount
     *
     * @mbg.generated
     */
    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.balance_amount
     *
     * @param balanceAmount the value for pay_account.balance_amount
     *
     * @mbg.generated
     */
    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.frozen_amount
     *
     * @return the value of pay_account.frozen_amount
     *
     * @mbg.generated
     */
    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.frozen_amount
     *
     * @param frozenAmount the value for pay_account.frozen_amount
     *
     * @mbg.generated
     */
    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.create_time
     *
     * @return the value of pay_account.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.create_time
     *
     * @param createTime the value for pay_account.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_account.update_time
     *
     * @return the value of pay_account.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_account.update_time
     *
     * @param updateTime the value for pay_account.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}