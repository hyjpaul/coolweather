package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import service.AutoUpdateService;

/** 
 * @ClassName: AutoUpdateReceiver 
 * @Description: 广播接收器收到PendingIntent广播跳转到AutoUpdateService后台服务
 * @author: Administrator
 * @date: 2016年1月9日 下午9:14:38  
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}
}
