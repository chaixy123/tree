package com.wanyun.select.controller;


import com.wanyun.select.service.CollectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: CollectController
 * @Version 1.0.0
 */
@RestController
@RequestMapping("coll")
public class CollectController {
    @Resource
    CollectService collectService;

    @RequestMapping(value = "/getCollList")
    public Object getCollList(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> dataList = collectService.getCollectList(map);
            int num=collectService.getCollectNum(map);
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

    @RequestMapping(value = "/addColl")
    public Object addColl(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            int reuslts = collectService.addCollect(map);
            if (reuslts > 0) {
                result.put("code", 200);
                result.put("msg", "添加成功");
            } else {
                result.put("code", 201);
                result.put("msg", "系统异常");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "系统异常");
            return result;
        }
    }

    @RequestMapping(value = "/delColl")
    public Object delColl(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            int reuslts = collectService.delCollect(map);
            if (reuslts > 0) {
                result.put("code", 200);
                result.put("msg", "删除成功");
            } else {
                result.put("code", 201);
                result.put("msg", "系统异常");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "系统异常");
            return result;
        }
    }


}