package com.wanyun.select.utils;


import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;

//适用于St d01文件


public class NcVariable {

//    提前加载U风数据
    public  static float[] getU_wind(NetcdfFile openNC)  {

        Variable u10 = openNC.findVariable("U_GRD_GDS3_HTGL");
        float [] arr_u=null;
        try {
             arr_u= (float[])u10.read().copyTo1DJavaArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr_u;
    }
//    提前加载V风数据
    public static float[] getV_wind(NetcdfFile openNC)  {

        Variable v10 = openNC.findVariable("V_GRD_GDS3_HTGL");
        float [] arr_v = null;
        try {
            arr_v= (float[])v10.read().copyTo1DJavaArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr_v;
    }
//    提前加载维度数据
    public static float[]  getXlat(NetcdfFile openNC)  {

        Variable xlat = openNC.findVariable("NLAT_GDS3_SFC");
        float [] lats=new float[0];
        try {
            lats= (float[])xlat.read().copyTo1DJavaArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lats;
    }
//    提前加载经度数据
    public static float[] getXlong(NetcdfFile openNC)  {

        Variable xlong = openNC.findVariable("ELON_GDS3_SFC");
        float [] lons=new float[0];
        try {
            lons= (float[])xlong.read().copyTo1DJavaArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lons;
    }
//    文件预报时间
    public String getTimes(NetcdfFile openNC){
        Variable times = openNC.findVariable("Times");
        String str = new String();
        try {
            str = times.read().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    // 降水
    public static float [] rain(NetcdfFile openNC)  {
        Variable variable = openNC.findVariable("A_PCP_GDS3_SFC_acc12h");

        float [] floats = new float[0];
        try {
            floats = (float[])variable.read().copyTo1DJavaArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return floats;
    }

   /* *  根据变量名称查询变量数据
    *   params: variable变量名， lat,lon经纬度
    *   方法描述：查询出数据， 查询经纬度对应格点坐标， 查询对应数据,返回
    *   return float date
    *
    */
    public static float getVariable(NetcdfFile openNC,String variable,float lat,float lon,int center) throws IOException {
        Variable variable1 = openNC.findVariable(variable);
        float [] floats = (float[])variable1.read().copyTo1DJavaArray();
        //int index = NcUtil.getcenter(openNC,lat,lon);
        //对应格点数据
        float data = floats[center];
        return data;
    }

    public static void main(String[] args) {

    }

}
