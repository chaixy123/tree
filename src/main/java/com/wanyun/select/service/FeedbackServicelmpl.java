package com.wanyun.select.service;

import com.wanyun.select.utils.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: FeedbackServicelmpl
 * @Version 1.0.0
 */
@Service
public class FeedbackServicelmpl implements  FeedbackService {
    @Resource
    JdbcTemplate jdbcTemplate;
    @Override
    public int addFeedback(Map<String, Object> map) {
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        String content = map.get("content") == null ? "" : map.get("content") + "";
        String sql="insert into sys_wy_feedback(openid,content,time) values('"+openid+"','"+content+"','"+ DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss") +"')";
        return jdbcTemplate.update(sql);
    }
}