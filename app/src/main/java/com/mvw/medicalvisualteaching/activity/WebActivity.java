package com.mvw.medicalvisualteaching.activity;

import static com.mvw.medicalvisualteaching.fragment.BookcaseFragment.UPDATE_BOOK_STATE_ACTION;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.alipay.sdk.app.PayTask;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.alipay.PayResult;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Payment;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.callback.BaseJavaScriptInterface;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.DataUtil;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.HttpUtils;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.mvw.medicalvisualteaching.view.MyXWalkView;
import com.mvw.medicalvisualteaching.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

//import org.xwalk.core.XWalkSettings;


/**
 * 新界面加载
 * (屏蔽返回键)
 * Created by a on 2016/11/16.
 */
public class WebActivity extends BaseActivity implements OnClickListener {

    private WebActivity activity;
    private String mCameraFilePath = null;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 10;
    private final static int REQUEST_CODE_GETIMAGE_BYSDCARD = 20;
    /** 请求裁剪 */
    private final static int CROP = 200;
    private BookStateReceiver bookStateReceiver;
    private WXPayReceiver wxPayReceiver;
    private String url;

    private LinearLayout llAudioView ;
    private TextView tvProgressTime ;
    private TextView tvDuration ;
    private SeekBar skbProgress ;
    private Button btnPlay ;
    private Button btnClose;
    private MediaPlayer player;
    private TimerTask timerTask;
    private Timer timer = new Timer();
    private Handler playerHandler;

