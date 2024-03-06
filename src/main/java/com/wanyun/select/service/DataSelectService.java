package com.wanyun.select.service;

import com.google.gson.Gson;
import com.wanyun.select.utils.NcMethods;
import com.wanyun.select.utils.NcVariable;
import com.wanyun.select.utils.ThreadPool;
import com.wanyun.select.utils.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import org.apache.commons.collections4.map.LinkedMap;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * @version 1.0
 */
@Service
@Slf4j
public class DataSelectService {

    ArrayList<Integer> jiaoList = null; //经纬度交集//北京地区自动站经纬度集合

    //瑞图数据批量判断
    public int getNumber(int hour){
        switch (hour){
            case 0:
                return 49;
            case 3:
                return 73;
            case 6:
                return 25;
            case 9:
                return 25;
            case 12:
                return 97;
            case 15:
                return 25;
            case 18:
                return 73;
            case 21:
                return 25;
            default:
                return 19;
        }
    }

    //遍历目录
    public static Stream<Path> selectFiles(String path, String str) {

        Path p1 = Paths.get(path); //文件存储路径
        BiPredicate<Path, BasicFileAttributes> pre =
                (p, u) -> p.getFileName().toString().contains(str);
        //搜索文件树中满足要求的文件
        Stream<Path> ps = null;
        try {
            ps = Files.find(p1, 2, pre); //参数： p1文件存储路径 maxDepth：遍历深度  pre：检索条件 ，ps符合条件的文件信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ps;
    }

    //开放平台可视化数据收集
    public void view() throws Exception {
        //获取文件路径
        ArrayList<String> paths = new ArrayList<>();  //存储文件路径
        ArrayList<LinkedHashMap> list = new ArrayList(); //存储预报对象
        String path = "E:\\ftp\\receive\\rmaps\\RMAPS-ST"; //存储路径 //E:\ftp\receive\rmaps\RMAPS-ST
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH", Locale.CHINA);
        String format = localDateTime.format(dateTimeFormatter);
        String s1 = "d01." + format; //查询关键词
        //根据文件名称关键词查询文件
        Stream<Path> ps1 = selectFiles(path, s1);
        ps1.forEach(p -> paths.add(p.toAbsolutePath().toString().replace('\\', '/')));
        ps1.close();
        LocalDateTime localDateTime1=localDateTime.minusDays(3);
        while ( localDateTime.isAfter(localDateTime1) && paths.size() == 0) {
            //包含关键字的文件不存在，localDateTime小时数减一，遍历
            localDateTime = localDateTime.minus(1, ChronoUnit.HOURS);
            format = localDateTime.format(dateTimeFormatter);
            s1 = "d01." + format; //拼接文件路径  查询关键词
            Stream<Path> ps = selectFiles(path, s1);
            ps.forEach(p -> paths.add(p.toAbsolutePath().toString().replace('\\', '/'))); //遍历从ps中获取文件路径（并做简单处理），result: p 文件路径集合
            ps.close();
        }
        if (paths.size()==0){
            System.out.println("服务器三天已没有数据");
        }else {
            new ThreadPool().getPool(paths);
        }
    }

    /**

     * @Params  * @param null:
     * @return  * @return: null
     **/
    public ArrayList<LinkedMap<String, Object>> getAreaData() throws IOException, InterruptedException {

        ArrayList<String> paths = new ArrayList<>();  //所有存储文件路径
        ArrayList< LinkedMap<String, Object>  > list = new ArrayList<>(); //存储天气数据
        String folder = "E:/ftp/receive/rmaps/RMAPS-ST";//C:/Users/adminis/Deskto  E:\ftp\receive\rmaps\RMAPS-ST
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.minusHours(8).withMinute(0).withSecond(0); //获取当前时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH", Locale.CHINA);
        String format = localDateTime.format(dateTimeFormatter);
        String s1 = "d01."+format; //关键词
        Stream<Path> ps1 = selectFiles(folder, s1);
        ps1.forEach(p -> paths.add(p.toAbsolutePath().toString().replace('\\', '/')));
        //截至时间
        LocalDateTime localDateTime1 = now.minusDays(3);
        while ( localDateTime.isAfter(localDateTime1) && paths.size() == 0) {   // 确定最新时段
            localDateTime = localDateTime.minus(1, ChronoUnit.HOURS);
            format = localDateTime.format(dateTimeFormatter);
            s1 = "d01." + format; //拼接文件路径  查询关键词
            Stream<Path> ps = selectFiles(folder, s1);
            ps.forEach(p -> paths.add(p.toAbsolutePath().toString().replace('\\', '/'))); //遍历从ps中获取文件路径（并做简单处理），result: p 文件路径集合
            ps.close();
        }
        //数据穿透判定
        if (paths.size()==0){
            System.out.println("服务器三天内没有文件");
            log.error("数据异常: 服务器三天内没有文件");
            return null;
        }else {
            int number1 = getNumber(localDateTime.getHour());//文件批次
            now =now.plusMinutes(50);
            while ( now.isAfter(LocalDateTime.now()) && paths.size() != number1 ){
                paths.clear();
                Thread.sleep(2*60*1000);
                Stream<Path> ps = selectFiles(folder, s1);
                ps.forEach(p -> paths.add(p.toAbsolutePath().toString().replace('\\', '/')));
                ps.close();
            }
            long l = LocalDateTime.now().minusHours(8).toEpochSecond(ZoneOffset.of("+8"));
            long l1 = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
            long l2 = l - l1;
            log.warn(format+"批次数据延迟"+(l2/3600)+"时"+(l2 % 3600) /60 +"分"+(l2 % 3600) % 60+"秒");
            //解析数据
            log.info("创建nc文件对象，开启nc文件操作流");
            NetcdfFile openNC = null;
            for (String s : paths) {
                if (s.contains("f00.nc")){
                   continue;
                }
                try {
                    System.out.println("预报文件" + s + "开始解析");
                    log.info("解析的文件"+s);
                    //单个文件-----单要素的map-----每个文件map更新
                    LinkedHashMap<float[], Object> rainTreeMap = new LinkedHashMap<>();
                    LinkedHashMap<float[], Object> windTreeMap = new LinkedHashMap<>();
                    LinkedHashMap<float[], Object> tempTreeMap = new LinkedHashMap<>();
                    LinkedHashMap<float[], Object> rhTreeMap = new LinkedHashMap<>();
                    LinkedHashMap<float[], Object> pTreeMap = new LinkedHashMap<>();
                    LinkedHashMap<float[], Object> weatherTreeMap = new LinkedHashMap<>();
                    LinkedMap<String, Object> map = new LinkedMap<>();   //单个文件-----6要素的map-----每个文件map更新

                    openNC = NetcdfFile.open(s);
                    float[] xlat = NcVariable.getXlat(openNC);
                    float[] xlong = NcVariable.getXlong(openNC);
                    //1.确定位置
                    if (jiaoList == null) {
                        ArrayList<Integer> latList = new ArrayList<>();
                        ArrayList<Integer> lonList = new ArrayList<>();
                        for (int i = 0; i < xlat.length; i++) {
                            if (xlat[i] >= 39.4f && xlat[i] <= 41.6f) {
                                latList.add(i);
                            }
                        }
                        for (int j = 0; j < xlong.length; j++) {
                            if (xlong[j] >= 115.7f && xlong[j] <= 117.4f) {
                                lonList.add(j);
                            }
                        }
                        jiaoList = (ArrayList<Integer>) CollectionUtils.intersection(latList, lonList); //求交集
                    }
                    //2.取数据值
                    String substring1 = s.substring(s.lastIndexOf("d01") + 4, s.lastIndexOf("f") - 1);//拼接预报时间  年月日时
                    String substring = s.substring(s.lastIndexOf("f") + 1, s.lastIndexOf("f") + 3);
                    int number = Integer.parseInt(substring);  //预报时效
                    // 将字符串解析成日期时间对象，并返回
                    LocalDateTime ldt = LocalDateTime.parse(substring1, dateTimeFormatter);
                    ldt = ldt.plus(number + 8, ChronoUnit.HOURS);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.CHINA);
                    String str = ldt.format(df);
                    //维度和数据绑定
                    for (Integer center : jiaoList) //单个文件-----407个点---------
                    {
                        float data = 0;
                        double snow = 0;
                        if (number == 0) {
                            //降水 kg/m^2
                            Variable a_pcp_gds3_sfc_acc = openNC.findVariable("A_PCP_GDS3_SFC_acc");
                            float[] floats = (float[]) a_pcp_gds3_sfc_acc.read().copyTo1DJavaArray();
                            data = floats[center];
                            //降雪
                            Variable weasd_p0_l1_gll0 = openNC.findVariable("WEASD_GDS3_SFC_acc");
                            float[] floats7 = (float[]) weasd_p0_l1_gll0.read().copyTo1DJavaArray();
                            snow = floats7[center];
                        } else {
                            Variable a_pcp_gds3_sfc_acc = openNC.findVariable("A_PCP_GDS3_SFC_acc" + number + "h");
                            float[] floats = (float[]) a_pcp_gds3_sfc_acc.read().copyTo1DJavaArray();
                            data = floats[center];
                            Variable weasd_p0_l1_gll0 = openNC.findVariable("WEASD_GDS3_SFC_acc" + number + "h");
                            float[] floats7 = (float[]) weasd_p0_l1_gll0.read().copyTo1DJavaArray();
                            snow = floats7[center];
                        }
                        String rain = String.format("%.2f", data);
                        rainTreeMap.put(new float[]{xlat[center], xlong[center]}, rain);
                        Variable u10 = openNC.findVariable("U_GRD_GDS3_HTGL");
                        float[] uWind = (float[]) u10.read().copyTo1DJavaArray();
                        float u = uWind[center];
                        Variable v10 = openNC.findVariable("V_GRD_GDS3_HTGL");
                        float[] vWind = (float[]) v10.read().copyTo1DJavaArray();
                        float v = vWind[center];
                        NcMethods ncMethods = new NcMethods();
                        double degrees = ncMethods.vectorToDegrees(u, v);
                        String words = ncMethods.getWords(degrees);
                        double wind_speed = ncMethods.getWind_speed(v, u);  // map.put("风向", words);
                        String num1 = String.format("%.1f", wind_speed);
                        windTreeMap.put(new float[]{xlat[center], xlong[center]}, new String[]{words, num1}); // map.put("风速", num1); //  m/s 保留一位
                        Variable tmp_gds3_htgl = openNC.findVariable("TMP_GDS3_HTGL");
                        float[] floats1 = (float[]) tmp_gds3_htgl.read().copyTo1DJavaArray();
                        float data1 = floats1[center];
                        String num2 = String.format("%.1f", data1 - 273.15f);
                        tempTreeMap.put(new float[]{xlat[center], xlong[center]}, num2); // map.put("温度", num2); // 摄氏度
                        Variable r_h_gds3_htgl1 = openNC.findVariable("R_H_GDS3_HTGL");
                        float[] floats2 = (float[]) r_h_gds3_htgl1.read().copyTo1DJavaArray();
                        int num3 = Math.round(floats2[center]);
                        rhTreeMap.put(new float[]{xlat[center], xlong[center]}, num3);
                        Variable pres_gds3_sfc = openNC.findVariable("PRES_GDS3_SFC");
                        float[] floats3 = (float[]) pres_gds3_sfc.read().copyTo1DJavaArray();
                        int num4 = Math.round((floats3[center]) / 100);//百帕
                        pTreeMap.put(new float[]{xlat[center], xlong[center]}, num4);
                        //天气现象变量
                        Variable lat_0 = openNC.findVariable("NLAT_GDS3_SFC");//降雪量（mm）
                        float[] lats = (float[]) lat_0.read().copyTo1DJavaArray();
                        Variable lon_0 = openNC.findVariable("ELON_GDS3_SFC");
                        float[] lons = (float[]) lon_0.read().copyTo1DJavaArray();
                        StringBuilder builder = new StringBuilder();
                        String snowWeather = WeatherUtil.getSnowWeather(snow);
                        String rainWeather = WeatherUtil.getRainWeather(data);
                        if (snowWeather != null & rainWeather != null) {
                            builder.append("雨加雪");
                        } else if (snowWeather != null & rainWeather==null ) {
                            builder.append(snowWeather);
                        } else if (rainWeather != null & snowWeather==null ) {
                            builder.append(rainWeather);
                        } else {
                            Variable r_h_gds3_htgl = openNC.findVariable("R_H_GDS3_HTGL");//相对湿度 %
                            float[] floats = (float[]) r_h_gds3_htgl.read().copyTo1DJavaArray();
                            float rh = floats[center];
                            if (rh >= 90) {
                                //能见度（m） 先判断降雪降雨，如果没有就读相对湿度，如果相对湿度大于90%，
                                // 然后读能见度，如果能见度在10km以上不是雾，10km以下有分级。如果相对湿度小于90%，就读云量，根据云量判断晴天多云和阴天。
                                Variable vis_p0_l1_gll0 = openNC.findVariable("VIS_GDS3_SFC"); //m
                                float[] floats9 = (float[]) vis_p0_l1_gll0.read().copyTo1DJavaArray();
                                float visibility = floats9[center];
                                if (visibility < 10000) {
                                    String fogWeather = WeatherUtil.getFogWeather(visibility);
                                    builder.append(fogWeather);
                                }else {
                                    builder.append("晴");
                                }
                            } else {
                                //TCDC_P0_L10_GLL0 总云量 %
                                Variable tcdc_p0_l10_gll0 = openNC.findVariable("T_CDC_GDS3_EATM");
                                float[] floats10 = (float[]) tcdc_p0_l10_gll0.read().copyTo1DJavaArray();
                                float cloud = floats10[center];
                                String cloudWeather = WeatherUtil.getCloudWeather(cloud);
                                builder.append(cloudWeather);
                            }
                        }
                        weatherTreeMap.put(new float[]{xlat[center], xlong[center]}, builder.toString());
                    }
                    System.out.println("预报文件" + s + "解析完毕");
                    //数据排序，
                    map.put("forcastTime", str);
                    map.put("rain", rainTreeMap.values());
                    map.put("wind", windTreeMap.values());
                    map.put("temp", tempTreeMap.values());
                    map.put("rh", rhTreeMap.values());
                    map.put("p", pTreeMap.values());
                    map.put("weather", weatherTreeMap.values());
                    list.add(map); //全局变量+单个文件6要素map
                    System.out.println("预报文件" + s + "数据装入list" + "大小" + list.size());
                } catch (Exception e) {
                    log.error("解析程序异常"+e.getMessage());
                }
            }
            log.info("关闭操作nc文件的流");
            openNC.close();
            return list;
        }
    }
    /**
     * @return * @return: null
     * @Author: L
     * @Params * @param null:
     **/
    public void setAreaData() throws IOException, InterruptedException {
        File file = new File("E:\\ftp\\prodect\\element.txt"); //E:\ftp\prodect\element.txt D:/wanyun/element.txt
        ArrayList<LinkedMap<String, Object>> areaData = getAreaData();
        if (areaData==null){
            System.out.println("本地服务器没有数据");
        }else {
            Gson gson = new Gson();
            System.out.println("数据解析完毕开始写入");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(gson.toJson(areaData));
            bufferedWriter.close();
            System.out.println("数据已写入");
        }
    }



}
