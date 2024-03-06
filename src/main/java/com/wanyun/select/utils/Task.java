package com.wanyun.select.utils;

import com.wanyun.select.service.DataSelectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;


/**
 * @programe: golf
 * @Description: 定时任务
 */
@Component
@Slf4j
public class Task {

    @Autowired
    private DataSelectService dataSelectService;

    @Scheduled(cron="0 30 * * * ? ")   //每2小时更新
    public void excute1() throws Exception {
        dataSelectService.view();
    }

    @Scheduled(cron = "5 0 * * * ?")
    public void execute1() throws IOException, InterruptedException {

        dataSelectService.setAreaData();


    }
}
