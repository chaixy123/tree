package com.wanyun.select.service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: ForecastService
 * @Version 1.0.0
 */
public interface ForecastService {
    List<Map<String, Object>> getForecastList(Map<String, Object> map);
}
