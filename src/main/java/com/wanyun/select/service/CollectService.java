package com.wanyun.select.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cxy
 * @Description: CollectService
 * @Version 1.0.0
 */
public interface CollectService {

    List<Map<String, Object>> getCollectList(Map<String, Object> map) throws ParseException;

    int addCollect(Map<String, Object> map);
    int getCollectNum(Map<String, Object> map);
    int delCollect(Map<String, Object> map);
}
