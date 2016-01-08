package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/** 
 * @ClassName: HttpUtil 
 * @Description: 与服务器交互的工具类
 * @author: Administrator
 * @date: 2016年1月7日 下午8:25:59  
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection conn = null;
				try {
					URL url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					InputStream in = conn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line = null;
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					if(listener != null){
						//回调onFinish()方法
						listener.onFinish(response.toString());
					}	
				} catch (Exception e) {
					//回调onError()方法
					listener.onError(e);
				}finally {
					if (conn != null){
						conn.disconnect();
					}
				}
			}
		}).start();
	}
}
