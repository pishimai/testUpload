package com.mvw.medicalvisualteaching.callback;

import static com.mvw.medicalvisualteaching.config.Constant.GET_BOOK_CATELOG;
import static com.mvw.medicalvisualteaching.utils.DataUtil.sendServiceResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.activity.BaseActivity;
import com.mvw.medicalvisualteaching.activity.CourseWebActivity;
import com.mvw.medicalvisualteaching.activity.LoginActivity;
import com.mvw.medicalvisualteaching.activity.MainActivity;
import com.mvw.medicalvisualteaching.activity.NavigationWebActivity;
import com.mvw.medicalvisualteaching.activity.VideoPlayActivity;
import com.mvw.medicalvisualteaching.activity.WebActivity;
import com.mvw.medicalvisualteaching.alipay.PayResult;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.Exam;
import com.mvw.medicalvisualteaching.bean.Payment;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.medicalvisualteaching.bean.Update;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.bean.UserBook;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.BookDao;
import com.mvw.medicalvisualteaching.db.dao.BookDao.Properties;
import com.mvw.medicalvisualteaching.db.dao.ExamDao;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao;
import com.mvw.medicalvisualteaching.db.dao.UserDao;
import com.mvw.medicalvisualteaching.dialog.ShareDialog;
import com.mvw.medicalvisualteaching.fragment.BookcaseFragment;
import com.mvw.medicalvisualteaching.utils.DataUtil;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.HttpUtils;
import com.mvw.medicalvisualteaching.utils.LocalBookUtilAnother;
import com.mvw.medicalvisualteaching.utils.SPUtil;
import com.mvw.medicalvisualteaching.utils.StringUtils;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.JavascriptInterface;


public class BaseJavaScriptInterface {
    private  MyHandler handler;
    private BaseActivity activity;
    private long startTime;

    public BaseJavaScriptInterface(BaseActivity activity){
        this.activity = activity;
        handler = new MyHandler(activity);

    }

    private static class MyHandler extends Handler {
        private final WeakReference mActivity;

