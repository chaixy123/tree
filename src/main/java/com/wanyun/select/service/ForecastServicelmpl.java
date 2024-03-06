package com.wanyun.select.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: ForecastServicelmpl
 * @Version 1.0.0
 */
@Service
public class ForecastServicelmpl implements ForecastService {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getForecastList(Map<String, Object> map) {
        String id = map.get("id") == null ? "" : map.get("id") + "";
        String sql = "select maxtemp,mintemp,wind_level,wind_direction,weat, DATE_FORMAT(forecast_time,'%m.%d') time ,'优' aqi from pc_wy_weatherforecast where station_num='" + id + "' order by forecast_time asc";
        return jdbcTemplate.queryForList(sql);
    }
}