package com.mvw.medicalvisualteaching.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.SPUtil;

/**
 * 启动页
 * Created by a on 2016/11/18.
 */
public class SplashActivity extends BaseActivity{

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        handler.sendEmptyMessageDelayed(1000,2000);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(SPUtil.getInstance(context).getBoolean(Constant.GUIDE_FLAG,true)){
                startActivity(new Intent(context,GuideActivity.class));
            }else {
                if(MyApplication.getUser() != null){
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }
            }
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler= null;
    }
}
