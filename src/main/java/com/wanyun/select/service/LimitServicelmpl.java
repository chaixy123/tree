package com.wanyun.select.service;

import com.wanyun.select.utils.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanyun.select.utils.HttpTool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: LimitServicelmpl
 * @Version 1.0.0
 */
@Service
public class LimitServicelmpl implements LimitService {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public void getLimitData() throws Exception {
        String url = "http://yw.jtgl.beijing.gov.cn/jgjxx/services/getRuleWithWeek";
        String data = HttpTool.sendGet(url);
        JSONObject jsonObject = JSONObject.parseObject(data);
        String state = jsonObject.getString("state");
        if (state.equals("success")) {
            String result = jsonObject.getString("result");
            JSONArray jsonArray = JSONArray.parseArray(result);
            if (jsonArray != null && jsonArray.size() > 0) {
                String insertSql = "insert into wy_sys_limit(limitedTime,limitedWeek,limitedNumber,time)values(?,?,?,?)";
                List<Object[]> batchArgs = new ArrayList<Object[]>();
                String time = DateUtil.getCurrentTime("yyyy-MM-dd");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject dataJson = jsonArray.getJSONObject(i);
                    String limitedTime = dataJson.getString("limitedTime");
                    String limitedWeek = dataJson.getString("limitedWeek");
                    String limitedNumber = dataJson.getString("limitedNumber");
                    batchArgs.add(new Object[]{limitedTime, limitedWeek, limitedNumber, time});
                }
                if (batchArgs != null && batchArgs.size() > 0) {
                    jdbcTemplate.batchUpdate(insertSql, batchArgs);
                    String delTime = DateUtil.subtractTime(24, "yyyy-MM-dd");
                    String delsql = "delete from wy_sys_limit where time='" + delTime + "'";
                    jdbcTemplate.update(delsql);
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getLimitQuart() {
        String time = DateUtil.getCurrentTime("yyyy年MM月dd日");
        String sql = "select limitedTime,limitedWeek,limitedNumber from wy_sys_limit  where limitedTime='" + time + "'";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
        return list;
    }


}