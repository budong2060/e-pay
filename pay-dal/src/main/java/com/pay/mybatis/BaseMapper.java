package com.pay.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 */
public interface BaseMapper<T> {

    void save(T domain);

    void update(T domain);

    T get(int id);

    void delete(int id);

    long count(T t);

    List<T> query(@Param("t") T t, @Param("pageNum")int pageNum, @Param("pageSize") int pageSize);

}
