package com.mvw.medicalvisualteaching.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkView;

/**
 * 空白fragment，处理个别手机切换白屏的问题
 */
@SuppressLint("ValidFragment")
public class TempFragment extends BaseFragment{


	public TempFragment(XWalkView xWalkView) {
		super(xWalkView);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater,container,savedInstanceState);
	}
	@Override
	public void resume(){

	}
	@Override
	public void pause(){

	}
	public TempFragment(){

	}

	@Override
	protected void initView(View view) {
		super.initView(view);
	}

	@Override
	protected void loadUrl(){
		super.loadUrl();

	}
	@Override
	public void appCallWeb(String sn,String content) {
		super.appCallWeb(sn,content);
	}
	@Override
	public void onKey(int keyCode, KeyEvent event) {
		super.onKey(keyCode, event);
		Logger.i("  onKey ===");
//		mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+ Constant.MSG_GO_BACK+"')", null);
	}
}
