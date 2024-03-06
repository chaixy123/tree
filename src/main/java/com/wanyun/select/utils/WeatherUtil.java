package com.wanyun.select.utils;

public class WeatherUtil {
    public static String getWeatherDesc(String key){
        String weatherName = "";
        if(key != null && !key.isEmpty()) {
            int code = (int) Float.parseFloat(key);
            switch (code) {
                case 0:
                    weatherName = "晴";
                    break;
                case 1:
                    weatherName = "多云";
                    break;
                case 2:
                    weatherName = "阴";
                    break;
                case 3:
                    weatherName = "阵雨";
                    break;
                case 4:
                    weatherName = "雷阵雨";
                    break;
                case 5:
                    weatherName = "雷阵雨并伴有冰雹";
                    break;
                case 6:
                    weatherName = "雨夹雪";
                    break;
                case 7:
                    weatherName = "小雨";
                    break;
                case 8:
                    weatherName = "中雨";
                    break;
                case 9:
                    weatherName = "大雨";
                    break;
                case 10:
                    weatherName = "暴雨";
                    break;
                case 11:
                    weatherName = "大暴雨";
                    break;
                case 12:
                    weatherName = "特大暴雨";
                    break;
                case 13:
                    weatherName = "阵雪";
                    break;
                case 14:
                    weatherName = "小雪";
                    break;
                case 15:
                    weatherName = "中雪";
                    break;
                case 16:
                    weatherName = "大雪";
                    break;
                case 17:
                    weatherName = "暴雪";
                    break;
                case 18:
                    weatherName = "雾";
                    break;
                case 19:
                    weatherName = "冻雨";
                    break;
                case 20:
                    weatherName = "沙尘暴";
                    break;
                case 21:
                    weatherName = "小到中雨";
                    break;
                case 22:
                    weatherName = "中到大雨";
                    break;
                case 23:
                    weatherName = "大到暴雨";
                    break;
                case 24:
                    weatherName = "暴雨到大暴雨";
                    break;
                case 25:
                    weatherName = "大暴雨到特大暴雨";
                    break;
                case 26:
                    weatherName = "小到中雪";
                    break;
                case 27:
                    weatherName = "中到大雪";
                    break;
                case 28:
                    weatherName = "大到暴雪";
                    break;
                case 29 :
                    weatherName= "浮尘";
                    break;
                case 30 :
                    weatherName= "扬沙";
                    break;
                case 31 :
                    weatherName= "强沙尘暴";
                    break;
                case 32 :
                    weatherName= "浓雾";
                    break;
                case 49 :
                    weatherName= "强浓雾";
                    break;
                case 53 :
                    weatherName= "霾";
                    break;
                case 54 :
                    weatherName= "中度霾";
                    break;
                case 55 :
                    weatherName= "重度霾";
                    break;
                case 56 :
                    weatherName= "严重霾";
                    break;
                case 57 :
                    weatherName= "大雾";
                    break;
                case 58 :
                    weatherName= "特强浓雾";
                    break;
                default:
                    weatherName = "";
                    break;
            }
        }
        return weatherName;
    }

    /**
     * 获取雪量
     * @param value
     * @return
     */
    public static String getSnowWeather(double value){
        if (value == 0){
            return null;
        }else if(value>0 && value<2.5){
            return "小雪";
        }else if(value>=2.5 && value<5){
            return "中雪";
        }else if(value>=5&&value<10){
            return "大雪";
        }else {
            return "暴雪";
        }
    }

    /**
     *  获取降水
     * @param value
     * @return
     */
    public static String getRainWeather(double value){
        if (value <= 0.1 ){
            return null;
        } else if(value>0.1 && value<10){
            return "小雨";
        }else if(value>=10&&value<25){
            return "中雨";
        }else if(value>=25&&value<50){
            return "大雨";
        }else if(value>=50&&value<100){
            return "暴雨";
        }else if(value>=100&&value<250){
            return "大暴雨";
        }else{
            return "特大暴雨";
        }
    }

    /**
     * 根据云量获取天气现象
     * @param value  云量值
     * @param isRain 是否是雨后云量
     * @return
     */
    public static String getCloudWeather(double value,boolean isRain){
        if(value<3){
            return isRain ? "多云到晴" : "晴到多云" ;
        }else if(value>=3&&value<6){
            return "多云";
        }else if(value>=6&&value<8){
            return isRain ? "阴到多云" : "多云到阴" ;
        }else{
            return "阴";
        }
    }

    public static String getWeather(double val2,double val4){
        String str="";
        if(val2==0){
            if(val4<=3){
                str = "晴";
            }else if(val4>3 && val4<=5){
                str = "少云";
            }else if(val4>5 && val4<=10){
                str = "多云";
            }
        }else{
            if(val2==1 && val4<=3){
                str = "晴";
            }else if(val2>=2 && val2<=3 &&val4>=4 && val4<=5){
                str = "少云";
            }else if(val4>=4 && val4<=7 && val4>=6 && val4<=10){
                str = "多云";
            }else{
                str = "阴天";
            }
        }
        return str;
    }
    /**
     * @Description: 雾
     * @Author: L
     * @Date: 2022/3/7 18:13
     * @Params  * @param null:
     * @return  * @return: null
     **/
    public static String getFogWeather(float f){
// ①水平能见度距离在1—10公里之间的称为轻雾。
//②水平能见度距离低于1公里的称为雾。
//③水平能见度距离200—500米之间的称为大雾。
//④水平能见度距离50—200米之间的称为浓雾。
//⑤水平能见度不足50米的雾称为强浓雾。
        if (1000<= f & f < 10000){
            return "轻雾";
        }else if (500<= f & f < 1000){
            return "雾";
        }else if (200<=f & f<500){
            return "大雾";
        }else if (50 <= f & f<200){
            return "浓雾";
        }else if (f < 50){
            return "强浓雾";
        }else {
            return "定级异常！";
        }
    }
    /**
     * @Description:  云
     * @Author: L
     * @Date: 2022/3/7 18:28
     * @Params  * @param null:
     * @return  * @return: null
     **/
    public static String getCloudWeather(float val4){
      //  则读出总云量，0.2<总云量<0.5为少云，0.5<总云量<0.8是多云，0.8<总云量为阴天，总云量<0.2为晴天。
        String str="";
        if(val4<= 20){
            str = "晴";
        }else if(val4>20 && val4<=50){
            str = "少云";
        }else if(val4>50 && val4<=80){
            str = "多云";
        }else {
            str="阴";
        }
        return str;
    }

    public static void main(String[] args) {
        float f=0;
        String cloudWeather = getCloudWeather(f);
        System.out.println(cloudWeather);
    }
}
