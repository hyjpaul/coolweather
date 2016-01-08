package util;

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
}