        private MyHandler(BaseActivity activity) {
            mActivity = new WeakReference<>(activity);

        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = (BaseActivity) mActivity.get();
            if (activity != null) {
                User user = MyApplication.getUser();
                Result result = (Result) msg.obj;
                String response = result.getResponse();
                String sn = result.getSn();
                if(result.isSuccess()){
                    try {
                        if(response != null){
                            String str  = URLDecoder.decode(response,"utf-8");
                            Logger.i(str);
                        }
                        switch (result.getCommand()){
                            case Constant.OPEN_LOADINGNG:
                                activity.showWaitDialog();
                                break;
                            case Constant.MSG_REFRESH://购买书籍套餐刷新
                                DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,Constant.MSG_REFRESH);
                                break;
                            case Constant.LOGOUT:
//                            String ss = "{\"serviceResult\":{\"result\":{\"content\":\"测试数据\"},\"flag\":true},\"errorMessage\":\"\",\"opFlag\":true,\"timestamp\":\"2017-04-28 20:42:00:134\"}";
                                if(TextUtils.equals("E012",response)){
                                    //书城token过期，发送退出命令
                                    showTokenExpiredDialog(activity,this,"");
                                    return;
                                }
                                //退出登录
                                //判断是不是游客登录，游客登录不进行token燃烧
                                if(!"00010".equals(user.getRoles())){
                                    //不是游客登录
                                    HttpUtils.burnHumanToken(user,this,"");
                                }
                                MyApplication.setUser(null);
                                UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                                userDao.deleteAll();
                                Intent intent = new Intent(activity, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(intent);
                                activity.finish();
                                break;

                            case Constant.CMD_PLAY_AUDIO:
                                if(activity instanceof WebActivity){
                                    WebActivity webActivity = (WebActivity) activity;
                                    JSONObject audioArgs = new JSONObject(response);
                                    String audioSrc="";
                                    if (audioArgs.has("src")){
                                        audioSrc = audioArgs.getString("src");
                                    }
                                    String audioIsbn = audioArgs.getString("isbn");
                                    BookDao bookDB = GreenDaoHelper.getDaoSession().getBookDao();
                                    Book localBook = bookDB.queryBuilder().where(Properties.Isbn.eq(audioIsbn)).unique();
                                    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                                    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+audioIsbn)).unique();
                                    String src;
                                    Logger.i("audioSrc------"+audioSrc);
                                    if(localBook != null && userBook != null && Constant.BOOK_COMPLETED.equals(userBook.getState())&&!TextUtils.isEmpty(audioSrc)){
                                        //本地音频
                                        if (!audioSrc.contains(localBook.getIsbn())){
                                            src = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+audioSrc+Constant.AUDIO_SUFFIX;
                                        }else{
                                            src = audioSrc+Constant.AUDIO_SUFFIX;
                                        }
                                    }else {
                                        //在线音频
                                        if (!audioSrc.contains(Constant.AUDIO_SUFFIX)){
                                            src = audioSrc+Constant.AUDIO_SUFFIX;
                                        }else {
                                            src = audioSrc;
                                        }

                                    }
                                    Logger.i("src------"+src);
                                    if(audioArgs.has("controls")){
                                        webActivity.playAudioControls(src);
                                    }else {
                                        webActivity.playAudio(src);
                                    }
                                }
                                break;
                            case Constant.CMD_PLAY_VIDEO:
                                //播放视频
                                //本地播放在线或离线的视频
                                JSONObject videoArgs = new JSONObject(response);
                                String videoSrc= "";
                                if(videoArgs.has("src")){
                                    videoSrc = videoArgs.getString("src");
                                }
                                Logger.i("video-----"+videoSrc);
                                String videoIsbn = videoArgs.getString("isbn");
                                BookDao videoBookDao = GreenDaoHelper.getDaoSession().getBookDao();
                                Book videoBook = videoBookDao.queryBuilder().where(Properties.Isbn.eq(videoIsbn)).unique();
                                UserBookDao videoUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                                UserBook videoUserBook = videoUserBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+videoIsbn)).unique();
                                String src;
//                                if(videoBook != null && videoUserBook != null && Constant.BOOK_COMPLETED.equals(videoUserBook.getState())){
//                                    //本地视频
//                                    src = videoBook.getDownloadPath() + File.separator + videoBook.getIsbn()+File.separator+Constant.LOCAL_BOOK_FOLDER+videoSrc;
//                                }else {
//                                    //在线视频
//                                    src = videoSrc;
//                                }
                                if (!videoSrc.contains(".mp4")){
                                    src = videoSrc+".mp4";
                                }else{
                                    src = videoSrc;
                                }
                                Intent videoIntent = new Intent(activity, VideoPlayActivity.class);
                                videoIntent.putExtra("url",src);
                                activity.startActivity(videoIntent);
                                break;
                            case Constant.CMD_BACK_STOP:
                                if (activity instanceof  WebActivity){
                                    ((WebActivity) activity).stopPlay();
                                }
                                break;
                            case Constant.CMD_CHECK_UPDATE://我的设置里的版本更新检查
                                Update update = (Update) result.getData();
                                if(update != null){
                                    if(TextUtils.equals(update.getAction(),"update")){
                                        //有更新数据
                                        MainActivity.initUpdateDialog(update,activity);
                                    }else if(TextUtils.equals(update.getAction(),"force")){
                                        //有更新且强制更新
                                        update.setAction("update");
                                        MainActivity.initUpdateDialog(update,activity);
                                    }else if(TextUtils.equals(update.getAction(),"false")){
                                        //最新版本没有更新数据
                                        Toast.makeText(activity,activity.getString(R.string.update_check),Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(activity,activity.getString(R.string.update_check_failed),Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case Constant.CMD_DIALING://拨打电话
                                try{
                                    activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(
                                        "tel:"+response)));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case Constant.GET_BOOK_CHAPTER_LOCAL:
                            case Constant.GET_BOOK_PARAGRAPH_LOCAL:
                            case Constant.GET_BOOK_MEDIA_LOCAL:
                            case Constant.GET_BOOK_CATELOG_LOCAL:
                            case Constant.GET_AUTHOR_PROFILE_LOCAL:
                            case Constant.GET_BOOK_STATE:
                                Map<String,Object> data = (Map<String, Object>) result.getData();
                                Gson gson = new Gson();
                                response = gson.toJson(data);
                                Logger.i("response-----"+response);
                                long start = System.currentTimeMillis();
//                                String ss = response.replace("\\n","").replace("\\r","").replace("\\u003d","=");
                                response = URLEncoder.encode(response,"utf-8");
                                Logger.i("替换耗时： "+(System.currentTimeMillis() - start));
                                activity.appCallWeb(result.getCommand(),sn,response);
//                                activity.appCallWeb(result.getCommand(),sn,ss,"JSON");

                                break;

                            case Constant.GET_BOOK_MEDIA:
                            case Constant.GET_BOOK_CHAPTER:
                            case Constant.GET_BOOK_CATELOG:
                            case Constant.GET_AUTHOR_PROFILE:
                            case Constant.GET_BOOK_PARAGRAPH:

                            case Constant.GET_BOOK_SHELF_LIST:
                            case Constant.GET_PURCHASE_BOOK_LIST:

                            case Constant.GET_CROSS_SEARCH_BOOK_LIST:
                            case Constant.GET_CROSS_SEARCH_BOOK:
                            case Constant.GET_RECHARGE_PACKAGE_LIST:
                            case Constant.GET_IS_PURCHASED_BOOK:
                            case Constant.CMD_DELETE_FROM_BOOKSHELF:
                            case Constant.UPDATE_CUSTOMER_INFO:
                            case Constant.WX_PAY_RESULT:
                            case Constant.GET_BOOK_CHAPTER_EXAM_LIST:
                            case Constant.GET_CUSTOMER_INFO:
                            case Constant.GET_DOWNLOAD_LOCATION:
                            case Constant.SET_DOWNLOAD_LOCATION:
                            case Constant.GET_RECHARGE_CONSUMPTION_LIST:
                            case Constant.GET_COUPON_LIST:
                            case Constant.POST_RESET_PASSWORD:
                            case Constant.GET_APP_VERSION:
                            case Constant.GET_NETWORK_STATE:
                            case Constant.GET_BOOK_SHELF_AD:
                            case Constant.CMD_GET_EXAMANSWERS:
                            case Constant.POST_FEEDBACK:
                                String jsonResponse = URLDecoder.decode(response,"utf-8");
                                Logger.i(response);
                                //{"errorMessage":"E012-令牌错误或已过期！","opFlag":false,"serviceResult":null,"timestamp":""}
                                if(jsonResponse.contains("E012")){
                                    //退出登录
//                                    Toast.makeText(activity,"您的账号已在另一台设备登录",Toast.LENGTH_SHORT).show();
                                    showTokenExpiredDialog(activity,this,sn);
                                }else {
                                    activity.appCallWeb(result.getCommand(),sn,response);
                                }
                                break;
                            case Constant.GET_BOOK_VERSION:
                                //当前书籍最新版本结果
                                String bookVersionResponse = URLDecoder.decode(response,"utf-8");
                                JSONObject bookVersionObject = new JSONObject(bookVersionResponse);
                                String bookVersionOpFlag = bookVersionObject.getString("opFlag");
                                if(TextUtils.equals(bookVersionOpFlag,"true")) {
                                    JSONObject bookVersionServiceResult = new JSONObject(
                                        bookVersionObject.getString("serviceResult"));
                                    String flag = bookVersionServiceResult.getString("flag");
                                    if (TextUtils.equals(flag, "true")) {
                                        DataUtil.sendBookBroadcast(activity,BookcaseFragment.START_UPDATE_BOOK_ACTION,"isbn",null,null);
                                    }
                                }
                                break;
                            case Constant.DOWNLOAD_BS_BOOK:
                            case Constant.CMD_PURCHASE_SUCCESS:
                                String bsBookResponse = URLDecoder.decode(response,"utf-8");
                                JSONObject bsBookObject = new JSONObject(bsBookResponse);
                                String bsBookOpFlag = bsBookObject.getString("opFlag");
                                if(TextUtils.equals(bsBookOpFlag,"true")){
                                    JSONObject bsBookServiceResult = new JSONObject(bsBookObject.getString("serviceResult"));
                                    String flag = bsBookServiceResult.getString("flag");
                                    if(TextUtils.equals(flag,"true")){
                                        JSONObject bookObject = bsBookServiceResult.getJSONObject("result");
                                        if(bookObject != null){
                                            if(Constant.DOWNLOAD_BS_BOOK.equals(result.getCommand())){
                                                //下载通知
                                                DataUtil.sendBookBroadcast(activity,BookcaseFragment.START_DOWNLOAD_ACTION,bookObject.toString(),null,null);
                                            }else {
                                                //购买成功通知已购
                                                DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_OBTAINED_ACTION,bookObject.toString(),"obtained",null);
                                            }
                                        }
                                    }
                                }
                                break;
                            case Constant.POST_VERIFICATION_CODE:
                                //激活码
                                String verificationCodeResponse = URLDecoder.decode(response,"utf-8");
                                activity.appCallWeb(result.getCommand(),sn,response);
                                JSONObject verCodeObject = new JSONObject(verificationCodeResponse);
                                String verCodeOpFlag = verCodeObject.getString("opFlag");
                                if(TextUtils.equals(verCodeOpFlag,"true")){
                                    JSONObject verCodeServiceResult = new JSONObject(verCodeObject.getString("serviceResult"));
                                    String flag = verCodeServiceResult.getString("flag");
                                    if(TextUtils.equals(flag,"true")){
                                        //激活成功，发送广播刷新书架
                                        DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,"");
                                        DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_OBTAINED_ACTION,"");
//                                        MyApplication.bookShelfRefresh = true;
//                                        MyApplication.bookObtainedRefresh = true;
                                    }
                                }
                                break;
                            case Constant.PAYMENT:
                                activity.hideWaitDialog();
                                if(result.getData() != null){
                                    Payment payment = (Payment) result.getData();
                                    IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);
                                    if(TextUtils.equals("2",payment.getPayType())){
                                        //微信支付，判断是否安装微信
                                        if (!msgApi.isWXAppInstalled()) {
                                            String rechargeResult = activity.getResources().getString(R.string.pay_wx_client_install);
                                            sendServiceResult(true,true,false,Constant.WX_PAY_RESULT,rechargeResult,null,this,sn);
                                            return;
                                        } else if (!msgApi.isWXAppSupportAPI()) {
                                            String rechargeResult = activity.getResources().getString(R.string.pay_wx_client_version);
                                            sendServiceResult(true,true,false,Constant.WX_PAY_RESULT,rechargeResult,null,this,sn);
                                            return;
                                        }
//                                        //如果是微信  注册成功广播
//                                        IntentFilter payIntent = new IntentFilter(WXPayEntryActivity.WXPAY_ACTION);
//                                        if(wxPayReceiver == null){
//                                            wxPayReceiver = new WXPayReceiver(this);
//                                            activity.registerReceiver(wxPayReceiver,payIntent);
//                                        }
                                        if(activity instanceof WebActivity){
                                            WebActivity webActivity = (WebActivity) activity;
                                            webActivity.registerWX(this);
                                        }
                                    }
                                    if(activity instanceof WebActivity){
                                        WebActivity webActivity = (WebActivity) activity;
                                        webActivity.payment(payment,activity,this,sn);
                                    }
                                }else {
                                    Toast.makeText(activity, R.string.pay_failed,Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case Constant.BOOK_WX_PAY_RESULT:
                                //购书微信支付成功
                                Logger.i("tradeNo-----"+response);
                                String[] extArr = response.split("-");
                                String tradeNo = extArr[0];//订单号
                                Logger.i("tradeNo------"+tradeNo);
                                String wxRechargeType = extArr[2];//rechargeType 0:购书  1:充值
                                String content = "{\"tradeNo\":\""+tradeNo+"\"}";
                                activity.appCallWeb(result.getCommand(),Constant.BOOK_PAY_RESULT,content);
                                break;
                            case Constant.PAY_RESULT:
                                PayResult payResult = (PayResult) result.getData();
                                if(payResult == null){
                                    Toast.makeText(activity, R.string.pay_failed,Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Bundle payBundle = msg.getData();
                                String out_trade_no = payBundle.getString("out_trade_no");
                                String rechargeType = payBundle.getString("rechargeType");
                                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//                                String resultInfo = payResult.getResult();
                                String resultStatus = payResult.getResultStatus();
                                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                if (TextUtils.equals(resultStatus, "9000")) {
//                                    Toast.makeText(activity, R.string.pay_success,Toast.LENGTH_SHORT).show();
                                    //通知界面
                                    if(TextUtils.equals("0",rechargeType)){
                                        //购书成功，通知书城

                                        String payResultContent = "{\"tradeNo\":\""+out_trade_no+"\"}";
                                        activity.appCallWeb(result.getCommand(),Constant.BOOK_PAY_RESULT,payResultContent);
                                    }
                                    //status:充值状态,0:订单开始,1:充值成功,2:充值失败
                                    //tradeType:交易方式 0:购书,1充值
                                    activity.showWaitDialog();
                                    //去服务端验证
                                    HttpUtils.confirmTrade(user.getToken(),user.getId(),out_trade_no,"0","1",this,sn);
                                } else if (TextUtils.equals(resultStatus, "8000")){
                                    // 判断resultStatus 为非“9000”则代表可能支付失败
                                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                    Toast.makeText(activity, R.string.pay_loading,Toast.LENGTH_SHORT).show();
                                } else if(TextUtils.equals(resultStatus, "6001")){
                                    //resultStatus={6001};memo={用户取消};result={}
//                                    Toast.makeText(activity, "支付取消",Toast.LENGTH_SHORT).show();//通知界面
                                    String payResponse= DataUtil.responseServiceResult(true,false,"支付取消",null);
                                    activity.appCallWeb(result.getCommand(),sn,payResponse);

                                }else {
                                    // 其他值就可以判断为支付失败或者系统返回的错误
//                                    Toast.makeText(activity, R.string.pay_failed,Toast.LENGTH_SHORT).show();
                                    String payResponse= DataUtil.responseServiceResult(true,false,"支付失败",null);
                                    activity.appCallWeb(result.getCommand(),sn,payResponse);
                                }
                                break;
                            case Constant.CONFIRM_TRADE:
                                activity.hideWaitDialog();
                                //{"errorMessage":"","opFlag":"true","serviceResult":"{\"flag\":\"true\",\"result\":{}}","timestamp":"2017-01-23 11:35:26:617"}
                                //{"errorMessage":"","opFlag":"true","serviceResult":"{\"flag\":\"true\",\"result\":{}}","timestamp":"2017-05-25 15:16:48:903"}
//                                DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,"");//通知书架刷新数据
                                String tradeResponse = URLDecoder.decode(response,"utf-8");
                                JSONObject tradeObject = new JSONObject(tradeResponse);
                                String tradeResult = tradeObject.getString("serviceResult");
                                JSONObject tradeResultObject = new JSONObject(tradeResult);
                                String tradeFlag = tradeResultObject.getString("flag");
                                if(TextUtils.equals("true",tradeFlag)){
                                    //服务端确认支付成功，进行界面刷新
                                    String payResponse= DataUtil.responseServiceResult(true,true,"支付成功",null);
                                    activity.appCallWeb(result.getCommand(),sn,payResponse);
//                                    Toast.makeText(activity,"支付成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    String payResponse= DataUtil.responseServiceResult(true,false,"支付失败",null);
                                    activity.appCallWeb(result.getCommand(),sn,payResponse);
//                                    Toast.makeText(activity,"支付结果失败，请稍候",Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }catch ( JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, R.string.http_exception_error,Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //请求失败,通知界面
                    activity.hideWaitDialog();
//                    Toast.makeText(activity, R.string.http_exception_error,Toast.LENGTH_SHORT).show();
                    String responseError = DataUtil.responseServiceResult(false,false,activity.getString(R.string.http_exception_error),null);
                    activity.appCallWeb(result.getCommand(),sn,responseError);
                }
            }
        }
    }
    @JavascriptInterface
    public void WebCallApp(String content) {
        Logger.e(content);
        try {
            JSONObject jsonObject = new JSONObject(content);
            String command = jsonObject.getString("command");
            String sn = null;
            if(jsonObject.has("sn")){
                sn = jsonObject.getString("sn");
            }
            JSONObject args = jsonObject.getJSONObject("args");
            User user = MyApplication.getUser();
            switch (command){
                case Constant.CMD_GO_BACK:
                    if(activity instanceof MainActivity){
                        //如果销毁首页界面，进行二次确认退出
                        long ms = System.currentTimeMillis() - startTime;
                        if(ms <= 2000){
                            activity.finish();
                        }else{
                            Toast.makeText(activity,activity.getString(R.string.exit_hint),Toast.LENGTH_SHORT).show();
                        }
                        startTime = System.currentTimeMillis();
                    }else{
//                        if(wxPayReceiver != null){
//                            //注销微信支付广播
//                            activity.unregisterReceiver(wxPayReceiver);
//                            wxPayReceiver = null;
//                        }
                        activity.finish();
                    }
                    break;
                case Constant.CMD_BACK_STOP://停止播放音频
                    sendServiceResult(true,Constant.CMD_BACK_STOP,args.toString(),handler,sn);
                    break;
                case Constant.CMD_GET_EXAMANSWERS://获取考试答案
                    ExamDao examDao2 = GreenDaoHelper.getDaoSession().getExamDao();
                    String examId = args.getString("examId");
                    List<Exam> list = examDao2.queryBuilder().where(ExamDao.Properties.ExamId.eq(examId)).orderAsc(ExamDao.Properties.QuestionId).list();
                    Gson gson2 = new Gson();

                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("datas",list);
                    map.put("leftSeconds",SPUtil.getInstance(activity).getLong(examId,0));

                    Map<String,Object> serviceResult = new ArrayMap<>();
                    serviceResult.put("result",map);
                    serviceResult.put("flag",true);

                    Map<String,Object> resultData = new HashMap<String, Object>();
                    resultData.put("serviceResult",serviceResult);
                    resultData.put("errorMessage","");
                    resultData.put("opFlag",true);
                    resultData.put("timestamp","");

                    String response = gson2.toJson(resultData);
                    response = URLEncoder.encode(response,"utf-8");
                    sendServiceResult(true, Constant.CMD_GET_EXAMANSWERS,response,handler,sn);
                    break;
                case Constant.CMD_SAVE_ANSWER://保存考试答案
                    ExamDao examDao1 = GreenDaoHelper.getDaoSession().getExamDao();
                    String id = args.getString("examId");
                    String question = args.getString("questionId");
                    String answer = args.getString("answer");
                    long seconds = Long.parseLong(args.getString("leftSeconds"));
                    SPUtil.getInstance(activity).save(id,seconds);
                    Exam exam = new Exam();
                    exam.setAnswer(answer);
                    exam.setId(id+"+"+question);
                    exam.setExamId(id);
                    exam.setQuestionId(question);
                    exam.setLeftSeconds(seconds);
                    exam.setCreateTime(new Date());
                    examDao1.insertOrReplace(exam);
                    break;
                case Constant.CMD_CLEAR_EXAM_ANSWERS://清除答案
                    String examId1 = args.getString("examId");
                    String sql = "delete from Exam where examId = "+"'"+examId1+"'";
                    Database database = GreenDaoHelper.getDaoSession().getDatabase();
                    database.execSQL(sql);
                    SPUtil.getInstance(activity).remove(examId1);
                    break;
                case Constant.CMD_OPEN_URL:
                    //打开一个新界面  {"args":{"url":"ui/phone/_book.html?id=1"},"command":"openUrl","sn":"20170117104425247357"}
                    String url = args.getString("url");
                    // TODO: 2017/10/17  
                    if (args.has("book")){
                        String isbn = args.getString("isbn");
                        BookDao bookDao = GreenDaoHelper.getDaoSession().getBookDao();
                        UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                        if (!TextUtils.isEmpty(isbn)){
                         //   Book bsBook = bookDao.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
                            UserBook userBook = userBookDao.queryBuilder().where(
                                UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
                            if (userBook==null){ //本地书籍为空
                                Book book = new Gson().fromJson(args.getJSONObject("book").toString(),Book.class);
                                if (book!=null){
                                    if (book.getTextbook().equals(Constant.TEXT_BOOK)&& book.getTextbookType().equals(Constant.TEXT_BOOK)){
                                        bookDao.insertOrReplace(book);
                                        DataUtil.saveUserBook(userBookDao,book,Constant.UNDOWNLOAD,book.getBuyStatus());
                                        DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKSHELF_ACTION,args.getJSONObject("book").toString());
                                    }
                                }
                            }
                        }
                    }
                    if(url != null){
                        if(args.has("target")){
                            //浏览器打开地址
                            try {
                                Intent browseIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                                activity.startActivity(browseIntent);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else {
                            Map<String,String> params = new HashMap<>();
                            params.put("token",user.getToken());
                            params.put("resourceType","0");
                            params.put("platform",Constant.PLATFORM);
//                            if(args.has("replaceSid")){
//                                String isbn = args.getString("isbn");
//                                UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
//                                UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
//                                if (userBook!=null && !TextUtils.isEmpty(userBook.getSectionId())){
//                                    params.put("sid",userBook.getSectionId());
//                                }else{
//                                    params.put("sid","");
//                                }
//                                if(userBook!=null&&Constant.BOOK_COMPLETED.equals(userBook.getState())){
//                                    //打开离线看书的新界面，要先判断该书籍是不是需要升级
//
//                                }
//                            }
                            //生成新的url地址
                            if (args.has("static")){
                                if (!args.getString("static").equals("1")){
                                    url = StringUtils.createUrl(url,params,activity);
                                }
                            }else{
                                url = StringUtils.createUrl(url,params,activity);
                            }
                            Intent openIntent = new Intent();
                            if(args.has("appBack")){
                                //跳转带返回按钮的界面
                                openIntent.setClass(activity, CourseWebActivity.class);
                            }else if(args.has("navigation") && TextUtils.equals("1",args.getString("navigation"))){
                                //跳转带返回按钮的界面
                                openIntent.setClass(activity, NavigationWebActivity.class);
                                if(args.has("name")){
                                    String name = args.getString("name");
                                    if(name != null){
                                        openIntent.putExtra("name",name);
                                    }
                                }
                            }else {
                                //跳转不带返回键的界面
                                openIntent.setClass(activity, WebActivity.class);
                            }
                            openIntent.putExtra("url",url);
                            Logger.i("url = "+url);
                            activity.startActivityForResult(openIntent,1000);
                        }
                    }
                    break;
                case Constant.CMD_OPEN_PDF:
                    //{"args":{"bookId":"7-80194-732-0.pdf.enc"},"command":"openPDFBook","sn":"20170113173855344575"}
                    String pdfId = args.getString("isbn");
                    DataUtil.saveUserBookOrder(pdfId,true);
     //               DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,"");
                    openPDF(pdfId);
                    break;
                case Constant.CMD_DOWNLOAD_BOOK:
                    //下载图书
                    String downloadBookJson = args.getJSONObject("book").toString();
                    String downloadBookFrom = "";
                    String nonWifi= "";
                    if(args.has("from")){
                        downloadBookFrom = args.getString("from");//"from":"obtained"从已购图书下载
                    }
                    if (args.has("nonWifi")){
                        nonWifi = args.getString("nonWifi");
                    }

                    //从已购点击下载图书，通知h5去刷新书架
                    DataUtil.sendBookBroadcast(activity,BookcaseFragment.START_DOWNLOAD_ACTION,downloadBookJson,downloadBookFrom,nonWifi);
                    break;
                case Constant.CMD_UPDATE_BOOK:
                    //更新图书
                    String updateBookJson = args.getJSONObject("book").toString();
                    DataUtil.sendBookBroadcast(activity,BookcaseFragment.START_UPDATE_BOOK_ACTION,updateBookJson);
                    break;
                case Constant.CMD_CANCEL_DOWNLOAD_BOOK:
                    //取消下载书籍
                    String cancelIsbn = args.getString("isbn");
                    String cancelBookJson = args.getJSONObject("book").toString();
                    DataUtil.sendBookBroadcast(activity,BookcaseFragment.CANCEL_DOWNLOAD_ACTION,cancelIsbn);
                    FileUtils.saveLog(activity,Constant.CMD_CANCEL_DOWNLOAD_BOOK,"","",cancelIsbn);
                    break;
                case Constant.CMD_PAUSE_DOWNLOAD_BOOK:
                    //暂停下载书籍
                    String pauseIsbn = args.getString("isbn");
                    String stopBookJson = args.getJSONObject("book").toString();
                    DataUtil.sendBookBroadcast(activity,BookcaseFragment.STOP_DOWNLOAD_ACTION,pauseIsbn);
                    FileUtils.saveLog(activity,Constant.CMD_PAUSE_DOWNLOAD_BOOK,"","",pauseIsbn);
                    break;
                case Constant.DOWNLOAD_BS_BOOK:
                    //从书城点击下载书籍
                    String bsBookId = args.getString("id");
                    String nonWifi1 = "";
                    if (args.has("nonWifi")){
                        nonWifi1 = args.getString("nonWifi");
                    }
                    //先判断本地书架有没有，没有需要网络获取然后添加到书架，有的话直接送下载命令
                    UserBookDao bsUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                    UserBook bsUserBook = bsUserBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+bsBookId)).unique();
                    if(bsUserBook != null){
                        BookDao bsBookDao = GreenDaoHelper.getDaoSession().getBookDao();
                        Book bsBook = bsBookDao.queryBuilder().where(Properties.Isbn.eq(bsBookId)).unique();
                        Gson gson = new Gson();
                        String bookJson = gson.toJson(bsBook);
                        DataUtil.sendBookBroadcast(activity,BookcaseFragment.START_DOWNLOAD_ACTION,bookJson,null,nonWifi1);
                    }else {
                        HttpUtils.getBookDetail(user,bsBookId,handler,sn,Constant.DOWNLOAD_BS_BOOK);
                    }
                    FileUtils.saveLog(activity,Constant.DOWNLOAD_BS_BOOK,"","",bsBookId);
                    break;
                case Constant.GET_BOOK_VERSION:
                    //验证是否需要更新书籍

                    break;
                case Constant.GET_BOOK_SHELF_LIST:
                    //获取书架数据
                    SPUtil.getInstance(activity).save(Constant.BOOK_STORAGE_POSITION,1);
                    Logger.i(user.getId()+"   "+user.getToken());
                    boolean refresh = false;
                    if(args.has("refresh")){
                        refresh = args.getBoolean("refresh");
                    }
                    HttpUtils.bookShelf(user,refresh,handler,sn,activity);
                    FileUtils.saveLog(activity,Constant.GET_BOOK_SHELF_LIST,"","");
                    break;
                case Constant.GET_BOOK_SHELF_AD:
                    //获取书架轮播图
                    HttpUtils.getBookAd(user,handler,sn,Constant.GET_BOOK_SHELF_AD);
                    FileUtils.saveLog(activity,Constant.GET_BOOK_SHELF_AD,"","");
                    break;
                case GET_BOOK_CATELOG:
                    //获取书籍章
                    //先判断本地有没有下载完成的书籍
                    String isbn = args.getString("isbn");
                    String network = "";
                    if (args.has("network")){
                        network = args.getString("network");
                    }
                    DataUtil.saveUserBookOrder(isbn);
//                    DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,"");
                    BookDao bookDB = GreenDaoHelper.getDaoSession().getBookDao();
                    Book localBook = bookDB.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
                    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
                    if(!TextUtils.equals(network,"online")&&localBook != null && userBook != null && Constant.BOOK_COMPLETED.equals(userBook.getState())){
                        //离线
//                        LocalBookUtil.getLocalBookCatelog(localBook,activity,handler,sn);
                        LocalBookUtilAnother.getLocalBookCatelog(localBook,activity,handler,sn);
                        FileUtils.saveLog(activity, GET_BOOK_CATELOG,"","",isbn,"","","1");
                    }else {
                        //在线
                        HttpUtils.getBookCatelog(MyApplication.getUser().getToken(),isbn,handler,sn);
                        FileUtils.saveLog(activity, GET_BOOK_CATELOG,"","",isbn,"","","0");
                    }
                    break;
                case Constant.GET_BOOK_CHAPTER:
                    //章节内容
                    String chapterIsbn = args.getString("isbn");
                    String chapterSectionId = args.getString("sectionId");
                    String network2 = "";
                    if (args.has("network")){
                        network2 = args.getString("network");
                    }
                    String chapterToken = MyApplication.getUser().getToken();
                    BookDao chapterBookDB = GreenDaoHelper.getDaoSession().getBookDao();
                    Book chapterLocalBook = chapterBookDB.queryBuilder().where(Properties.Isbn.eq(chapterIsbn)).unique();
                    UserBookDao chapterUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                    UserBook chapterUserBook = chapterUserBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+chapterIsbn)).unique();
                    if(chapterUserBook != null){
                        if(!args.has("extend")){
                            chapterUserBook.setSectionId(chapterSectionId);
                            chapterUserBookDao.insertOrReplace(chapterUserBook);//记录该用户当前读取的章节
                        }
                    }
                    if(!TextUtils.equals(network2,"online")&&chapterLocalBook != null && chapterUserBook != null && Constant.BOOK_COMPLETED.equals(chapterUserBook.getState())){
                        //离线
//                        LocalBookUtil.getLocalBookChapter(chapterLocalBook,activity,chapterSectionId,handler,sn);
                        LocalBookUtilAnother.getLocalBookChapter(chapterLocalBook,activity,chapterSectionId,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_BOOK_CHAPTER,"","",chapterIsbn,"",chapterSectionId,"1");
                    }else {
                        //在线
                        HttpUtils.bookChapter(chapterToken,chapterSectionId,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_BOOK_CHAPTER,"","",chapterIsbn,"",chapterSectionId,"0");
                    }
                    break;

                case Constant.GET_BOOK_PARAGRAPH:
                    //段落数据
                    String paragraphIsbn = args.getString("isbn");
                    String  paragraphId = args.getString("paragraphId");
                    String paragraphToken = MyApplication.getUser().getToken();
                    String network3 = "";
                    if (args.has("network")){
                        network3 = args.getString("network");
                    }
                    BookDao paragraphBookDB = GreenDaoHelper.getDaoSession().getBookDao();
                    Book paragraphBook = paragraphBookDB.queryBuilder().where(Properties.Isbn.eq(paragraphIsbn)).unique();
                    UserBookDao paragraphUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                    UserBook paragraphUserBook = paragraphUserBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+paragraphIsbn)).unique();

