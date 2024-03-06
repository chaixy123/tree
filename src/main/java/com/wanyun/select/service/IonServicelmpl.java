package com.wanyun.select.service;

import com.wanyun.select.utils.DataUtil;
import com.wanyun.select.utils.DateUtil;
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
 * @Description: IonServicelmpl
 * @Version 1.0.0
 */
@Service
public class IonServicelmpl implements IonService {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public void addIon() throws IOException {
        String delsql = "delete from sys_wy_ion where time<='" + DateUtil.subtractTime(3, "yyyy-MM-dd HH:mm") + ":00'";
        jdbcTemplate.update(delsql);
        delsql = "OPTIMIZE TABLE sys_wy_station";
        jdbcTemplate.queryForList(delsql);
        String path = "E:/ftp/receive/ion";
        File file = new File(path);
        if (file.exists()) {
            String sql = "select station_num,lon,lat  from sys_wy_station";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            Map<String, String> sqlMap = DataUtil.listPackingMap(list, "station_num", "lon", "lat");
            File[] fileList = file.listFiles();
            String sqls = "insert into sys_wy_ion(station_num,ion,time,lon,lat)values(?,?,?,?,?)";
            List<Object[]> batchArgs = new ArrayList<Object[]>();
            for (int i = 0; i < fileList.length; i++) {
                String contrnt = readFile02(path + "/" + fileList[i].getName());
                String[] arr = contrnt.split("NNNN");
                for (int j = 0; j < arr.length; j++) {
                    String[] dataarr = arr[j].split(";");
                    String station = dataarr[0];
                    station = station.replace("staID=", "");
                    String time = dataarr[1];
                    time = time.replace("recTime=", "");
                    String num = dataarr[3];
                    num = num.substring(num.indexOf("FYLZ,") + 5, num.length() - 1);
                    String lonandLat = sqlMap.get(station) == null ? "" : sqlMap.get(station) + "";
                    if (!lonandLat.isEmpty()) {
                        String[] arrs = lonandLat.split("_");
                        batchArgs.add(new Object[]{station, num, time, arrs[0], arrs[1]});
                    }
                }
                fileList[i].delete();
            }
            if (batchArgs.size() > 0) {
                jdbcTemplate.batchUpdate(sqls, batchArgs);
            }
        }
    }
    
    //一行一行读取文本内容
    public static String readFile02(String path) throws IOException {
        String result = "";
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, "gbk");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        br.close();
        isr.close();
        fis.close();
        return result;
    }

}