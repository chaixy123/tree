package com.wanyun.select.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanyun.select.entity.View;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class NcJson {

    //nc转json---------集合转json

    public static JSONObject testJson( View view,View view1) throws IOException {

        //1.4添加 u_wind  对象信息
        JSONObject u_header = new JSONObject();
        u_header.put("refTime",view.getRefTime() );
        u_header.put("lo2", view.getLo2());
        u_header.put("lo1",view.getLo1());
        u_header.put("dx", view.getDx());
        u_header.put("dy", view.getDy());
        u_header.put("ny", view.getNy());
        u_header.put("nx", view.getNx());
        u_header.put("parameterNumberName", view.getParameterNumberName());
        u_header.put("parameterCategory", view.getParameterCategory());
        u_header.put("parameterCategoryName",view.getParameterCategoryName());  //增加属性
        u_header.put("parameterNumber", 3);
        u_header.put("la1", view.getLa1());
        u_header.put("la2",view.getLa2());
        u_header.put("parameterUnit", view.getParameterUnit());

        //2.将两个属性 data、header添加到json对象中
        //2.1水平风
        JSONObject object1 = new JSONObject();
        object1.put("header", u_header);
        object1.put("data", view.getData());
        //3.将对象添加到json数组content中
        JSONArray objects = new JSONArray();
        objects.add(object1);
        if(view1!=null){
            JSONObject v_header = new JSONObject();
            v_header.put("refTime",view.getRefTime() );
            v_header.put("lo2", view1.getLo2());
            v_header.put("lo1",view1.getLo1());
            v_header.put("dx", view1.getDx());
            v_header.put("dy", view1.getDy());
            v_header.put("ny", view1.getNy());
            v_header.put("nx", view1.getNx());
            v_header.put("parameterNumberName", view1.getParameterNumberName());
            v_header.put("parameterCategory", view1.getParameterCategory());
            v_header.put("parameterCategoryName",view1.getParameterCategoryName());  //增加属性
            v_header.put("parameterNumber", 2);
            v_header.put("la1", view1.getLa1());
            v_header.put("la2",view1.getLa2());
            v_header.put("parameterUnit", view1.getParameterUnit());
           /* String data1 = view1.getData();
            String[] split1 = data1.split(",");*/
            JSONObject object2 = new JSONObject();
            object2.put("header", v_header);
            object2.put("data", view1.getData());
            objects.add(object2);
        }
        //4.添加DS JSON对象
        JSONObject ds = new JSONObject();
        ds.put("content", objects);
        ds.put("times", view.getRefTime().substring(9));
        ds.put("D_DATETIME",view.getRefTime());
        ds.put("filename", view.getFileName());
        ds.put("prodate", view.getRefTime());

        //完善整体json对象属性
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("returnCode", "0");
        jsonObject.put("DS", ds);
        if (view.getVariable().equals("TMP_GDS3_HTGL")){
           //求温度极值（除0外排序）
            ArrayList<Float> data = view.getData();
            // 使用HashSet去掉重复
            Set<Float> set = new HashSet<Float>(data);
            // 得到去重后的新集合
            ArrayList<Float> newList = new ArrayList<Float>(set);
            //排序（升）
            Collections.sort(newList);
            Float min = newList.get(1);
            Float max = newList.get(newList.size() - 1);
            jsonObject.put("max",max);
            jsonObject.put("min",min);
        }
        return jsonObject;
    }


}
