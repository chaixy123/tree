package com.wanyun.select.service;

import com.wanyun.select.utils.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: IndexServicelmpl
 * @Version 1.0.0
 */
@Service
public class IndexServicelmpl implements IndexService {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public void getIndexData() throws IOException {
        String time = DateUtil.getCurrentTime("yyyy-MM-dd");
        String path = "E:/ftp/send/yidong/moving_index/" + time + "-ydzs24.txt";
        //String path = "D:/file/" + time + "-ydzs24.txt";
        addIndex(path, DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        String delTime = DateUtil.subtractTime(24, "yyyy-MM-dd HH:mm:ss");
        String sql = "delete from  sys_scenic_index where time<='" + delTime + "' ";
        jdbcTemplate.update(sql);
    }


    public void addIndex(String path, String time) throws IOException {
        File file = new File(path);
        if (file.exists()) {//判断指数文件是否存在
            List<String> list = new ArrayList<String>();
            //一行一行开始读取数据
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, "gbk");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            isr.close();
            fis.close();
            if (list.size() > 0) {
                String sql = "select areacode,name from wy_area  ";
                List<Map<String, Object>> sqlList = jdbcTemplate.queryForList(sql);
                if (sqlList != null && sqlList.size() > 0) {
                    String insertSql = "insert into sys_scenic_index(areaid,grade,descri,type,time)values(?,?,?,?,?)";
                    List<Object[]> batchArgs = new ArrayList<Object[]>();
                    for (int i = 0; i < sqlList.size(); i++) {//循环地区
                        String id = sqlList.get(i).get("areacode") == null ? "" : sqlList.get(i).get("areacode") + "";
                        String name = sqlList.get(i).get("name") == null ? "" : sqlList.get(i).get("name") + "";
                        String grade = "", describe = "", type = "", areaid = "";
                        for (int j = 0; j < list.size(); j++) {
                            String content = list.get(j);
                            if (content.indexOf("指数类型：") >= 0) {
                                type = content.replace("指数类型：", "");
                            }
                            if (content.indexOf("区域：") >= 0) {
                                String areaName = content.replace("区域：", "");
                                if (!areaName.equals("全市")) {
                                    if (name.indexOf(areaName) >= 0) {
                                        areaid = id;
                                    }
                                }
                            }

                            if (content.indexOf("适宜度: ") >= 0) {
                                grade = content.replace("适宜度: ", "");
                            }
                            if (content.indexOf("提示语：") >= 0) {
                                describe = content.replace("提示语：", "");
                            }

                            if (!grade.isEmpty() && !type.isEmpty() && !describe.isEmpty() && !areaid.isEmpty()) {
                                batchArgs.add(new Object[]{areaid, grade, describe, type, time});
                                grade = "";
                                describe = "";
                                type = "";
                                areaid = "";
                            }

                        }
                    }
                    if (batchArgs.size() > 0) {
                        jdbcTemplate.batchUpdate(insertSql, batchArgs);
                    }
                }
            }
        }
    }
}