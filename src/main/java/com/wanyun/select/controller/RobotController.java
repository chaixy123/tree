package com.wanyun.select.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanyun.select.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: cxy
 * @Description: RobotController
 * @Version 1.0.0
 */
@RestController
@RequestMapping("Robot")
public class RobotController {
    @Value("${Robkey}")
    private String robkey;

    @Value("${RobSecret}")
    private String robSecret;
    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getRobot")
    public Object getBjEarlyList(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String sql = "select token from sys_wy_rob_token ";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            if (list != null && list.size() > 0) {
                String token = list.get(0).get("token") + "";
                String session = map.get("session") == null ? "" : map.get("session") + "";
                String content = map.get("content") == null ? "" : map.get("content") + "";
                if (session.isEmpty()) {
                    UUID uuid = UUID.randomUUID();
                    session = uuid.toString();
                }
                String talkUrl = "https://aip.baidubce.com/rpc/2.0/unit/service/v3/chat";
                String params = "{\"version\":\"3.0\",\"service_id\":\"S87225\",\"session_id\":\"" + session + "\",\"log_id\":\"7758521\",\"request\":{\"terminal_id\":\"88888\",\"query\":\"" + content + "\"}}";
                String resulst = HttpUtil.post(talkUrl, token, "application/json", params);
                JSONObject jsonObject = JSONObject.parseObject(resulst);
                String error_code = jsonObject.getString("error_code");
                if (error_code.equals("0")) {
                    JSONObject data = JSONObject.parseObject(jsonObject.getString("result"));
                    JSONObject jsonObject2 = JSONObject.parseObject(data.getString("context"));
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject2.getString("SYS_PRESUMED_HIST"));
                    String contents = (String) jsonArray.get(jsonArray.size() - 1);
                    result.put("code", 200);
                    result.put("msg", "查询成功");
                    result.put("data", contents);
                    result.put("session", session);
                } else {
                    result.put("code", 201);
                    result.put("msg", "系统异常");
                }

            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "查询异常");
            return result;
        }
    }


    private String utterance() {
        // 请求URL
        String talkUrl = "https://aip.baidubce.com/rpc/2.0/unit/service/v3/chat";
        try {
            // 请求参数
            String params = "{\"version\":\"3.0\",\"service_id\":\"S87225\",\"session_id\":\"123\",\"log_id\":\"7758521\",\"request\":{\"terminal_id\":\"88888\",\"query\":\"现在干什么去\"}}";
            String accessToken = "24.de8822d8a4585d1f54f624972d68437e.2592000.1693701681.282335-32256602";
            String result = HttpUtil.post(talkUrl, accessToken, "application/json", params);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}