                    if(!TextUtils.equals(network3,"online")&&paragraphBook != null && paragraphUserBook != null && Constant.BOOK_COMPLETED.equals(paragraphUserBook.getState())){
                        //离线
//                        LocalBookUtil.getLocalBookChapter(chapterLocalBook,activity,chapterSectionId,handler,sn);
                        LocalBookUtilAnother.getParagraphById(paragraphBook,paragraphId,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_BOOK_PARAGRAPH,"","",paragraphIsbn,"",paragraphId,"1");
                    }else {
                        //在线
                        HttpUtils.getBookParagraph(paragraphToken,paragraphIsbn,paragraphId,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_BOOK_PARAGRAPH,"","",paragraphIsbn,"",paragraphId,"0");
                    }
                    break;
                case Constant.GET_BOOK_MEDIA:
                    //书籍内容多媒体
                    String mediaIsbn = args.getString("isbn");
                    String mediaId = args.getString("mediaId");
                    String mediaType = "";
                    if(args.has("type")){
                        mediaType = args.getString("type");
                    }
                    String network4 = "";
                    if (args.has("network")){
                        network4 = args.getString("network");
                    }
                    BookDao mediaBookDB = GreenDaoHelper.getDaoSession().getBookDao();
                    Book mediaBook = mediaBookDB.queryBuilder().where(Properties.Isbn.eq(mediaIsbn)).unique();
                    UserBookDao mediaUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                    UserBook mediaUserBook = mediaUserBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+mediaIsbn)).unique();
                    if(!TextUtils.equals(network4,"online")&&mediaBook != null && mediaUserBook != null &&Constant.BOOK_COMPLETED.equals(mediaUserBook.getState())){
                        if(TextUtils.equals(mediaType,"table")){
//                            LocalBookUtil.getTableText(mediaBook,activity,mediaId,handler,sn);
                            LocalBookUtilAnother.getTableText(mediaBook,activity,mediaId,handler,sn);
                        }else {
//                            LocalBookUtil.getBookMedia(mediaBook,activity,mediaId,handler,sn);
                            LocalBookUtilAnother.getBookMedia(mediaBook,activity,mediaId,handler,sn);
                        }
                        FileUtils.saveLog(activity,Constant.GET_BOOK_MEDIA,"","",mediaIsbn,"",mediaId,"1");
                    }else {
                        HttpUtils.bookMedia(user.getToken(),mediaIsbn,mediaId,mediaType,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_BOOK_MEDIA,"","",mediaIsbn,"",mediaId,"0");
                    }
                    break;
                case Constant.GET_AUTHOR_PROFILE:
                    //书籍作者简介
                    String authorIsbn = args.getString("isbn");
                    String authorId = args.getString("authorId");
                    String network5 = "";
                    if (args.has("network")){
                        network5 = args.getString("network");
                    }
                    BookDao authorBookDB = GreenDaoHelper.getDaoSession().getBookDao();
                    Book authorBook = authorBookDB.queryBuilder().where(Properties.Isbn.eq(authorIsbn)).unique();
                    UserBookDao authorUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                    UserBook authorUserBook = authorUserBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+authorIsbn)).unique();
                    if(!TextUtils.equals(network5,"online")&&authorBook != null && authorUserBook != null && Constant.BOOK_COMPLETED.equals(authorUserBook.getState())){
//                        LocalBookUtil.getAuthorInfo(authorBook,activity,authorId,handler,sn);
                        LocalBookUtilAnother.getAuthorInfo(authorBook,activity,authorId,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_AUTHOR_PROFILE,"","",authorIsbn,"",authorId,"1");
                    }else {
                        HttpUtils.getBookAuthor(user.getToken(),authorIsbn,authorId,handler,sn);
                        FileUtils.saveLog(activity,Constant.GET_AUTHOR_PROFILE,"","",authorIsbn,"",authorId,"0");
                    }
                    break;
                case Constant.CMD_PLAY_AUDIO:
                    //播放音频
                    sendServiceResult(true,Constant.CMD_PLAY_AUDIO,args.toString(),handler,sn);
                    break;
                case Constant.CMD_PLAY_VIDEO:
                    //播放视频
                    sendServiceResult(true, Constant.CMD_PLAY_VIDEO,args.toString(),handler,sn);
                    break;
                case Constant.PAYMENT:
                    //支付付款 {"command":"payment","args":{"payType":"1","bookid":"56497bdf0cf28762ae7f0f77","amount":-48,"uuid":"1"}}
