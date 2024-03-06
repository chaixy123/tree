package com.wanyun.select.controller;


import com.wanyun.select.service.FeedbackService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: FeedbackController
 * @Version 1.0.0
 */
@RestController
@RequestMapping("feedback")
public class FeedbackController {

    @Resource
    FeedbackService feedbackService;

    @RequestMapping(value = "/addFeedback")
    public Object addFeedback(@RequestParam Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            int dataList = feedbackService.addFeedback(map);
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

}