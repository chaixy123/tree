package com.wanyun.select.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyun.select.entity.View;
import com.wanyun.select.service.DataSelectService;
import org.springframework.stereotype.Component;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.stream.LongStream;

/**
 * @author 李帅明
 * @version 1.0
 * @date 2022/3/14 13:59
 */
@Component
public class ThreadPool {

    public enum Variables {
        temp, wind;
    }

    public void getPool(List<String> list) {
        //开始计算线程
        int fornum = 0;
        if (list.size() % 1 == 0) {//一万的整数倍
            fornum = list.size() / 1;
        } else {
            fornum = (list.size() / 1) + 1;
        }
        int maximumPoolSize = 1;//最大线程
        int corePoolSize = 1;//最小维护线程
        maximumPoolSize = fornum;//最大线程数
        if (maximumPoolSize <= 5) {//核心线程数最多5个
            corePoolSize = maximumPoolSize;
        } else {
            corePoolSize = 5;
        }
        //开启多线程处理
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(maximumPoolSize));//线程池最少维护1个线程，最多5个。当线程空闲时间达到5000毫秒，该线程会退出，缓冲队列为5。
        List<Future<Integer>> futures = new ArrayList<Future<Integer>>();

        for (int j = 0; j < fornum; j++) {
            //int end = (j+1)*1 > list.size() ? list.size() : (j+1)*1;
            ProgramThread task = new ProgramThread(list.get(j));
            Future<Integer> future = executor.submit(task);
            futures.add(future);
        }
    }
    public class ProgramThread implements Callable<Integer> {

        private String path;

        public ProgramThread(String path) {
            this.path = path;
        }

        @Override
        public Integer call() throws Exception {
            //DataSelectService dataSelectService= (DataSelectService) SpringUtils.getBean(DataSelectService.class);
            //数据库批量插入
            //System.out.println("进入多线程处理数据方法————————————————");
            System.out.println("解析文件"+path);
            String origin_path = null;  //路径+当天文件夹+变量名+时间戳（到小时）
            JSONObject object = null;
            for (Variables variables : Variables.values()) {
                if (variables.name().equals("wind")) {
                    View u_wind = new CreateNetCDF_2D_TEMP().view(path, "u_wind");
                    View v_wind = new CreateNetCDF_2D_TEMP().view(path, "v_wind");
                    object = NcJson.testJson(u_wind, v_wind);
                    origin_path = "E:/ftp/prodect/view/WIND"; //E:/ftp/prodect/view
                } else {
                    View view = new CreateNetCDF_2D_TEMP().view(path, variables.name());
                    object = NcJson.testJson(view, null);
                    origin_path = "E:/ftp/prodect/view/" + view.getVariable();
                }
                String substring = path.substring(path.indexOf("d01") + 4, path.lastIndexOf("f") - 1); //预报时间戳（小时）
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH", Locale.CHINA);
                LocalDateTime ldt = LocalDateTime.parse(substring, dateTimeFormatter);
                String substring1 = path.substring(path.lastIndexOf("f") + 1, path.lastIndexOf("f") + 3);//预报时效
                int number = Integer.parseInt(substring1);
                LocalDateTime plus = ldt.plus(number + 8, ChronoUnit.HOURS);//转换成世界时
                String format = plus.format(dateTimeFormatter); //世界时
                Files.createDirectories(Paths.get(origin_path)); //创建文件夹
                File file = new File(origin_path + "/" + format + ".json");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(JSON.toJSONString(object));
                bufferedWriter.close();
            }
            int result = 0;
            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:/wanyun/test.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("dfg！");
        bufferedWriter.close();
    }

}
