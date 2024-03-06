package com.wanyun.select.service;


import com.wanyun.select.utils.DataUtil;
import com.wanyun.select.utils.DateUtil;
import com.wanyun.select.utils.NcMethods;
import com.wanyun.select.utils.WeatherUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: RmapsServicelmpl
 * @Version 1.0.0
 */
@Service
public class RmapsServicelmpl implements RmapsService {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public void getRmapsData() throws IOException {
        String deltime = DateUtil.subtractTime(72, "yyyyMMddHH");
        String delsql = "delete from sys_scenic_db where filetime<='" + deltime + "'";
        jdbcTemplate.update(delsql);
        String selectTime = DateUtil.subtractTime(24, "yyyyMMddHH");
        String sql = "select forecast_time,filetime from sys_scenic_db where  filetime>='" + selectTime + "'";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql);//查询文件时次
        Map<String, String> dataMap = DataUtil.listConvertMap(dataList, "forecast_time", "filetime");
        List<String> sqlList = new ArrayList<String>();
        String time = DateUtil.subtractTime(24, "yyyyMMddHH");
        String path = "E:/ftp/receive/rmaps/RMAPS-ST/";
        //String path = "D:/file/";
        File file = new File(path);
        File[] fileList = file.listFiles();//获取所有文件
        if (fileList != null && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {//循环获取数据库中不存在的文件
                String filename = fileList[i].getName();
                if (filename.indexOf("wrfprs_d01.") >= 0) {
                    String fileStr = filename.replace("wrfprs_d01.", "");
                    fileStr = fileStr.substring(0, fileStr.indexOf(".f"));
                    int fileTile = Integer.parseInt(fileStr);
                    int currentTime = Integer.parseInt(time);
                    if (fileTile >= currentTime) {
                        String sqlData = dataMap.get(filename);
                        if (sqlData == null || sqlData.isEmpty()) {
                            sqlList.add(filename);
                        }
                    }
                }
            }
            if (sqlList != null && sqlList.size() >= 0) {
                String sqls = "insert into sys_scenic_db(station_num,temp,rain,wind_level,wind_direction,vis,hum,weat,time,forecast_time,filetime)values(?,?,?,?,?,?,?,?,?,?,?)";
                List<Object[]> batchArgs = new ArrayList<Object[]>();
                sql = "select id,lat,log,x,y from wy_scenic";
                List<Map<String, Object>> scenicList = jdbcTemplate.queryForList(sql);//查询所有景点
                for (int i = 0; i < sqlList.size(); i++) {//循环文件获取要素
                    String filename = sqlList.get(i).substring(sqlList.get(i).indexOf(".f") + 2, sqlList.get(i).indexOf(".nc"));
                    if (filename.equals("00")) {
                        filename = "";
                    } else {
                        filename = Integer.parseInt(filename) + "h";
                    }
                    NetcdfFile ncFile = null;
                    try {
                        ncFile = NetcdfFile.open(path + "" + sqlList.get(i));
                        //Array vlat = ncFile.findVariable("NLAT_GDS3_SFC").read();//维度
                        //Array vlog = ncFile.findVariable("ELON_GDS3_SFC").read();//经度
                        Array vvis = ncFile.findVariable("VIS_GDS3_SFC").read();//能见度
                        Array vtemp = ncFile.findVariable("TMP_GDS3_HTGL").read();//温度
                        Array vhun = ncFile.findVariable("R_H_GDS3_HTGL").read();//相对湿度
                        Array vvwind = ncFile.findVariable("V_GRD_GDS3_HTGL").read();//v风
                        Array vuwind = ncFile.findVariable("U_GRD_GDS3_HTGL").read();//u风
                        Array vrain = ncFile.findVariable("A_PCP_GDS3_SFC_acc" + filename + "").read();//降雨
                        Array vsnow = ncFile.findVariable("WEASD_GDS3_SFC_acc" + filename + "").read();//降雪
                        Array vcloud = ncFile.findVariable("T_CDC_GDS3_EATM").read();//总云量
                        float[][] vis = (float[][]) vvis.copyToNDJavaArray();
                        float[][] temp = (float[][]) vtemp.copyToNDJavaArray();
                        float[][] hun = (float[][]) vhun.copyToNDJavaArray();
                        float[][] vwind = (float[][]) vvwind.copyToNDJavaArray();
                        float[][] uwind = (float[][]) vuwind.copyToNDJavaArray();
                        float[][] snow = (float[][]) vsnow.copyToNDJavaArray();
                        float[][] rain = (float[][]) vrain.copyToNDJavaArray();
                        float[][] cloud = (float[][]) vcloud.copyToNDJavaArray();
                        //开始处理数据
                        for (int j = 0; j < scenicList.size(); j++) {
                            Map<String, Object> scenicMapp = scenicList.get(j);
                            String id = scenicMapp.get("id") == null ? "" : scenicMapp.get("id") + "";
                            String x = scenicMapp.get("x") == null ? "" : scenicMapp.get("x") + "";
                            String y = scenicMapp.get("y") == null ? "" : scenicMapp.get("y") + "";
                            int indexx = Integer.parseInt(x);
                            int indexy = Integer.parseInt(y);
                            float temedata = temp[indexx][indexy];//温度
                            String num2 = String.format("%.1f", temedata - 273.15f);//温度
                            float visdata = vis[indexx][indexy];//能见度
                            float hundata = hun[indexx][indexy];//相对湿度
                            float vwinddata = vwind[indexx][indexy];//v风
                            float uwinddata = uwind[indexx][indexy];//u风
                            //double degrees = NcMethods.vectorToDegrees(uwinddata, vwinddata);//计算风向角度
                            // String words = NcMethods.getWords(degrees);//风向
                            //double wind_speed = NcMethods.getWind_speed(vwinddata, uwinddata); //计算过风速
                            // String windlevel = NcMethods.getWindLevel(wind_speed);//风力等级
                            float snowdata = snow[indexx][indexy];//降雪
                            float raindata = rain[indexx][indexy];//降雨
                            String rains = String.format("%.2f", raindata);//降雨处理完的数据
                            float clouddata = cloud[indexx][indexy];//总运量
                            String wart = "";
                            String snowWeather = WeatherUtil.getSnowWeather(snowdata);
                            String rainWeather = WeatherUtil.getRainWeather(raindata);
                            if (snowdata > 0 && raindata >= 0.1) {
                                wart = "雨加雪";
                            } else if (snowdata > 0 && raindata < 0.1) {
                                wart = snowWeather;
                            } else if (snowdata <= 0 && raindata >= 0.1) {
                                wart = rainWeather;
                            } else {//开始处理其他要素
                                if (hundata >= 90) {//相对湿度大于90
                                    // 然后读能见度，如果能见度在10km以上不是雾，10km以下有分级。如果相对湿度小于90%，就读云量，根据云量判断晴天多云和阴天。
                                    if (visdata < 10000) {
                                        wart = WeatherUtil.getFogWeather(visdata);
                                    } else {
                                        wart = "晴";
                                    }
                                } else {
                                    wart = WeatherUtil.getCloudWeather(clouddata);
                                }
                            }
                            int sc = 0;
                            if (filename.equals("") || filename.isEmpty()) {
                                sc = 0;
                            } else {
                                sc = Integer.parseInt(filename.replace("h", ""));
                            }
                            String filetime = sqlList.get(i).replace("wrfprs_d01.", "");
                            filetime = filetime.substring(0, filetime.indexOf(".f"));
                            String forecast = DateUtil.addhour(filetime, sc + 8, "yyyyMMddHH", "yyyy-MM-dd HH");
                            //处理预报时间
                            //  batchArgs.add(new Object[]{id, num2, rains, windlevel, words, visdata, hundata, wart, forecast, sqlList.get(i), filetime});
                        }
                    } catch (IOException | ParseException e) {
                    } finally {
                        ncFile.close();
                    }
                }
                if (batchArgs.size() > 0) {
                    jdbcTemplate.batchUpdate(sqls, batchArgs);
                }
            }
        }
    }


}