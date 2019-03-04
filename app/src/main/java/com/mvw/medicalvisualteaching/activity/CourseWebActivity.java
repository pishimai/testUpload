package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.RelativeLayout;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.callback.BaseJavaScriptInterface;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

//import org.xwalk.core.XWalkSettings;


/**
 * 教程显示界面
 * Created by a on 2016/11/16.
 */
public class CourseWebActivity extends BaseActivity{

    private CourseWebActivity activity;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_web);
        activity = this;
        initView();
    }


    private void initView(){
//        Glide.with(activity).load(R.mipmap.loading).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivProgress);
        RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        mXwalkView = new XWalkView(activity);
        rlLayout.addView(mXwalkView);
        XWalkSettings settings = mXwalkView.getSettings();
       // settings.setCacheMode(XWalkSettings.LOAD_NO_CACHE);//不加载缓存
        mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
        //mXwalkView.clearCache(true);
        mXwalkView.setUIClient(new MyUIClient(mXwalkView));
        mXwalkView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        mXwalkView.addJavascriptInterface(new BaseJavaScriptInterface(activity),"Elf");

        url = getIntent().getStringExtra("url");
        mXwalkView.loadUrl(url);
        activity.showWaitDialog();
        findViewById(R.id.button_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        @Override
        public void onReceivedLoadError(XWalkView view, int errorCode, String description,
            String failingUrl) {
//            super.onReceivedLoadError(view, errorCode, description, failingUrl);
            initNoNetworkLayout(url);
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
        FileUtils.saveLog(activity,"",Constant.ACTION_TYPE_END,url);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mXwalkView != null) {
            mXwalkView.resumeTimers();
            mXwalkView.onShow();
        }
        FileUtils.saveLog(activity,"",Constant.ACTION_TYPE_START,url);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//           appCallWeb("", Constant.MSG_GO_BACK,"");
//            return false;
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}
