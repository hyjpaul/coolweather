package util;

/** 
 * @ClassName: HttpCallbackListener 
 * @Description: 用于回调的接口
 * @author: Administrator
 * @date: 2016年1月7日 下午8:25:09  
 */
public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
