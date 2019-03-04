package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.medicalvisualteaching.bean.Update;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.dialog.UpdateDialog;
import com.mvw.medicalvisualteaching.fragment.BaseFragment;
import com.mvw.medicalvisualteaching.fragment.BookCityFragment;
import com.mvw.medicalvisualteaching.fragment.BookcaseFragment;
import com.mvw.medicalvisualteaching.fragment.DatabaseFragment;
import com.mvw.medicalvisualteaching.fragment.ExamFragment;
import com.mvw.medicalvisualteaching.fragment.OperationFragment;
import com.mvw.medicalvisualteaching.fragment.TempFragment;
import com.mvw.medicalvisualteaching.service.UpdateService;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.HttpUtils;
import com.mvw.medicalvisualteaching.utils.SPUtil;
import com.mvw.medicalvisualteaching.utils.StringUtils;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.mvw.medicalvisualteaching.view.HomeViewPager;
import com.mvw.medicalvisualteaching.view.MyXWalkView;
import com.mvw.netlibrary.OkHttpUtils;
import com.mvw.netlibrary.callback.StringCallback;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.MediaType;
import org.xwalk.core.XWalkView;

/**
 * 主界面
 * Created by a on 2016/11/17.
 */
public class MainActivity  extends BaseActivity {

    private MainOnCheckedChangeListener onCheckedChangeListener = new MainOnCheckedChangeListener();
    private SimpleTarget simpleTarget;
    private List<BaseFragment> fragments = new ArrayList<>();
    private BaseActivity activity;
    private HomeViewPager vp_home;
    private int currentFragment = 0;//默认显示第几个fragment

