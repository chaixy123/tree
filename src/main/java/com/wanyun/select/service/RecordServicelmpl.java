package com.wanyun.select.service;


import com.alibaba.fastjson.JSONObject;
import com.wanyun.select.utils.DateUtil;
import com.wanyun.select.utils.DistanceUtil;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: RecordServicelmpl
 * @Version 1.0.0
 */
@Service
public class RecordServicelmpl implements RecordService {

    @Resource
    JdbcTemplate jdbcTemplate;


    @Override
    public List<Map<String, Object>> getRecordByOpenid(Map<String, Object> map) throws ParseException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        String lat = map.get("lat") == null ? "" : map.get("lat") + "";//经度
        String log = map.get("lon") == null ? "" : map.get("lon") + "";//维度
        String sql = "select careaTime from  sys_user_visit where userid='" + openid + "' order by careaTime desc";
        List<Map<String, Object>> visitList = jdbcTemplate.queryForList(sql);
        List<String> visitMap = new ArrayList<String>();
        if (visitList != null && visitList.size() > 0) {
            String dataTime = "";
            for (int i = 0; i < visitList.size(); i++) {
                Map<String, Object> collMap = visitList.get(i);
                String careaTime = collMap.get("careaTime") == null ? "" : collMap.get("careaTime") + "";
                careaTime = DateUtil.tranTime(careaTime, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日");
                if (dataTime.indexOf(careaTime) < 0) {
                    if (dataTime.isEmpty()) {
                        dataTime = careaTime;
                    } else {
                        dataTime += "," + careaTime;
                    }
                    visitMap.add(careaTime);
                }
            }
            sql = " SELECT scenic.id,scenic.NAME,scenic.ename,scenic.bsTime,scenic.tel," +
                    " scenic.visittime,scenic.lat,scenic.log,scenic.grade,scenic.addr," +
                    " scenic.`into`,scenic.url,scenic.way,scenic.type,state," +
                    " ( SELECT paht FROM sys_wy_scenicimg img WHERE img.scenicid = scenic.id GROUP BY img.scenicid ) img," +
                    " visit.id visitId, visit.careaTime FROM sys_user_visit visit join wy_scenic  scenic  on visit.scenicId=scenic.id where visit.userid='" + openid + "' order by  visit.careaTime desc";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            if (results != null && results.size() > 0) {
                for (int i = 0; i < visitMap.size(); i++) {
                    String time = visitMap.get(i);
                    JSONObject jsonObject = new JSONObject();
                    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                    for (int j = 0; j < results.size(); j++) {
                        Map<String, Object> mapdata = results.get(j);
                        String lats = mapdata.get("lat") == null ? "" : mapdata.get("lat") + "";
                        String logs = mapdata.get("log") == null ? "" : mapdata.get("log") + "";
                        String careaTime = mapdata.get("careaTime") == null ? "" : mapdata.get("careaTime") + "";
                        careaTime = DateUtil.tranTime(careaTime, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日");
                        if (time.equals(careaTime)) {
                            GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(lats), Double.parseDouble(logs));
                            GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(lat), Double.parseDouble(log));
                            double meter2 = DistanceUtil.getDistanceMeter(source, target, Ellipsoid.WGS84);//获取84坐标距离
                            mapdata.put("distance", meter2);
                            dataList.add(mapdata);
                        } else {
                            continue;
                        }
                    }
                    if (dataList != null && dataList.size() > 0) {
                        jsonObject.put("time", time);
                        jsonObject.put("data", dataList);
                        result.add(jsonObject);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int deltRecord(Map<String, Object> map) {
        String visitId = map.get("visitId") == null ? "" : map.get("visitId") + "";
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        String time = map.get("careaTime") == null ? "" : map.get("careaTime") + "";
        int result = 0;
        String[] arr = visitId.split(",");
        String[] timearr = time.split(",");
        for (int i = 0; i < arr.length; i++) {
            String sql = "delete from sys_user_visit where scenicId='" + arr[i] + "' and userid='" + openid + "' and careaTime='" + timearr[i] + "'";
            jdbcTemplate.update(sql);
            result++;
        }
        return result;
    }

    @Override
    public int getRecordNum(Map<String, Object> map) {
        String openid = map.get("openid") == null ? "" : map.get("openid") + "";
        if (openid.isEmpty()) {
            return 0;
        } else {
            String sql = "select careaTime from  sys_user_visit where userid='" + openid + "'  order by careaTime desc";
            List<Map<String, Object>> visitList = jdbcTemplate.queryForList(sql);
            return visitList.size();
        }
    }


}