package com.mvw.medicalvisualteaching.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.orhanobut.logger.Logger;

/**
 * 网络变化广播接收
 */
public class MyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.i("onReceive = 网络发生了变化" + intent.getAction());
//		Intent netIntent = new Intent();
//		netIntent.setAction("receive_net_state");
//		context.sendBroadcast(netIntent);
		if(Utils.isNetworkAvailable(context)){//有网络的情况下
			if(Utils.isWIFI(context)){
				//WiFi状态下
				FileUtils.commitLog(context);
			}else{
				//有网而且在4g情况下  暂停下载  
//				Intent startIntent = new Intent();
//				startIntent.setAction(BookcaseFragment.STOP_DOWNLOAD_ACTION);
//				startIntent.putExtra("from","net_stop");
//				context.sendBroadcast(startIntent);
			}
			Intent startIntent = new Intent();
			startIntent.setAction("com.mvw.nationalmedicalPhone.networkstate");
			context.sendBroadcast(startIntent);
		}
	}
}
