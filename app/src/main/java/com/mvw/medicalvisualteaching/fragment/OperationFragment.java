package com.mvw.medicalvisualteaching.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.StringUtils;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkView;

/**
 * 教程
 */
@SuppressLint("ValidFragment")
public class OperationFragment extends BaseFragment {

  public OperationFragment(XWalkView xWalkView) {
    super(xWalkView);
  }
  public OperationFragment(){

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }
  @Override
  protected void initView(View view) {
    super.initView(view);
  }
  @Override
  protected void loadUrl() {
    super.loadUrl();
    url = AppConfig.COURSE_URL + "?token=" + MyApplication.getUser().getToken();
    url = StringUtils.createUrl(url, null, activity);
    Logger.i("operationFragment",url);
    mXwalkView.loadUrl(url);
    activity.showWaitDialog();
  }
  @Override
  public void resume() {
    Logger.i("opeartionFragment on Resume ");
    super.resume();
    if (activity != null) {
      FileUtils.saveLog(activity, "", Constant.ACTION_TYPE_START, url);
    }
  }
  @Override
  public void pause() {
    Logger.i(" on Pause ");
    super.pause();
    if (activity != null) {
      FileUtils.saveLog(activity, "", Constant.ACTION_TYPE_END, url);
    }
  }

  @Override
  public void appCallWeb(String sn, String content) {
    super.appCallWeb(sn, content);
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mXwalkView != null) {
      mXwalkView.onDestroy();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (mXwalkView != null) {
      mXwalkView.onActivityResult(requestCode, resultCode, intent);
    }
  }

  @Override
  public void onKey(int keyCode, KeyEvent event) {
    super.onKey(keyCode, event);
    Logger.i("  onKey ===");
    mXwalkView
        .evaluateJavascript("javascript:Elf.AppCallWeb('" + Constant.MSG_GO_BACK + "')", null);
  }
}
