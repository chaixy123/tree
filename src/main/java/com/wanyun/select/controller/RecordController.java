package com.wanyun.select.controller;


import com.wanyun.select.service.RecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: RecordController
 * @Version 1.0.0
 */
@RestController
@RequestMapping("record")
public class RecordController {

    @Resource
    RecordService recordService;

    @RequestMapping(value = "/getRecordByOpenid")
    public Object getRecordByOpenid(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> dataList = recordService.getRecordByOpenid(map);
            int num = recordService.getRecordNum(map);
            result.put("num", num);
            result.put("data", dataList);
            result.put("code", 200);
            result.put("msg", "查询成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "查询异常");
            return result;
        }
    }

    @RequestMapping(value = "/delRecord")
    public Object delRecord(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            int dataList = recordService.deltRecord(map);
            result.put("data", dataList);
            result.put("code", 200);
            result.put("msg", "删除成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "系统异常");
            return result;
        }
    }


}