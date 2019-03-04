package com.mvw.medicalvisualteaching.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkView;

/**
 * 跨书搜
 */
@SuppressLint("ValidFragment")
public class ExamFragment extends BaseFragment{

	public static String LOCK_SCREEN_ACTION = "com.mvw.nationalmedicalPhone.lockReceiver";
	private MyReceiver myReceiver;

	public ExamFragment(XWalkView xWalkView) {
		super(xWalkView);
	}
	public ExamFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater,container,savedInstanceState);
	}
	@Override
	protected void initView(View view){
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(LOCK_SCREEN_ACTION);
		activity.registerReceiver(myReceiver,intentFilter);
		super.initView(view);
	}

	@Override
	public void resume(){
		Logger.i("examFragment on Resume ");
		super.resume();
		if(activity != null) {
			FileUtils.saveLog(activity, "", Constant.ACTION_TYPE_START, url);
		}
	}
	@Override
	public void pause(){
		Logger.i(" on Pause ");
		super.pause();
		if(activity != null) {
			FileUtils.saveLog(activity, "", Constant.ACTION_TYPE_END, url);
		}
	}

	@Override
	public void onDestroy() {
		if (myReceiver!=null){
			activity.unregisterReceiver(myReceiver);
		}
		super.onDestroy();
	}

	@Override
	protected void loadUrl(){
		super.loadUrl();
		User user = MyApplication.getUser();
		url = AppConfig.EXAM_URL+"&token="+user.getToken();
	//	url = AppConfig.EXAM_URL;
		mXwalkView.loadUrl(url);
		activity.showWaitDialog();
	}

	@Override
	public void appCallWeb(String sn, String content) {
		super.appCallWeb(sn, content);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mXwalkView != null) {
			mXwalkView.onActivityResult(requestCode, resultCode, data);
		}
	}
	@Override
	public void onKey(int keyCode, KeyEvent event) {
		super.onKey(keyCode, event);
		Logger.i("  onKey ===");
		mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+ Constant.MSG_GO_BACK+"')", null);
	}
	private class  MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String flag = intent.getStringExtra("book");
			if (action.equals(LOCK_SCREEN_ACTION)){
				if (!TextUtils.isEmpty(flag)){
					if (flag.equals("start")){
						appCallWeb(Constant.MSG_APP_REACTIVATE,null);
					}else if (flag.equals("stop")){
						appCallWeb(Constant.MSG_APP_STARTING,null);
					}
				}

			}
		}
	}
}
