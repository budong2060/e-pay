package com.pay.mybatis;

/**
 * Created by admin on 2017/7/4.
 */
public interface BaseMapper<T> {

    void save(T domain);

    void update(T domain);

    T get(int id);

    void delete(int id);

}