//                    IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, Constant.AppID);
                    String payBookId = args.getString("bookid");//图书id
                    String payType = args.getString("payType");//支付类型 1:支付宝  2:微信
                    String payAmount = args.getString("amount");//价格
                    String uuid = args.getString("uuid");
                    String discountId = args.getString("discountId");
                    if(TextUtils.equals("2",payType)){
                        //微信支付，判断是否安装微信
                        IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, Constant.AppID);
                        if (!msgApi.isWXAppInstalled()) {
                            Toast.makeText(activity, R.string.pay_wx_client_install,Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!msgApi.isWXAppSupportAPI()) {
                            Toast.makeText(activity, R.string.pay_wx_client_version,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(activity, R.string.pay_call_wx, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity, R.string.pay_call_alipay, Toast.LENGTH_SHORT).show();
                    }
                    showWaitDialog(handler);
                    String payToken = MyApplication.getUser().getToken();
                    String rechargeType = "0";//0:购书,1充值
                    HttpUtils.getOrderInfo(payToken,uuid,payBookId,payType,payAmount,"",rechargeType,discountId,handler,sn);
                    break;
                case Constant.GET_RECHARGE_PACKAGE_LIST:
                    //获取充值界面初始化数据
                    //{"args":{},"command":"recharge","sn":"20170222150745965114"}
                    HttpUtils.recharge(user,handler,sn);
                    FileUtils.saveLog(activity,Constant.GET_RECHARGE_PACKAGE_LIST,"","");
                    break;
                case Constant.RECHARGE_PAY:
                    //充值支付
                    //{"args":{"rechargePackage":{"id":"6","amount":5,"point":50,"giftAmount":"0","giftPoint":"0"},"payType":1},"command":"rechargePay","sn":"20170222164420440062"}
                    //{"command":"payment","args":{"payType":"1","bookid":"56497bdf0cf28762ae7f0f77","amount":-48,"uuid":"1"}}
                    JSONObject rechargePackage = args.getJSONObject("rechargePackage");
                    String rechargeId = rechargePackage.getString("id");
                    String rechargeAmount = rechargePackage.getString("amount");
                    String rechargePayType = args.getString("payType");
                    String rechargType = "1";//0:购书,1充值
//                    IWXAPI rechargMsgApi = WXAPIFactory.createWXAPI(activity, Constant.AppID);
                    if(TextUtils.equals("2",rechargePayType)){
//                        //微信支付，判断是否安装微信
//                        if (!rechargMsgApi.isWXAppInstalled()) {
//                            String rechargeResult = activity.getResources().getString(R.string.pay_wx_client_install);
//                            DataUtil.sendServiceResult(true,true,false,Constant.WX_PAY_RESULT,rechargeResult,null,handler,sn);
//                            return;
//                        } else if (!rechargMsgApi.isWXAppSupportAPI()) {
//                            String rechargeResult = activity.getResources().getString(R.string.pay_wx_client_version);
//                            DataUtil.sendServiceResult(true,true,false,Constant.WX_PAY_RESULT,rechargeResult,null,handler,sn);
//                            return;
//                        }
                        Toast.makeText(activity, R.string.pay_call_wx, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity, R.string.pay_call_alipay, Toast.LENGTH_SHORT).show();
                    }
                    HttpUtils.getOrderInfo(user.getToken(),user.getId(),"",rechargePayType,rechargeAmount,rechargeId,rechargType,"",handler,sn);
                    break;
                case Constant.SET_DOWNLOAD_LOCATION:
                    //设置下载位置
                    int locationId = args.getInt("id");
                    SPUtil.getInstance(activity).save(Constant.BOOK_STORAGE_POSITION,locationId);
                    sendServiceResult(true,true,true,Constant.SET_DOWNLOAD_LOCATION,"",null,handler,sn);
                    break;
                case Constant.GET_DOWNLOAD_LOCATION:
                    //获取用户选择的下载位置
                    DataUtil.getDownloadLocations(activity,handler,sn);
                    break;
                case Constant.GET_CROSS_SEARCH_BOOK_LIST:
                    //跨书搜
                    //{"args":{"keyword":"痢疾"},"command":"crosssearch","sn":"20170220162219957260"}
                    String keyword = args.getString("keyword");
                    HttpUtils.crossSearch(user.getToken(),keyword,handler,sn);
                    FileUtils.saveLog(activity,Constant.GET_CROSS_SEARCH_BOOK_LIST,"","");
                    break;
                case Constant.GET_CROSS_SEARCH_BOOK:
                    //跨书搜详情
                    //{"args":{"bookId":9,"keyword":"gu"},"command":"crosssearchbook","sn":"20170221091849613000"}
                    String keywordBook = args.getString("keyword");
                    String isbnSearch = args.getString("isbn");
                    HttpUtils.crossSearchBook(user.getToken(),keywordBook,isbnSearch,handler,sn);
                    FileUtils.saveLog(activity,Constant.GET_CROSS_SEARCH_BOOK,"","",isbnSearch);
                    break;
                case Constant.GET_BOOK_SEARCH_LIST:
                    //搜索图书列表
                    String bookSearchCondition = args.getString("condition");
                    String bookSearchPageSize = args.getString("pageSize");
                    String bookSearchPage = args.getString("page");
                    HttpUtils.searchBookList(user.getToken(),bookSearchCondition,bookSearchPageSize,bookSearchPage,handler,sn);
                    FileUtils.saveLog(activity,Constant.GET_BOOK_SEARCH_LIST,"","");
                    break;
                case Constant.GET_HOT_WORD_LIST:
                    //获取热门搜索关键字
                    HttpUtils.getHotWordList(user.getToken(),handler,sn);
                    FileUtils.saveLog(activity,Constant.GET_HOT_WORD_LIST,"","");
                    break;
                case Constant.CMD_DELETE_FROM_BOOKSHELF:
                    //删除书架书籍
                //{"args":{"bookId":"56497bdf0cf28762ae7f0f80"},"command":"deleteFromBookShelf","sn":"20170220142936949443"}
                    String deleteIsbn = args.getString("isbn");
                    DataUtil.deleteBookFromShelf(deleteIsbn,activity,handler,sn);
                    FileUtils.saveLog(activity,Constant.CMD_DELETE_FROM_BOOKSHELF,"","",deleteIsbn);
                    break;
                case Constant.GET_PURCHASE_BOOK_LIST:
                    //获取已获得图书
                    //{"args":{"pageSize":10,"page":1,"accountId":1},"command":"obtainedbooks","sn":"20170222101406111572"}
                    String obtainedPageSize = args.getString("pageSize");
                    String obtainedPage = args.getString("page");
                    HttpUtils.obtainedBooks(user,obtainedPage,obtainedPageSize,handler,sn);
                    break;
                case Constant.UPDATE_CUSTOMER_INFO:
                    //{"args":{"key":"portrait","value":"http://mvw-develop.oss-cn-beijing.aliyuncs.com//kevintest/2017/03/02/d1c66a25305d44bb8e80b15f4c29496c.jpg"},"command":"updateCustomerInfo","sn":"20170302163212154545"}
                    String key = args.getString("key");
                    String value = args.getString("value");
                    HttpUtils.updateCustomerInfo(user,key,value,handler,sn);
                    FileUtils.saveLog(activity,Constant.UPDATE_CUSTOMER_INFO,"","");
                    break;
                case Constant.GET_IS_PURCHASED_BOOK:
                    //判断书籍是否购买
                    String isbnStatus = args.getString("isbn");
                    HttpUtils.buyStatus(user,isbnStatus,handler,sn);
                    break;
                case Constant.GET_BOOK_CHAPTER_EXAM_LIST:
                    //书籍自测题  取消了
//{"args":{"isbn":"111117","chapterNo":"13","type":"explanation"},"command":"GetBookChapterExamList","sn":"20170406180227219226"}
//                    String examIsbn = args.getString("isbn");
//                    examIsbn = "111111";
//                    String examChapterNo = args.getString("chapterNo");
//                    String examType = args.getString("type");
//                    HttpUtils.getBookChapterExam(user,examIsbn,examChapterNo,examType,handler,sn);
                    break;
                case Constant.GET_CUSTOMER_INFO:
                    //获取用户个人资料
                    HttpUtils.getCustomerInfo(user,handler,sn);
                    break;
                case Constant.CMD_DIALING:
                    String tel = args.getString("tel");
                    sendServiceResult(true,Constant.CMD_DIALING,tel,handler,sn);
                    break;
                case Constant.CMD_SHARE_APP:
                    //分享
                    ShareDialog shareDialog = new ShareDialog(activity);
                    shareDialog.show();
                    break;
                case Constant.GET_RECHARGE_CONSUMPTION_LIST:
                    //充值消费记录
                    HttpUtils.getRechargeAndConsumptionList(user,handler,sn);
                    break;
                case Constant.GET_COUPON_LIST:
                    //优惠券
                    HttpUtils.getCouponList(user,handler,sn);
                    break;
                case Constant.POST_VERIFICATION_CODE:
                    //激活验证码
                    String code = args.getString("code");
                    HttpUtils.postVerificationCode(user,code,handler,sn);
                    break;
                case Constant.POST_RESET_PASSWORD:
                    //修改密码
                    String password = args.getString("password");
                    String newPassword = "";
                     newPassword = args.getString("newPassword");
                    if(TextUtils.isEmpty(newPassword)){
                        if (args.has("npwd")){
                            newPassword = args.getString("npwd");
                        }
                    }
                    HttpUtils.postResetPassword(user,password,newPassword,handler,sn);
                    break;
                case Constant.GET_BOOK_STATE:
                    //获取书籍状态
                    String isbnBookState = args.getString("isbn");
                    DataUtil.getBookState(activity,isbnBookState);
//                    DataUtil.getBookState(isbnBookState,handler,sn);
                    break;
                case Constant.GET_APP_VERSION:
                    //获取版本信息
                    DataUtil.getAppVersion(activity,handler,sn);
                    break;
                case Constant.CMD_CHECK_UPDATE:
                    //检查版本更新
                    HttpUtils.checkVersion(user,Utils.getAppVersionCode(activity)+"",handler,sn);
                    break;
                case Constant.CMD_PURCHASE_SUCCESS:
                    //书籍购买成功,发送刷新书架数据广播
//                    DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,"");
                    String purchaseBookId = args.getString("id");
                    String bookSet = args.getString("bookSet");
                    if (bookSet.equals("1")){
                        HttpUtils.getBookDetail(user,purchaseBookId,handler,sn,Constant.CMD_PURCHASE_SUCCESS);
                    }else if (bookSet.equals("0")){
                        Result result = new Result();
                        result.setSuccess(true);
                        result.setCommand(Constant.MSG_REFRESH);
                        Message message = new Message();
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                    break;
                case Constant.POST_FEEDBACK:
                    //{"args":{"title":"11","content":"11"},"command":"PostFeedback","sn":"20170503174758109666"}
                    //意见反馈
                    String feedbackTitle = args.getString("title");
                    String feedbackContent = args.getString("content");
                    HttpUtils.postFeedback(user,feedbackTitle,feedbackContent,handler,sn);
                    break;
                case Constant.GET_NETWORK_STATE:
                    //取网络状态 0:未知 & 1:无网络 & 2:4G & 3:WIFI
                    DataUtil.getNetwork(activity,handler,sn);
                    break;
                case Constant.LOGOUT:
                    //退出登录
                    String logoutRes = "";
                    if(args.has("logoutType")){
                        logoutRes = args.getString("logoutType");
                    }
                    sendServiceResult(true,Constant.LOGOUT,logoutRes,handler,sn);
                    break;
                case Constant.CMD_RETURN_HOME_PAGE:
                    //{"args":{"Args":{"code":"0001"}},"command":"ReturnHomePage","sn":"2017060514061789074"}
                    //考试导航返回
//                    activity.finish();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                JSONObject jsonObject = new JSONObject(content);
                String command = jsonObject.getString("command");
                String sn = "";
                if(jsonObject.has("sn")){
                    sn = jsonObject.getString("sn");
                }
                switch (command){
                    case GET_BOOK_CATELOG:
                        //读取本地缓存书籍报错时处理
                        sendServiceResult(true,true,false, GET_BOOK_CATELOG,activity.getString(R.string.book_not_exists),null,handler,sn);
                        break;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }

  /**
   * token过期时给用户显示对话框
   */
    private static void showTokenExpiredDialog(Activity activity, final Handler handler, final String sn){
        Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("提示");
        builder.setMessage("您的账号在其他设备登录，如果这不是您的操作，请及时修改登录密码\n"
            + "\n"
            + "如有疑问，请咨询：400-001-8080");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendServiceResult(true,Constant.LOGOUT,"",handler,sn);
            }
        });
        AlertDialog alertDialog = builder.show();
        //设置对话框的背景为黑色
        Window window = alertDialog.getWindow();
        window.setDimAmount(1.0f);
    }

  /**
   * 显示加载框
   */
  private static void showWaitDialog(Handler handler){
      Result result = new Result();
      result.setSuccess(true);
      result.setCommand(Constant.OPEN_LOADINGNG);
      Message msg = Message.obtain();
      msg.obj= result;
      handler.sendMessage(msg);
  }
    /**
     * 打开pdf
     * @param isbn isbn
     */
    private void openPDF(String isbn){
        BookDao pdfLocalBookDao = GreenDaoHelper.getDaoSession().getBookDao();
        Book pdfBook = pdfLocalBookDao.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
        if(pdfBook != null){
            String pathEncrypt = pdfBook.getDownloadPath()+File.separator+Utils.md5Digest(pdfBook.getPath());
            File file = new File(pathEncrypt);
            if(!file.exists()){
                Toast.makeText(activity,"这本书已经丢失或损坏，请重新安装",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent mIntent = new Intent(activity, MuPDFActivity.class);
            mIntent.putExtra("FileName",pdfBook.getName());
            mIntent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(pathEncrypt);
            mIntent.setData(uri);
            activity.startActivityForResult(mIntent,2000);
            FileUtils.saveLog(activity,Constant.CMD_OPEN_PDF,Constant.ACTION_TYPE_START,"");
        }else {
            Toast.makeText(activity,"书籍不存在或已损坏",Toast.LENGTH_SHORT).show();
        }
    }
}