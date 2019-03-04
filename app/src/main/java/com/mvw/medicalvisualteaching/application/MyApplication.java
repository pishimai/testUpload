package com.mvw.medicalvisualteaching.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;
import com.liulishuo.filedownloader.FileDownloader;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.UserDao;
import com.mvw.medicalvisualteaching.fragment.ExamFragment;
import com.mvw.medicalvisualteaching.utils.DataUtil;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.SPUtil;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.mvw.netlibrary.OkHttpUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;
import com.umeng.socialize.PlatformConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * App
 * Created by a on 2016/11/16.
 */
public class MyApplication extends Application {
    private static User user;
    private int count;
//    public static boolean bookShelfRefresh;
//    public static boolean bookObtainedRefresh;
    @Override
    public void onCreate() {
        super.onCreate();
        Settings settings = Logger.init();
        settings.logLevel(LogLevel.NONE);//设置log输出
        initHttp();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);   		// 初始化 JPush
        FileDownloader.init(getApplicationContext());//下载初始化
        String myProcessName = Utils.getProcessName(getApplicationContext(),
            android.os.Process.myPid());
        if ("com.mvw.nationalmedicalPhone".equals(myProcessName)) {
            // 监听前后台切换状态
            GreenDaoHelper.initDatabase(getApplicationContext());//本地数据库初始化
            registerRunning();
            FileUtils.commitLog(getApplicationContext());
        }
        Logger.e("MyApplication ========== onCreate...");
    }

    private void initHttp(){

//        InetSocketAddress inetAddress = new InetSocketAddress("192.168.8.218",3128);
//        Proxy proxy = new Proxy(Type.HTTP,inetAddress);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
//                .proxy(proxy)
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                .writeTimeout(60000L,TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static User getUser(){
        if(user == null){
            UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
            user = userDao.queryBuilder().unique();
        }
        return user;
    }
    public static void setUser(User u){
        user = u;
    }

    /** 监听前后台切换 */
    @SuppressLint("NewApi")
    private void registerRunning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

                @Override
                public void onActivityStopped(Activity activity) {
                    count--;
                    if (count == 0) {
                        Logger.i(" 切换到后台。。。");
                        FileUtils.saveLog(getApplicationContext(), Constant.ACTION_APP_HANG,"","");
                        DataUtil.sendBookBroadcast(activity, ExamFragment.LOCK_SCREEN_ACTION,"stop");
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    if (count == 0) {
                        Logger.i(" 切换到前台。。。");
                        int currentFragment = SPUtil.getInstance(activity).getInt("currentFragment",0);
                        if (currentFragment == 3){ //如果是考试页面切换到前台
                            DataUtil.sendBookBroadcast(activity, ExamFragment.LOCK_SCREEN_ACTION,"start");
                        }
                        FileUtils.saveLog(getApplicationContext(), Constant.ACTION_APP_STARTUP,"","");
                    }
                    count++;
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityResumed(Activity activity) {
                }
                @Override
                public void onActivityPaused(Activity activity) {
                }
                @Override
                public void onActivityDestroyed(Activity activity) {
                }
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }
            });
        }
    }

    {
        //分享初始化
        PlatformConfig.setWeixin(Constant.AppID, Constant.WXAppSecret);
        PlatformConfig.setQQZone(Constant.QQAppID, Constant.QQAppKey);

    }
}
