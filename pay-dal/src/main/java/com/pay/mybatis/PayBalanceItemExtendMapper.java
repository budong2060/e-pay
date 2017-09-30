package com.pay.mybatis;

import com.pay.domain.PayBalanceItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2017/9/29.
 */
public interface PayBalanceItemExtendMapper extends PayBalanceItemMapper {

    /**
     * 批量保存数据
     * @param items
     */
    void batchInsert(@Param("items") List<PayBalanceItem> items);

}