    private IWXAPI msgApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        activity = this;
        initView();
        initPlayHandler();
    }


    private void initView(){
//        Glide.with(activity).load(R.mipmap.loading).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivProgress);
        RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        mXwalkView = new MyXWalkView(activity);
        rlLayout.addView(mXwalkView);
        XWalkSettings settings = mXwalkView.getSettings();
//        settings.setCacheMode(XWalkSettings.LOAD_NO_CACHE);//不加载缓存
        mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
//        mXwalkView.clearCache(true);


        mXwalkView.setUIClient(new MyUIClient(mXwalkView));
        mXwalkView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        mXwalkView.addJavascriptInterface(new BaseJavaScriptInterface(activity),"Elf");

        mXwalkView.setEnabled(true);
        mXwalkView.setFocusable(true);
        mXwalkView.setClickable(true);
        mXwalkView.setFocusableInTouchMode(true);

        url = getIntent().getStringExtra("url");
        activity.showWaitDialog();
        String path1 = "file:///android_asset/";
        String path2 = "file:///storage/emulated/0/Android/data/com.mvw.nationalmedicalPhone/files/";
        mXwalkView.setOriginAccessWhitelist(url,new String[]{path1,path2});
        mXwalkView.loadUrl(url);
        //用于更新书城图书详情里的书籍状态
        bookStateReceiver = new BookStateReceiver();
        IntentFilter intentFilter = new IntentFilter(UPDATE_BOOK_STATE_ACTION);
        registerReceiver(bookStateReceiver,intentFilter);
        //音频播放
        llAudioView = (LinearLayout) findViewById(R.id.ll_play_audio_view);
        tvProgressTime = (TextView)findViewById(R.id.tvProgressTime);
        tvDuration = (TextView)findViewById(R.id.tvDuration);
        skbProgress = (SeekBar)findViewById(R.id.skbProgress);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnClose = (Button) findViewById(R.id.btnClose);
        skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
        player = new MediaPlayer();
        btnPlay.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    private void initPlayHandler() {
        playerHandler = new Handler() {
            public void handleMessage(Message msg) {
                // 更新播放进度
                if (player != null) {
                    int position = player.getCurrentPosition();
                    int duration = player.getDuration();
                    if (duration > 0) {
                        tvProgressTime.setText(Utils.formatTime(position));
                        skbProgress.setProgress((int) position);
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlay:
                // 播放
                startPlay();
                break;
            case R.id.btnClose:
                stopPlay();// 关闭
                break;
        }
    }

    class MyResourceClient extends XWalkResourceClient {
        MyResourceClient(XWalkView view) {
            super(view);
        }

//        @Override
//        public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
//            Logger.i(url);
//            Intent intent = new Intent(activity,URLWebActivity.class);
//            intent.putExtra("url",url);
//            startActivity(intent);
//            return true;
//        }

        @Override
        public void onLoadStarted(XWalkView view, String url) {
            super.onLoadStarted(view, url);
        }

        @Override
        public void onLoadFinished(XWalkView view, String url) {
            super.onLoadFinished(view, url);
            activity.hideWaitDialog();
            if(mXwalkView != null){
                mXwalkView.requestFocus();
            }
        }
//        @Override
//        public void onReceivedResponseHeaders(XWalkView view, XWalkWebResourceRequest request,
//            XWalkWebResourceResponse response) {
//            int status = response.getStatusCode();
//            if(status != 200){
//                initNoNetworkLayout(url);
//            }
//        }
        

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
        @Override
        public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile,
            String acceptType,
            String capture) {
//            super.openFileChooser(view, uploadFile, acceptType, capture);
            if (uploadFile == null) {
                return;
            }
//                mUploadMessage = uploadFile;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                startActivityForResult(Intent.createChooser(i, "文件选择"), FILECHOOSER_RESULTCODE);
            mUploadMessage = uploadFile;
            Intent i=createDefaultOpenableIntent();
            startActivityForResult(
                Intent.createChooser(i, "文件选择"),
                FILECHOOSER_RESULTCODE);
        }

        private Intent createDefaultOpenableIntent() {
            // Create and return a chooser with the default OPENABLE
            // actions including the camera, camcorder and sound
            // recorder where available.
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");

//                Intent chooser = createChooserIntent(createCameraIntent(),createCamcorderIntent(), createSoundRecorderIntent());
            Intent chooser = createChooserIntent(createCameraIntent());
            chooser.putExtra(Intent.EXTRA_INTENT, i);
            return chooser;
        }

        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE, "文件选择");
            return chooser;
        }

        private Intent createCameraIntent() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                System.out.println("externalDataDir:" + externalDataDir);
            File cameraDataDir = new File(externalDataDir.getAbsolutePath()+ File.separator + "browser-photo");
            cameraDataDir.mkdirs();
            mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator+ System.currentTimeMillis() + ".jpg";
//                System.out.println("mcamerafilepath:" + mCameraFilePath);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(mCameraFilePath)));

            return cameraIntent;
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
        llAudioView.setVisibility(View.GONE);
        if(bookStateReceiver != null){
            unregisterReceiver(bookStateReceiver);
        }
        if(wxPayReceiver != null){
            unregisterReceiver(wxPayReceiver);
        }
     //   GreenDaoHelper.closeLocalBookDatabase();
        stopPlay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mXwalkView != null) {
            mXwalkView.onNewIntent(intent);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Logger.e("requestCode = " + requestCode+" resultCode = "+resultCode);
        if(requestCode == 1000){
            appCallWeb("",Constant.MSG_RE_BACK,"");
        }
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            String headPath = activity.getExternalFilesDir(null)+File.separator+"head"+File.separator+"head.jpg";
            File file = new File(activity.getExternalFilesDir(null)+File.separator+"head");
            if(!file.exists()){
                file.mkdir();
            }
            if(resultCode == Activity.RESULT_OK){
                if(intent != null){
                    Uri selectedImage = intent.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                    if(cursor != null){
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mCameraFilePath = cursor.getString(columnIndex);
                        cursor.close();
                    }else {
                        mCameraFilePath = selectedImage.getPath();
                    }
                }
            }
            File cameraFile = new File(headPath);
            File mCameraFile = new File(mCameraFilePath);
            if(!mCameraFile.exists()){
                if(mUploadMessage != null){
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }
                return;
            }
            Uri result = Uri.fromFile(mCameraFile);
            Intent intentCrop = new Intent("com.android.camera.action.CROP");
            intentCrop.setDataAndType(result, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intentCrop.putExtra("crop", "true");
            intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            // aspectX aspectY 是宽高的比例
            intentCrop.putExtra("aspectX", 1);
            intentCrop.putExtra("aspectY", 1);
            // outputX,outputY 是剪裁图片的宽高
            intentCrop.putExtra("outputX", CROP);
            intentCrop.putExtra("outputY", CROP);
            startActivityForResult(intentCrop, REQUEST_CODE_GETIMAGE_BYSDCARD);
            mCameraFilePath = headPath;
        }else if(requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD){
            if(resultCode == Activity.RESULT_OK){
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    Uri result = Uri.fromFile(cameraFile);
                    mUploadMessage.onReceiveValue(result);
                }
            }else {
                if(mUploadMessage != null){
                    mUploadMessage.onReceiveValue(null);
                }
            }
            mUploadMessage = null;
        }else {
            if (mXwalkView != null) {
                mXwalkView.onActivityResult(requestCode, resultCode, intent);
            }
        }
    }

    @Override
    public void appCallWeb(String command,String sn,String content) {
        super.appCallWeb(command,sn,content);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
           appCallWeb("", Constant.MSG_GO_BACK,"");
            Logger.i("on key ----"+keyCode);
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 书籍的状态接收
     */
    class BookStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //要下载的目标图书
            String isbn = intent.getStringExtra("isbn");
            String state = intent.getStringExtra("state");
            Logger.e("ACTION = "+intent.getAction());
            switch (intent.getAction()){
                case UPDATE_BOOK_STATE_ACTION:
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("downloadState",state);
                        jsonObject.put("isbn",isbn);
                        try {
                            Logger.i("webactivity---回传"+jsonObject.toString());
                            String response = URLEncoder.encode(jsonObject.toString(),"utf-8");
                            appCallWeb("",Constant.MSG_UPDATE_BOOK_STATE,response);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

  /**
   * 点读播放
   */
  public void playAudio(final String path){
//      new Thread(){
//          @Override
//          public void run() {
//              final MediaPlayer player = new MediaPlayer();
//              try {
//                  player.setDataSource(path);
//                  player.prepareAsync();
//                  player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                      @Override
//                      public void onPrepared(MediaPlayer mp) {
//                          // 播放器准备好
//                          player.start();
//                      }
//                  });
//                  player.setOnErrorListener(new OnErrorListener() {
//                      @Override
//                      public boolean onError(MediaPlayer mp, int what, int extra) {
//                          mp.reset();
//                          mp.release();
//                          return true;
//                      }
//                  });
//                  player.setOnCompletionListener(new OnCompletionListener() {
//                      @Override
//                      public void onCompletion(MediaPlayer mp) {
//                          player.reset();
//                          player.release();
//                      }
//                  });
//              } catch (IOException e) {
//                  e.printStackTrace();
//                  player.reset();
//                  player.release();
//              }
//          }
//      }.start();

      if (player == null){
          player = new MediaPlayer();
      }
      if (player.isPlaying())
      {
          player.stop();
          player.release();
          player = null;
          player = new MediaPlayer();
      }
      try {
          player.reset();
          player.setDataSource(path);
          player.prepareAsync();
          player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
              @Override
              public void onPrepared(MediaPlayer mp) {
                  // 播放器准备好
                  player.start();
              }
          });
          player.setOnErrorListener(new OnErrorListener() {
              @Override
              public boolean onError(MediaPlayer mp, int what, int extra) {
                  WebActivity.this.player.release();
                  return false;
              }
          });
          player.setOnCompletionListener(new OnCompletionListener() {
              @Override
              public void onCompletion(MediaPlayer mp) {
                  WebActivity.this.player.release();
                  WebActivity.this.player = null;
              }
          });
      } catch (IOException e) {
          e.printStackTrace();
          WebActivity.this.player.release();
          WebActivity.this.player = null;
      }
    }
    /**
     * 带进度条的音频播放
     * */
    public void playAudioControls(final String path){
        if(llAudioView.getVisibility() == View.VISIBLE){
            return;
        }
        if(player == null){
            player = new MediaPlayer();
        }
        player.reset();
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                tvProgressTime.setText("00:00:00");
                String formatTime = Utils.formatTime(player.getDuration());
                tvDuration.setText(formatTime);
                skbProgress.setMax(player.getDuration());
                llAudioView.setVisibility(View.VISIBLE);// 显示播放界面
                startPlay();
            }
        });
        player.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完毕
                if (player != null) {
                    btnPlay.setBackgroundResource(R.mipmap.icon_audio_start);
                    if (timerTask != null) {
                        timerTask.cancel();
                    }
                    tvProgressTime.setText(Utils.formatTime(0));
                    skbProgress.setProgress(0);
                    llAudioView.setVisibility(View.GONE);// 显示播放界面
                }
            }
        });
    }
    /**
     * 开始播放
     */
    public void startPlay() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
                btnPlay.setBackgroundResource(R.mipmap.icon_audio_start);
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
            } else {
                player.start();
                btnPlay.setBackgroundResource(R.mipmap.icon_audio_stop);
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        playerHandler.sendEmptyMessage(0);
                    }
                };
                timer.schedule(timerTask, 0, 100);
            }
        }
    }

    /**
     * 停止播放音频
     */
    public void stopPlay() {
        llAudioView.setVisibility(View.GONE);
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            btnPlay.setBackgroundResource(R.mipmap.icon_audio_stop);
            if (timerTask != null) {
                timerTask.cancel();
            }
            tvProgressTime.setText(Utils.formatTime(0));
            skbProgress.setProgress(0);
            player = null;
        }
    }

    OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (player != null && !player.isPlaying()) {
                player.start();
                btnPlay.setBackgroundResource(R.mipmap.icon_audio_stop);
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        playerHandler.sendEmptyMessage(0);
                    }
                };
                timer.schedule(timerTask, 0, 100);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (player != null && !player.isPlaying()) {
                btnPlay.setBackgroundResource(R.mipmap.icon_audio_start);
                player.pause();
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (player != null && fromUser) {
                player.seekTo(progress);
                int position = player.getCurrentPosition();
                tvProgressTime.setText(Utils.formatTime(position));
                skbProgress.setProgress((int) position);
            }
        }
    };


    public void registerWX(Handler handler){
        //如果是微信  注册成功广播
        IntentFilter payIntent = new IntentFilter(WXPayEntryActivity.WXPAY_ACTION);
        if(wxPayReceiver == null){
            wxPayReceiver = new WXPayReceiver(handler);
            activity.registerReceiver(wxPayReceiver,payIntent);
        }
    }

    /**
     * 支付宝微信支付处理
     */
    public void payment(final Payment payment, final Activity activity,final Handler handler,
        final String sn){
        if(TextUtils.equals("1",payment.getPayType())){
            //支付宝
            final String payInfo = "partner=\""+payment.getPartner()+"\"&seller_id=\""+payment.getSeller_id()+"\"&out_trade_no=\""
                + payment.getOut_trade_no()+"\"&subject=\""+payment.getSubject()+"\"&body=\""+payment.getBody()+"\""
                + "&total_fee=\""+payment.getTotal_fee()+"\"&notify_url=\""+payment.getNotify_url()+"\""
                + "&service=\""+payment.getService()+"\"&payment_type=\""+payment.getPayment_type()+"\""
                + "&_input_charset=\""+payment.get_input_charset()+"\"&it_b_pay=\""+payment.getIt_b_pay()+"\""
                + "&return_url=\""+payment.getReturn_url()+"\"&sign=\""+payment.getSign()+"\"&sign_type=\"RSA\"";
            Logger.i("payInfo------"+payInfo);
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(activity);
                    // 调用支付接口，获取支付结果
                    Map<String,String> map = alipay.payV2(payInfo,true);
                    PayResult payResult = new PayResult(map);
                    Logger.i("payResult------"+payResult.toString());
                    Message paymentMsg = Message.obtain();
                    Bundle payBundle = new Bundle();
                    Result result = new Result();
                    result.setData(payResult);
                    result.setSuccess(true);
                    result.setCommand(Constant.PAY_RESULT);
                    result.setSn(sn);
                    payBundle.putString("out_trade_no",payment.getOut_trade_no());
                    payBundle.putString("rechargeType",payment.getRechargeType());
                    paymentMsg.obj = result;
                    paymentMsg.setData(payBundle);
                    handler.sendMessage(paymentMsg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }else if(TextUtils.equals("2",payment.getPayType())){
            if (msgApi == null){
                msgApi = WXAPIFactory.createWXAPI(activity, Constant.AppID);
            }
            boolean regResult = msgApi.registerApp(Constant.AppID);
            Logger.e(" 微信支付注册app result = "+regResult);
            //微信
            PayReq req = new PayReq();
            req.appId			= payment.getAppid();
            req.partnerId		= payment.getPartnerid();
            req.prepayId		= payment.getPrepayid();
            req.nonceStr		= payment.getNoncestr();
            req.timeStamp		= payment.getTimestamp();
            req.packageValue	= payment.getPkg();
            req.sign			= payment.getSign();
            req.extData			= payment.getTradeNo()+"-"+sn+"-"+payment.getRechargeType(); // 订单号
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            msgApi.sendReq(req);
        }
    }

    /**
     * 微信支付成功接收广播
     */
    static class WXPayReceiver extends BroadcastReceiver{
        private Handler handler;
        //        private IWXAPI msgApi;
        public WXPayReceiver(Handler handler){
            super();
            this.handler = handler;
//            this.msgApi = msgApi;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
//            msgApi.unregisterApp();
            if(TextUtils.equals(intent.getAction(), WXPayEntryActivity.WXPAY_ACTION)){
                Logger.e("WXPayReceiver = ");
                int result = intent.getIntExtra("result",-1);
                String extData = intent.getStringExtra("extData");
                String[] extArr = extData.split("-");
                String tradeNo = extArr[0];//订单号
                String sn = extArr[1];//sn
                String rechargeType = extArr[2];//rechargeType 0:购书  1:充值
                String msg = "";
                boolean payResult = false;
                switch (result){
                    case 0:
                        payResult = true;
                        msg = "支付成功";
                        User user = MyApplication.getUser();
//                        showWaitDialog(handler);
                        if(TextUtils.equals(rechargeType,"0")){
                            DataUtil
                                .sendServiceResult(true,Constant.BOOK_WX_PAY_RESULT,extData,handler,sn);
                        }
                        //status:充值状态,0:订单开始,1:充值成功,2:充值失败
                        //tradeType:交易方式 0:购书,1充值
                        HttpUtils
                            .confirmTrade(user.getToken(),user.getId(),tradeNo,"0","1",handler,sn);
                        break;
                    case -1:
                        msg = "支付失败";
                        DataUtil.sendServiceResult(true,true,payResult,Constant.WX_PAY_RESULT,msg,null,handler,sn);
                        break;
                    case -2:
                        msg = "支付取消";
                        DataUtil.sendServiceResult(true,true,payResult,Constant.WX_PAY_RESULT,msg,null,handler,sn);
                        break;
                    case -3:
                        msg = "支付失败";
                        DataUtil.sendServiceResult(true,true,payResult,Constant.WX_PAY_RESULT,msg,null,handler,sn);
                        break;
                }
//                DataUtil.sendServiceResult(true,true,payResult,Constant.WX_PAY_RESULT,msg,null,handler,sn);
            }
        }
    }
}
