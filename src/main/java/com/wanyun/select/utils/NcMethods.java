package com.wanyun.select.utils;

import java.text.DecimalFormat;

public class NcMethods {

    //风向的文字描述
    public  String getWords(double degrees) {
        if (degrees == 0) {
            return "无持续风向";
        } else if (0 < degrees && degrees < 90) {
            return "东北风";
        } else if (degrees == 90) {
            return "东风";
        } else if (90 < degrees && degrees < 180) {
            return "东南风";
        } else if (degrees == 180) {
            return "南风";
        } else if (180 < degrees && degrees < 270) {
            return "西南风";
        } else if (degrees == 270) {
            return "西风";
        } else if (270 < degrees && degrees < 360) {
            return "西北风";
        } else if (360 == degrees ) {
            return "北风";
        } else {
            return "风向超过360°，异常警告！";
        }
    }

    //UV数据计算风度
    public   double vectorToDegrees(float u,float v) {
        DecimalFormat df = new DecimalFormat("#.0"); //保留1位小数
        double windDirTrigTo = 0;//返回从原点(0,0)到(x,y)点的线段与x轴正方向之间的平面角度(弧度值)
        double windDirTrigToDegrees = 0;//角度
        double windDirTrigFromDegrees = 0; //极坐标角度、
        //360为正北风 ，0.0为无风
        if (u == 0 & v == 0) {
            return windDirTrigFromDegrees;
        } else {
            // u/v v不能为0
            windDirTrigTo = Math.atan2(v, u);  //JaMath.atan2() 返回从原点(0,0)到(x,y)点的线段与x轴正方向之间的平面角度(弧度值)， 按180度算，正负计算
            windDirTrigToDegrees = windDirTrigTo * 180 / Math.PI;     // Math.PI; atan2返回为弧度  通过弧度求角度   1弧度=(180/π)°
            windDirTrigFromDegrees = (270 - windDirTrigToDegrees + 180);      //+180 极坐标转换为是三角坐标
            if (windDirTrigFromDegrees > 360) {
                windDirTrigFromDegrees -= 360;
            }
            return Double.parseDouble(df.format(windDirTrigFromDegrees));
        }
    }


    //UV数据计算速度 m/s
    public  double getWind_speed(float v,float u) {
        DecimalFormat df = new DecimalFormat("#"); //保留整数
        double wind_speed = Math.sqrt(Math.pow(u, 2) + Math.pow(v, 2));//  Math.sqrt 平方根
        return Double.parseDouble(df.format(wind_speed));
    }
}
