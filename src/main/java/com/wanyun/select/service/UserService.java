package com.wanyun.select.service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: UserService
 * @Version 1.0.0
 */
public interface UserService {

    int addUser(Map<String, Object> map);

    int updateUser(Map<String, Object> map);

    List<Map<String, Object>> getUserList(Map<String, Object> map);
}