    private XWalkView bookCityView ;
    private XWalkView bookcaseView ;
    private XWalkView examView ;
    private XWalkView findView ;
    private XWalkView operationView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        //crosswalk调试
//        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        initView();
        checkUpdate();//APP更新
        initStoragePosition();
        initBindActivity();
        setJPushTag();
        rechargePoint();//给2.0升3.0用户充值阅读
        deleteBookFromTwo();//删除2.0版本的数据文件
    }

    private void initStoragePosition(){
        int storagePosition = SPUtil.getInstance(activity).getInt(Constant.BOOK_STORAGE_POSITION,-1);
        if(storagePosition == -1){
            //先判断用户是否已经选择了下载存储位置,默认是手机内存
            long phoneSize = Utils.getSDCardAvailableSize(activity,1);
            if(phoneSize <= 0){
                //手机存储不可用
                SPUtil.getInstance(activity).save(Constant.BOOK_STORAGE_POSITION,2);
            }else {
                SPUtil.getInstance(activity).save(Constant.BOOK_STORAGE_POSITION,1);
            }
        }
    }

    private void initView(){
        final RadioGroup rgFoot = (RadioGroup) findViewById(R.id.rg_main_foot);
        rgFoot.setOnCheckedChangeListener(onCheckedChangeListener);
        vp_home = (HomeViewPager) findViewById(R.id.vp_home);
        vp_home.setEnabled(false);
        bookCityView = new MyXWalkView(activity);
        BookCityFragment bookCityFragment = new BookCityFragment(bookCityView);
        bookcaseView = new MyXWalkView(activity);
        BookcaseFragment bookcaseFragment = new BookcaseFragment(bookcaseView);
        examView = new MyXWalkView(activity);
        ExamFragment examFragment = new ExamFragment(examView);
        findView = new MyXWalkView(activity);
        DatabaseFragment findFragment = new DatabaseFragment(findView);
        operationView = new MyXWalkView(activity);
        OperationFragment operationFragment = new OperationFragment(operationView);
        fragments.add(bookcaseFragment);
        fragments.add(bookCityFragment);
        fragments.add(findFragment);
        fragments.add(examFragment);
        fragments.add(operationFragment);
        if (VERSION.SDK_INT < 26){
            TempFragment temp1 = new TempFragment(null);
            TempFragment temp2 = new TempFragment(null);
            fragments.add(temp1);
            fragments.add(temp2);
        }

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        vp_home.setOffscreenPageLimit(6);
        vp_home.setAdapter(pagerAdapter);
        if (VERSION.SDK_INT<26){
            vp_home.setCurrentItem(6,false);
            handler.sendEmptyMessage(10000);
        }
    }
    /**
     * 菜单切换监听
     */
    private class MainOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_main_foot_bookcase:
                    currentFragment = 0;
                    vp_home.setCurrentItem(currentFragment,false);
                    SPUtil.getInstance(activity).save("currentFragment",currentFragment);
                    break;
                case R.id.rb_main_foot_bookcity:
                    currentFragment = 1;
                    vp_home.setCurrentItem(currentFragment,false);
                    SPUtil.getInstance(activity).save("currentFragment",currentFragment);
                    break;
                case R.id.rb_main_foot_database:
                    currentFragment = 2;
                    vp_home.setCurrentItem(currentFragment,false);
                    SPUtil.getInstance(activity).save("currentFragment",currentFragment);
                    break;
                case R.id.rb_main_foot_exam:
                    currentFragment = 3;
                    vp_home.setCurrentItem(currentFragment,false);
                    SPUtil.getInstance(activity).save("currentFragment",currentFragment);
                    break;
                case R.id.rb_main_foot_operation:
                    currentFragment = 4;
                    vp_home.setCurrentItem(currentFragment,false);
                    SPUtil.getInstance(activity).save("currentFragment",currentFragment);
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("  MainActivity  onResume");
        getCurrentFragment().resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        getCurrentFragment().pause();
        Logger.e("  MainActivity  onPause");
    }
    @Override
    public void appCallWeb(String command, String sn,String content) {
        switch (command){
            case Constant.GET_BOOK_SHELF_LIST:
            case Constant.GET_PURCHASE_BOOK_LIST:
                if(bookcaseView != null){
                    bookcaseView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"','"+content+"')", null);
                }
                break;
            default:
                fragments.get(currentFragment).appCallWeb(sn,content);
                break;
        }

    }

    public BaseFragment getCurrentFragment() {
        return fragments.get(currentFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ((BaseFragment)fragments.get(currentFragment)).onKey(keyCode,event);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     *APP更新提示
     */
    public static void initUpdateDialog(final Update update,final BaseActivity activity) {
        if(update == null || update.getPath() == null){
            return;
        }
        Logger.i("action = "+update.getAction()+" path = "+update.getPath());
        final UpdateDialog dialog = new UpdateDialog(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View updateLayout = layoutInflater.inflate(R.layout.dialog_update, null);
        TextView tvHint = (TextView) updateLayout.findViewById(R.id.tvHint);
        final TextView tvInstall = (TextView) updateLayout.findViewById(R.id.tvInstall);
        TextView tvCancel = (TextView) updateLayout.findViewById(R.id.tvCancel);
        if(update.getIntroduce() != null){
            //设置更新内容
            tvHint.setText(update.getIntroduce());
        }
        if("force".equals(update.getAction())){
            //强制更新
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            tvCancel.setVisibility(View.GONE);
        }
        tvInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //埋点立即更新
                FileUtils.saveLog(activity, Constant.ACTION_UPDATE_APP,"","");
                if ("force".equals(update.getAction())) {
                    tvInstall.setText("正在更新 ");
                    tvInstall.setClickable(false);
                } else {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
                // 下载安装包
                String downloadDir = Utils.getSDCardPath(activity,1) + "/apk";
                File downloadDirFile = new File(downloadDir);
                if (!downloadDirFile.exists()) {
                    downloadDirFile.mkdirs();
                }else{
                    File[] files = downloadDirFile.listFiles();
                    for(int i=0; i<files.length; i++){
                        if(files[i].getName().contains(".apk")){
                            files[i].delete();
                        }
                    }
                }
                Intent service = new Intent(activity, UpdateService.class);
                String url = update.getPath();
                service.putExtra("apkURL",url);
                service.putExtra("apkPath",downloadDir);
                service.putExtra("version",System.currentTimeMillis()+"");
                activity.startService(service);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                    // 点击了升级取消 记录log
                    FileUtils.saveLog(activity, Constant.ACTION_CANCEL_UPDATE_APP,"","");
                }
            }
        });
        dialog.setContentView(updateLayout);
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000){
            Logger.i(" 从pdf结束返回。。。");
            FileUtils.saveLog(activity,Constant.CMD_OPEN_PDF,Constant.ACTION_TYPE_END,"");
        }
        if (VERSION.SDK_INT<26){
            vp_home.setCurrentItem(6,false);
            handler.sendEmptyMessage(10000);
        }

    }

    public void setFragmentPosition(int index){
        vp_home.setCurrentItem(6,false);
    }

    /**
     * 手机号绑定
     */
    private void initBindActivity(){
        User user = MyApplication.getUser();
        if(!TextUtils.equals("true",user.getGuest())&&TextUtils.isEmpty(user.getCellphone())){
            //不是游客且没有手机号，需要强制绑定手机
            startActivity(new Intent(activity, BindingPhoneActivity.class));
        }
    }
    /**
     * APP版本更新
     */
    private void checkUpdate(){
        User user = MyApplication.getUser();
        HttpUtils.checkVersion(user,Utils.getAppVersionCode(activity)+"",handler,"");
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    String url = (String) msg.obj;
                    String fileName = StringUtils.getUrlFileName(url);
                    FileUtils.savePicture(activity,fileName,url);
                    break;
                case 10000:
                    vp_home.setCurrentItem(5,false);
                    handler.sendEmptyMessage(20000);
                    break;
                case 20000:
                    vp_home.setCurrentItem(currentFragment,false);
                    break;
                default:
                    Result result = (Result) msg.obj;
                    String command = result.getCommand();
                    if(result.isSuccess()){
                        switch (command){
                            case Constant.CMD_CHECK_UPDATE:
                                Update update = (Update) result.getData();
                                if(update != null){
                                    if(TextUtils.equals(update.getAction(),"update")){
                                        //有更新数据
                                        MainActivity.initUpdateDialog(update,activity);
                                    }else if(TextUtils.equals(update.getAction(),"force")){
                                        //有更新且强制更新
                                        MainActivity.initUpdateDialog(update,activity);
                                    }else if(TextUtils.equals(update.getAction(),"false")){
                                        //最新版本没有更新数据
//                                Toast.makeText(activity,activity.getString(R.string.update_check),Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(activity,activity.getString(R.string.update_check_failed),Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
    };

    public Handler getHandler(){
        return handler ;
    }

    private void setJPushTag(){
        User user = MyApplication.getUser();
        if(user != null){
            //极光推送别名设置 Alias
            JPushInterface.setAlias(activity, user.getId(), null);
        }
    }

    private void deleteBookFromTwo(){
        new Thread(){
            @Override
            public void run() {
                String phonePath = Utils.getSDCardPath(activity,1)+File.separator+"book";//手机存储路径
                String sdCardPath = Utils.getSDCardPath(activity,2)+File.separator+"book";//SD卡存储路径
                Logger.i("phonePath = "+phonePath);
                Logger.i("sdCardPath = "+sdCardPath);
                File phoneFile = new File(phonePath);
                File sdCardFile = new File(sdCardPath);
                if(phoneFile.exists()){
                    //删除
                    FileUtils.deleteFile(phoneFile);
                }
                if(sdCardFile.exists()){
                    //删除
                    FileUtils.deleteFile(sdCardFile);
                }
            }
        }.start();

    }

    /**
     * 给2.0升级3.0的用户充值阅读
     * 判断条件是该用户有没有book路径
     */
    private void rechargePoint(){
        String phonePath = Utils.getSDCardPath(activity,1)+File.separator+"book";//手机存储路径
        String sdCardPath = Utils.getSDCardPath(activity,2)+File.separator+"book";//SD卡存储路径
        File phoneFile = new File(phonePath);
        File sdCardFile = new File(sdCardPath);
        if(phoneFile.exists() || sdCardFile.exists()){
            //存在进行充值阅点
            User user = MyApplication.getUser();
            Map<String,Object> serviceMap = new ArrayMap<>();
            Map<String,String> argsMap = new ArrayMap<>();
            serviceMap.put("serviceNumber","0302000");
            serviceMap.put("serviceModule","BS-Service");
            serviceMap.put("TerminalType",Constant.TerminalType);
            serviceMap.put("DeviceType", Constant.DeviceType);
            serviceMap.put("token",user.getToken());
            argsMap.put("platform", Constant.PLATFORM);
            argsMap.put("token",user.getToken());
            serviceMap.put("args",argsMap);
            Gson gson = new Gson();
            String result = gson.toJson(serviceMap);
            Logger.i(result);
            OkHttpUtils.postString()
                .url(AppConfig.HOST)
                .content(result)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            String res = URLDecoder.decode(response,"utf-8");
                            Logger.i(res);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
    }
}
