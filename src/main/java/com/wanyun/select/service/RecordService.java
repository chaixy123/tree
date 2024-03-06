package com.wanyun.select.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: RecordService
 * @Version 1.0.0
 */
public interface RecordService {

    List<Map<String, Object>> getRecordByOpenid(Map<String, Object> map) throws ParseException;

    int deltRecord(Map<String, Object> map);
    int getRecordNum(Map<String, Object> map);
}
