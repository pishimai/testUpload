package com.mvw.medicalvisualteaching.fragment;

import static com.mvw.medicalvisualteaching.config.AppConfig.BOOK_CITY_URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.StringUtils;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkView;

/**
 * 书城
 */
@SuppressLint("ValidFragment")
public class BookCityFragment extends BaseFragment{


	public BookCityFragment(XWalkView xWalkView) {
		super(xWalkView);
	}
	public BookCityFragment(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater,container,savedInstanceState);
	}
	@Override
	protected void initView(View view){
		super.initView(view);
	}

	@Override
	public void resume() {
		super.resume();
		Logger.i("BookCityFragment on Resume ");
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
	protected void loadUrl(){
		super.loadUrl();
		url = BOOK_CITY_URL+"&token="+MyApplication.getUser().getToken();
		url = StringUtils.createUrl(url,null,activity);
		Logger.i("BOOK_CITY_URL----"+url);
		mXwalkView.loadUrl(url);
		activity.showWaitDialog();
		Logger.i("BookCityFragment -webview------"+mXwalkView.toString());
	}

	@Override
	public void appCallWeb(String sn,String content) {
		super.appCallWeb(sn,content);
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

}
