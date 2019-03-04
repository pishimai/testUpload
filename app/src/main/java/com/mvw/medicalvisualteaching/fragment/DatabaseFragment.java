package com.mvw.medicalvisualteaching.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkView;

/**
 * 数据库
 */
@SuppressLint("ValidFragment")
public class DatabaseFragment extends BaseFragment{


	public DatabaseFragment(XWalkView xWalkView) {
		super(xWalkView);
	}
	public DatabaseFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater,container,savedInstanceState);
	}
	@Override
	public void resume(){
		Logger.i("databaseFragment on Resume ");
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
	protected void initView(View view) {
		super.initView(view);
	}

	@Override
	protected void loadUrl(){
		super.loadUrl();
		url = AppConfig.DATABASE_URL;
		mXwalkView.loadUrl(url);
		activity.showWaitDialog();
	}

	@Override
	public void appCallWeb(String sn,String content) {
		super.appCallWeb(sn,content);
	}
	@Override
	public void onKey(int keyCode, KeyEvent event) {
		super.onKey(keyCode, event);
		Logger.i("  onKey ===");
		mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+ Constant.MSG_GO_BACK+"')", null);
	}
}
