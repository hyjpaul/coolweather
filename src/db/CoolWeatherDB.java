package db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.City;
import model.County;
import model.Province;

/**
 * @ClassName: CoolWeatherDB
 * @Description: TODO
 * @author: Administrator
 * @date: 2016年1月7日 下午7:10:23
 */
public class CoolWeatherDB {
	/**
	 * @fieldName: DB_NAME
	 * @fieldType: String
	 * @Description: 数据库名
	 */
	public static final String DB_NAME = "cool_weather";
	/**
	 * @fieldName: VERSION
	 * @fieldType: int
	 * @Description: 数据库版本
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/** 
	 * @Title:CoolWeatherDB
	 * @Description:将构造方法私有化
	 * @param context 
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/** 
	 * @Title: getInstance 
	 * @Description: 获取CoolWeatherDB的实例
	 * @param context
	 * @return
	 * @return: CoolWeatherDB
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/** 
	 * @Title: saveProvince 
	 * @Description: 将Province实例存储到数据库
	 * @param province
	 * @return: void
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name",province.getProvinceName());
			values.put("province_code",province.getProvinceCode());
			db.insert("Province", null, values);
			
		}
	}
	
	/** 
	 * @Title: loadProvince 
	 * @Description: 从数据库获取全国所有省份信息
	 * @return
	 * @return: List<Province>
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query( "Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/** 
	 * @Title: saveCity 
	 * @Description: 将City实例存储到数据库
	 * @param city
	 * @return: void
	 */
	public void saveCity(City city){
		ContentValues values = new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
	}
	
	/** 
	 * @Title: loadCities 
	 * @Description: 从数据库读取某省下所有城市信息
	 * @param provinceId
	 * @return
	 * @return: List<City>
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
			
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/** 
	 * @Title: saveCounty 
	 * @Description: 将County实例存储到数据库
	 * @param county
	 * @return: void
	 */
	public void saveCounty(County county){
		ContentValues values = new ContentValues();
		values.put("county_name", county.getCountyName());
		values.put("county_code", county.getCountyCode());
		values.put("city_id", county.getCityId());
		db.insert("County", null, values);
	}
	
	/** 
	 * @Title: loadCouties 
	 * @Description: 从数据库读取某城市下所有的县信息
	 * @param cityId
	 * @return
	 * @return: List<County>
	 */
	public List<County> loadCouties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query( "County", null, "cityId = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
}
