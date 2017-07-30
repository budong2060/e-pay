package com.pay.common;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import util.JsonUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by admin on 2017/7/5.
 * 数据转换
 */
public class JsonTypeHandler implements TypeHandler<Map<String, Object>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Map<String, Object> stringObjectMap, JdbcType jdbcType) throws SQLException {
        if(null == stringObjectMap) {
            preparedStatement.setString(i, null);
        } else {
            String json = JsonUtil.toString(stringObjectMap);
            preparedStatement.setString(i, json);
        }
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, String s) throws SQLException {
        String rs = resultSet.getString(s);
        return JsonUtil.toMap(rs);
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, int i) throws SQLException {
        String rs = resultSet.getString(i);
        return JsonUtil.toMap(rs);
    }

    @Override
    public Map<String, Object> getResult(CallableStatement callableStatement, int i) throws SQLException {
        String rs = callableStatement.getString(i);
        return JsonUtil.toMap(rs);
    }
}
