package com.wanyun.select.service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: LimitService
 * @Version 1.0.0
 */
public interface LimitService {
    public void getLimitData() throws Exception;
    public List<Map<String, Object>> getLimitQuart();
}
