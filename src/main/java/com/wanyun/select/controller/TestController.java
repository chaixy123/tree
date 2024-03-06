package com.wanyun.select.controller;

import com.wanyun.select.service.DataSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    private DataSelectService dataSelectService;
    @GetMapping(value = "/start")
    public void start() throws Exception {
        dataSelectService.view();
    }
    @GetMapping("/yidong")
    public void yidong() throws IOException, InterruptedException {
        dataSelectService.setAreaData();
    }
}
