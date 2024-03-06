package com.wanyun.select.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: UserServicelmpl
 * @Version 1.0.0
 */
@Service
public class UserServicelmpl implements UserService {
    @Resource
    JdbcTemplate jdbcTemplate;


    @Override
    public int addUser(Map<String, Object> map) {
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        String sql = "insert into sys_user(openid) values('" + openid + "')";
        return jdbcTemplate.update(sql);
    }

    @Override
    public int updateUser(Map<String, Object> map) {
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        String name = map.get("name") == null ? "" : map.get("name") + "";
        String phone = map.get("openid") == null ? "" : map.get("phone") + "";
        String head = map.get("head") == null ? "" : map.get("head") + "";
        String sql = "update sys_user set ";
        int inde = 0;
        if (!name.isEmpty()) {
            inde = 1;
            sql += " name='" + name + "' ";
        }
        if (!phone.isEmpty()) {
            if (inde > 0) {
                sql += " , ";
            }
            inde = 2;
            sql += " name='" + name + "' ";
        }
        if (!head.isEmpty()) {
            if (inde > 0) {
                sql += " , ";
            }
            sql += " name='" + name + "' ";
        }
        sql += "  where openid='" + openid + "' ";
        return jdbcTemplate.update(sql);
    }

    @Override
    public List<Map<String, Object>> getUserList(Map<String, Object> map) {
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        String sql = "select openid,name,phone,head from sys_user where openid='" + openid + "'";
        return jdbcTemplate.queryForList(sql);
    }
}