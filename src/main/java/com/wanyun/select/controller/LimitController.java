package com.wanyun.select.controller;


import com.wanyun.select.service.LimitService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: LimitController
 * @Version 1.0.0
 */
@RestController
@RequestMapping("limit")
public class LimitController {
    @Resource
    LimitService limitService;

    @RequestMapping(value = "/getLimitList")
    public Object getBjEarlyList(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> dataList = limitService.getLimitQuart();
            result.put("data", dataList);
            result.put("code", 200);
            result.put("msg", "查询成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "查询异常");
            return result;
        }
    }
}