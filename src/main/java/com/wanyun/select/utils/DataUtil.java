package com.wanyun.select.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {
    //把集合转换成map
    public static Map<String, Map<String, Object>> conversionMap(List<Map<String, Object>> list, String name) {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dataMap = list.get(i);
                String type = dataMap.get(name) + "";
                result.put(type, dataMap);
            }
        }
        return result;
    }

    //集合转换map
    public static Map<String, Map<String, Object>> conversionMap(List<Map<String, Object>> list, String name, String name1) {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> dataMap = list.get(i);
            String type = dataMap.get(name) + "";
            String type1 = dataMap.get(name1) + "";
            result.put(type + type1, dataMap);
        }
        return result;
    }

    public static Map<String, List<Map<String, Object>>> getDataMap(List<Map<String, Object>> stationList, List<Map<String, Object>> list) {
        Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
        for (int i = 0; i < stationList.size(); i++) {
            Map<String, Object> stationMap = stationList.get(i);
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String station_code = stationMap.get("station_code") + "";
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> dataMap = list.get(j);
                String code = dataMap.get("ZDID") + "";
                if (station_code.equals(code)) {//站点一样封装
                    dataList.add(dataMap);
                }
            }
            result.put(station_code, dataList);
        }
        return result;
    }

    public static Map<String, List<Map<String, Object>>> conversionMap(List<Map<String, Object>> stationList, List<Map<String, Object>> list) {
        Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
        for (int i = 0; i < stationList.size(); i++) {
            Map<String, Object> stationMap = stationList.get(i);
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String station_code = stationMap.get("station_code") + "";
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> dataMap = list.get(j);
                String code = dataMap.get("code") + "";
                String time = dataMap.get("time") + "";
                if (station_code.equals(code)) {//站点一样封装
                    dataList.add(dataMap);
                }
            }
            if (dataList != null && dataList.size() > 0) {
                result.put(station_code, dataList);
            }
        }
        return result;
    }

    //月
    public static Map<String, List<Map<String, Object>>> monthonMap(List<Map<String, Object>> stationList, List<Map<String, Object>> list) {
        Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
        for (int i = 0; i < stationList.size(); i++) {
            Map<String, Object> stationMap = stationList.get(i);
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String station_code = stationMap.get("station_code") + "";
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> dataMap = list.get(j);
                Map<String, Object> dataMaps = new HashMap<String, Object>();
                String code = dataMap.get("code") + "";
                String time = dataMap.get("time") + "";
                String num = dataMap.get("1h") == null ? "" : dataMap.get("1h") + "";
                if (station_code.equals(code)) {//站点一样封装
                    dataMaps.put(time, num);
                    dataList.add(dataMaps);
                }
            }
            if (dataList != null && dataList.size() > 0) {
                result.put(station_code, dataList);
            }
        }
        return result;
    }


    public static Map<String, String> listConvertMap(List<Map<String, Object>> list, String key, String value) {
        Map<String, String> result = new HashMap<String, String>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dataMap = list.get(i);
                String type = dataMap.get(key) == null ? "" : dataMap.get(key) + "";
                String num = dataMap.get(value) == null ? "" : dataMap.get(value) + "";
                result.put(type, num);
            }
        }
        return result;
    }


    public static Map<String, Map<String, Object>> listOrMap(List<Map<String, Object>> list) {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        ;
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> stationMap = list.get(i);
            String station_code = stationMap.get("Sj") + "";
            result.put(station_code, stationMap);
        }
        return result;
    }


    public static Map<String, List<Map<String, Object>>> getDatasMap(List<Map<String, Object>> stationList, List<Map<String, Object>> list) {
        Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
        for (int i = 0; i < stationList.size(); i++) {
            Map<String, Object> stationMap = stationList.get(i);
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String station_code = stationMap.get("station_code") + "";
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> dataMap = list.get(j);
                String code = dataMap.get("station_code") + "";
                if (station_code.equals(code)) {//站点一样封装
                    dataList.add(dataMap);
                }
            }
            result.put(station_code, dataList);
        }
        return result;
    }

    public static Map<String, String> listPackingMaps(List<Map<String, Object>> list, String key, String key1, String value) {
        Map<String, String> result = new HashMap<String, String>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dataMap = list.get(i);
                String name = dataMap.get(key) == null ? "" : dataMap.get(key) + "";
                String name1 = dataMap.get(key1) == null ? "" : dataMap.get(key1) + "";
                String values = dataMap.get(value) == null ? "" : dataMap.get(value) + "";
                result.put(name + "_" + name1, values);
            }
        }
        return result;
    }

    public static Map<String, String> listPackingMapa(List<Map<String, Object>> list, String key, String key1, String value, String value1) {
        Map<String, String> result = new HashMap<String, String>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dataMap = list.get(i);
                String name = dataMap.get(key) == null ? "" : dataMap.get(key) + "";
                String name1 = dataMap.get(key1) == null ? "" : dataMap.get(key1) + "";
                String values = dataMap.get(value) == null ? "" : dataMap.get(value) + "";
                String values1 = dataMap.get(value1) == null ? "" : dataMap.get(value1) + "";
                result.put(name + "_" + name1, values + "_" + values1);
            }
        }
        return result;
    }

    public static Map<String, String> listPackingMap(List<Map<String, Object>> list, String key, String value, String value1) {
        Map<String, String> result = new HashMap<String, String>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dataMap = list.get(i);
                String name = dataMap.get(key) == null ? "" : dataMap.get(key) + "";
                String values = dataMap.get(value) == null ? "" : dataMap.get(value) + "";
                String values1 = dataMap.get(value1) == null ? "" : dataMap.get(value1) + "";
                result.put(name, values + "_" + values1);
            }
        }
        return result;
    }


    public static Map<String, String> listPackingMap(List<Map<String, Object>> list, String key, String value) {
        Map<String, String> result = new HashMap<String, String>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> dataMap = list.get(i);
                String name = dataMap.get(key) == null ? "" : dataMap.get(key) + "";
                String values = dataMap.get(value) == null ? "" : dataMap.get(value) + "";
                result.put(name, values);
            }
        }
        return result;
    }

}
