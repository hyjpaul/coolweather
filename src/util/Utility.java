package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import db.CoolWeatherDB;
import model.City;
import model.County;
import model.Province;

/** 
 * @ClassName: Utility 
 * @Description: 用于解析和处理服务器数据的工具类(数据："代号|城市，代号|城市")
 * @author: Administrator
 * @date: 2016年1月7日 下午8:38:23  
 */
public class Utility {
	/** 
	 * @Title: handleProvincesResponse 
	 * @Description: 解析和处理服务器返回的省级数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 * @return: boolean
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response){
		//先用TextUtils判断response是否为空
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出的数据存储到数据库Province表
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}

		}
		return false;
	}

	/** 
	 * @Title: handleCitiesResponse 
	 * @Description: 解析和处理服务器返回的县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 * @return: boolean
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response,int provinceId){
		//先用TextUtils判断response是否为空
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//将解析出的数据存储到数据库City表
					coolWeatherDB.saveCity(city);
				}
				return true;
			}

		}
		return false;
	}
	/** 
	 * @Title: handleCountiesResponse 
	 * @Description: 解析和处理服务器返回的县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId
	 * @return
	 * @return: boolean
	 */
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response,int cityId){
		//先用TextUtils判断response是否为空
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//将解析出的数据存储到数据库County表
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}

		}
		return false;
	}
	
	public static void handleWeatherResponse(Context context, String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void saveWeatherInfo(Context context,String cityName,String  weatherCode,String temp1,String temp2, String weatherDesp,String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name",cityName );
		editor.putString("weather_code",weatherCode );
		editor.putString("temp1",temp1 );
		editor.putString("temp2",temp2 );
		editor.putString("weather_desp",weatherDesp );
		editor.putString("publish_time",publishTime );
		editor.putString("current_date",sdf.format(new Date()));
		editor.commit();
	}
}