package activity;

import com.coolweather.app.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

/** 
 * @ClassName: WeatherActivity 
 * @Description: TODO
 * @author: Administrator
 * @date: 2016年1月9日 下午2:37:20  
 */
public class WeatherActivity extends Activity {
/**
 * @fieldName: weatherInfoLayout
 * @fieldType: LinearLayout
 * @Description: 天气信息布局
 */
private LinearLayout weatherInfoLayout;
/**
 * @fieldName: cityNameText
 * @fieldType: TextView
 * @Description: 显示城市名
 */
private TextView  cityNameText;
/**
 * @fieldName: publishText
 * @fieldType: TextView
 * @Description: 显示发布时间
 */
private TextView  publishText;
/**
 * @fieldName: weatherDespText
 * @fieldType: TextView
 * @Description: 显示天气描述信息
 */
private TextView  weatherDespText;
/**
 * @fieldName: temp1Text
 * @fieldType: TextView
 * @Description: 显示最低气温
 */
private TextView  temp1Text;
/**
 * @fieldName: temp2Text
 * @fieldType: TextView
 * @Description: 显示最高气温
 */
private TextView  temp2Text;
/**
 * @fieldName: currentDateText
 * @fieldType: TextView
 * @Description: 显示当前日期
 */
private TextView  currentDateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText  = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_dscp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号时就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else {
			//没有县级代号是就直接显示本地天气
			showWeather();
		}
	}
	/** 
	 * @Title: queryWeatherCode 
	 * @Description: 查询县级代号所对应的天气代号
	 * @param countyCode
	 * @return: void
	 */
	private void queryWeatherCode(String countyCode){
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address, "countyCode");
	}
	
	/** 
	 * @Title: queryWeatherInfo 
	 * @Description: 查询天气代号对应的天气
	 * @param weatherCode
	 * @return: void
	 */
	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address, "weatherCode");
	}
	private void queryFromServer(final String address, final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					//处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("同步失败...");
					}
				});
			}
		});
	}
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
}
