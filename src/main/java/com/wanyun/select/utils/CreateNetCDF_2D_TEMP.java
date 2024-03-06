package com.wanyun.select.utils;

import com.alibaba.fastjson.JSONObject;

import com.wanyun.select.entity.View;

import org.springframework.http.converter.json.GsonBuilderUtils;
import ucar.ma2.ArrayFloat;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CreateNetCDF_2D_TEMP {

	public final static float MISSING_VALUE = -9.96921E36f;
	public View view(String path , String variable) throws Exception {
		String variable1 = VariableList.getVariable(variable);
		HashMap<String, Object> map = new HashMap<>();
		//last 'aa' is 'AM' or 'PM'. 'HH' is 24 hour system. if you change it to 'hh', it means 12 hour system
		NetcdfFile ncfile = null;
		JSONObject object=null;
		//自定义等间距网格数
		int xGridsNum = 250;//648
		int yGridsNum = 200;//499
		ArrayFloat.D2 tempData;
		View view = new View();
		try {
			ncfile = NetcdfFile.open(path);
			// Get the latitude and longitude Variables.
			Variable latVarO = ncfile.findVariable("NLAT_GDS3_SFC");
			Variable lonVarO = ncfile.findVariable("ELON_GDS3_SFC");
			// Get the temperature Variables.
			Variable tmpVariable = ncfile.findVariable(variable1);

			ArrayFloat.D2 latArray;
			ArrayFloat.D2 lonArray;
			ArrayFloat.D2 tmpArray;
			latArray = (ArrayFloat.D2) latVarO.read();
			lonArray = (ArrayFloat.D2) lonVarO.read();
			tmpArray = (ArrayFloat.D2) tmpVariable.read();

			//Calculate latitude and longitude range
			float minLat = 99999f;
			float maxLat = 0f;
			float minLon = 99999f;
			float maxLon = 0f;
			int latGridsNumO = latVarO.getDimension(0).getLength();
			int lonGridsNumO = latVarO.getDimension(1).getLength();
			//使用原始NC数据中网格数
			float minTmp = 99999f, maxTmp = 0f;
			for(int j=0;j<lonGridsNumO;j++) {
				for(int i=0; i<latGridsNumO; i++) {
					float fLat = latArray.get(i,j);
					float fLon = lonArray.get(i,j);
					float fTemp = tmpArray.get(i,j);
					//latData.set(i,j, f);
					//lonData.set(i,j, f);
					if(maxLat<fLat) maxLat = fLat;
					if(minLat>fLat) minLat = fLat;
					if(maxLon<fLon) maxLon = fLon;
					if(minLon>fLon) minLon = fLon;
					if(maxTmp<fTemp) maxTmp = fTemp;
					if(minTmp>fTemp) minTmp = fTemp;
				}
			}

			//wrfprs_d01.2022030515.f07.nc yyyy-MM-dd-HH mm"
			//path.substring(path.indexOf("d01")+3,path.lastIndexOf("f")); //预报时间
			//map.put("refTime",)
			String substring = path.substring(path.indexOf("d01") + 4, path.lastIndexOf("f")-1);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH", Locale.CHINA);
			LocalDateTime ldt = LocalDateTime.parse(substring, dateTimeFormatter);
			String substring1 = path.substring(path.lastIndexOf("f") + 1, path.lastIndexOf("f") + 3);
			int number = Integer.parseInt(substring1);  //预报时效
			LocalDateTime plus = ldt.plus(number+8, ChronoUnit.HOURS);
			String format = plus.format(dateTimeFormatter);
			format = getFrom(format, "yyyyMMddHH", "yyyy-MM-dd-HH");

			//定义新NC文件中经度、纬度、温度变量数组
			 tempData = new ArrayFloat.D2(xGridsNum, yGridsNum);

			//纬度网格间距
			float latLen = Math.abs(maxLat-minLat)/yGridsNum;
			String latLen1 = String.format("%.8f", latLen);
			//经度网格间距
			float lonLen = Math.abs(maxLon-minLon)/xGridsNum;
			String lonLen1 = String.format("%.8f", lonLen);
			//变量自带信息
			String parameterUnit = tmpVariable.findAttribute("units").getValues().toString();//参数单位
			String  parameterNumberName = tmpVariable.findAttribute("long_name").getValues().toString();//参数编号名称
			//String  v_parameterNumberName = tmpVariable.findAttribute("long_name").getValues().toString();

			//创建一个线程池
			ExecutorService pool = Executors.newCachedThreadPool();
			for(int j=0; j<yGridsNum; j++) {
				for(int i=0; i<xGridsNum; i++) {
					float tempLat = minLat + j*latLen;
					float tempLon = minLon + i*lonLen;
					//计算等经纬网格中J，I网格的值
					//参数 数据行数， 数据列数， 维度数组， 经度数组 ，温度二维数组，温度行数，温度列数， 维度网格间距， 精度度网格间距
					Callable c = new MyCallable(latGridsNumO,lonGridsNumO,latArray,lonArray,
							tmpArray,tempLat,tempLon,latLen,lonLen);
					//执行任务并获取 Future 对象
					Future future = pool.submit(c);
					float f = Float.parseFloat(future.get().toString());
					tempData.set(i,j, f);
				}
			}
			//将数组中的数据组装成字符串。数据之间','间隔
			String TEMPData = "";
			ArrayList<Float> arrayList = new ArrayList<>();
			for(int j=yGridsNum-1;j>-1;j--){
				for(int i=0;i<xGridsNum;i++) {
					if(j==0 && i==xGridsNum-1){
						TEMPData += tempData.get(i,j) + "";
						arrayList.add(tempData.get(i,j));
					}
					else{
						TEMPData += tempData.get(i,j) + ",";
						arrayList.add(tempData.get(i,j));
					}
				}
			}
            view.setVariable(variable1);
			view.setRefTime(format);
			view.setLo2(String.format("%.8f", maxLon));
			view.setLo1(String.format("%.8f", minLon));
			view.setDx(lonLen1 );
			view.setDy(latLen1);
			view.setNx(xGridsNum+"");
			view.setNy(yGridsNum+"");
			view.setParameterNumberName(parameterNumberName);
			view.setParameterCategory(1+"");
			view.setParameterCategoryName(variable);
			view.setParameterNumber(2+"");
			view.setLa1(String.format("%.8f", maxLat));
			view.setLa2(String.format("%.8f", minLat));
			view.setParameterUnit(parameterUnit);
			view.setData(arrayList);
			view.setFileName(path.substring(path.indexOf("wrfprs")));
		}
		finally {

			ncfile.close();
		}

		return view;
	}

	public static String getFrom(String time, String formt, String formt1) {
		try {
			Date  d1 = new SimpleDateFormat(formt).parse(time);
			SimpleDateFormat sdf0 = new SimpleDateFormat(formt1);
			return sdf0.format(d1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}

class MyCallable implements Callable<Object> {
	//经度纬度维度定义
	int latGridsNumO;
	int lonGridsNumO;
	ArrayFloat.D2 latArray;
	ArrayFloat.D2 lonArray;
	ArrayFloat.D2 tmpArray;
	float tempLat;
	float tempLon;
	float latLen;
	float lonLen;

	MyCallable(int latGridsNumO,int lonGridsNumO,ArrayFloat.D2 latArray,ArrayFloat.D2 lonArray,
		ArrayFloat.D2 tmpArray,float tempLat,float tempLon,float latLen,float lonLen) {
		this.latGridsNumO = latGridsNumO;
		this.lonGridsNumO = lonGridsNumO;
		this.latArray = latArray;
		this.lonArray = lonArray;
		this.tmpArray = tmpArray;
		this.tempLat = tempLat;
		this.tempLon = tempLon;
		this.latLen = latLen;
		this.lonLen = lonLen;
	}

	public Object call() throws Exception {
		float tempTmps = 0f;
		int tempNum = 0;
		for(int jLat=0; jLat<this.lonGridsNumO; jLat++) {
			for (int iLon = 0; iLon < this.latGridsNumO; iLon++) {
				if(this.latArray.get(iLon,jLat)>=(tempLat-latLen) &&
						this.latArray.get(iLon,jLat)<=(tempLat+latLen) &&
						this.lonArray.get(iLon,jLat)>=(tempLon-lonLen) &&
						this.lonArray.get(iLon,jLat)<=(tempLon+lonLen)
				){
					tempNum++;
					tempTmps += this.tmpArray.get(iLon,jLat);
				}
			}
		}
		float f = 0f;
		if(tempNum>0)
		{
			f = tempTmps/tempNum;
		}

		return f;
	}

	public static void main(String[] args) {
		ArrayList<Object> arrayList = new ArrayList<>();
		for (int i = 0; i < 8 ; i++) {
			arrayList.add(1);
		}
		System.out.println(arrayList);
	}

}
