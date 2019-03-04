package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.RelativeLayout;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.callback.BaseJavaScriptInterface;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

//import org.xwalk.core.XWalkSettings;

/**
 * URL跳转新界面
 * (不屏蔽返回键)
 * Created by a on 2016/11/16.
 */
public class URLWebActivity extends BaseActivity{

    private URLWebActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        activity = this;
        initView();
    }

    private void initView(){
        RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        mXwalkView = new XWalkView(this);
        rlLayout.addView(mXwalkView);
        XWalkSettings settings = mXwalkView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(XWalkSettings.LOAD_NO_CACHE);//不加载缓存
        mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
        mXwalkView.setUIClient(new MyUIClient(mXwalkView));
        mXwalkView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        mXwalkView.addJavascriptInterface(new BaseJavaScriptInterface(activity),"Elf");
        activity.showWaitDialog();
        String url = getIntent().getStringExtra("url");
        mXwalkView.loadUrl(url);
    }

    class MyResourceClient extends XWalkResourceClient {
        MyResourceClient(XWalkView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(XWalkView view, String url) {
            super.onLoadStarted(view, url);
        }

        @Override
        public void onLoadFinished(XWalkView view, String url) {
            super.onLoadFinished(view, url);
            activity.hideWaitDialog();
        }
    }

    class MyUIClient extends XWalkUIClient {
        MyUIClient(XWalkView view) {
            super(view);
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        if (mXwalkView != null) {
            mXwalkView.pauseTimers();
            mXwalkView.onHide();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mXwalkView != null) {
            mXwalkView.resumeTimers();
            mXwalkView.onShow();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mXwalkView != null) {
            mXwalkView.onDestroy();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mXwalkView != null) {
            mXwalkView.onNewIntent(intent);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (mXwalkView != null) {
            mXwalkView.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void appCallWeb(String command,String sn,String content) {
        super.appCallWeb(command,sn,content);
    }
}
