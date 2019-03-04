package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.view.MyXWalkView;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

//import org.xwalk.core.XWalkSettings;

/**
 * 数据库界面加载
 */
public class DatabaseWebActivity extends BaseActivity{

    private DatabaseWebActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        activity = this;
        initView();
    }

    private void initView(){
        RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        mXwalkView = new MyXWalkView(this);
        rlLayout.addView(mXwalkView);
        XWalkSettings settings = mXwalkView.getSettings();
        settings.setCacheMode(XWalkSettings.LOAD_NO_CACHE);//不加载缓存
        mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
        mXwalkView.setUIClient(new MyUIClient(mXwalkView));
        mXwalkView.addJavascriptInterface(new MyJavaInterface(),"Elf");
        activity.showWaitDialog();
        mXwalkView.loadUrl(AppConfig.DATABASE_URL);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mXwalkView != null) {
            mXwalkView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void appCallWeb(String command,String sn,String content) {
        super.appCallWeb(command,sn,content);
        mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"','"+content+"')", null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Logger.i("onKeyDown  =) "+mXwalkView.getNavigationHistory().canGoBack());
            String command="{\"Command\":\"PageBack\",\"Args\":{}}";
            mXwalkView.evaluateJavascript("javascript:Elf.appCallWeb('" + command + "')", null);
//            if (mXwalkView.getNavigationHistory().canGoBack()) {
//                mXwalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);//返回上一页面
//            } else {
//                finish();
//            }
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    public class MyJavaInterface {

        @JavascriptInterface
        public void webCallApp(String json) {
            JSONObject jsobj;
            try {
                jsobj = new JSONObject(json);
                String command = jsobj.getString("Command");//
                String args = jsobj.getString("Args");//
                if ("quit".equals(command) || "ReturnHomePage".equals(command)) {
                    finish();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
