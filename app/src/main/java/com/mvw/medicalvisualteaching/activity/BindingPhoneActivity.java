package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.UserDao;
import com.mvw.medicalvisualteaching.utils.HttpUtils;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.mvw.netlibrary.OkHttpUtils;
import com.mvw.netlibrary.callback.StringCallback;
import com.orhanobut.logger.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.MediaType;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 设置密码界面
 */
public class BindingPhoneActivity extends BaseActivity implements OnClickListener {

  private BindingPhoneActivity activity;
  private EditText etPhone;//手机号
  private EditText etVerificationCode;//验证码
  private Button btnVerificationCode;
  private Button btn_quit;
  private int ss = 60;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_binding_phone);
    activity = this;
    initView();
  }

  private void initView() {
    etPhone = (EditText) findViewById(R.id.et_binding_phone_input);
    etVerificationCode = (EditText) findViewById(R.id.et_binding_verification_input);
    Button btnBinding = (Button) findViewById(R.id.btn_binding);
    btnBinding.setOnClickListener(this);
    btnVerificationCode = (Button) findViewById(R.id.btn_binding_get_verification);
    btnVerificationCode.setOnClickListener(this);
    btn_quit = (Button) findViewById(R.id.btn_quit);
    btn_quit.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_binding:
        //绑定手机
        String phone = etPhone.getText().toString();
        String verificationCode = etVerificationCode.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
          Toast.makeText(activity, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(verificationCode)) {
          Toast.makeText(activity, "请输入验证码", Toast.LENGTH_SHORT).show();
          return;
        }
        bindPhone(phone,verificationCode);
        break;
      case R.id.btn_binding_get_verification:
        //获取验证码
        String registerPhone = etPhone.getText().toString();
        if (TextUtils.isEmpty(registerPhone) || registerPhone.length() != 11) {
          Toast.makeText(activity, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
          return;
        }
        ss = 60;
        handler.sendEmptyMessageDelayed(1000, 1000);
        //获取验证码接口
        getVerificationCode(registerPhone);
        break;
      case R.id.btn_quit://暂不绑定
        hideWaitDialog();
        HttpUtils.burnHumanToken(MyApplication.getUser(),null,"");
        MyApplication.setUser(null);
        UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
        userDao.deleteAll();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        break;
    }
  }

  /**
   * 绑定手机
   */
  private void bindPhone(String phone, String verificationCode) {
    showWaitDialog();
    final User user = MyApplication.getUser();
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, String> argsMap = new HashMap<>();
    serviceMap.put("serviceNumber", "0017000");
    serviceMap.put("serviceModule", "imed");
    serviceMap.put("token",user.getToken() );
    argsMap.put("token",user.getToken() );
    argsMap.put("phone", phone);
    argsMap.put("code", verificationCode);
    serviceMap.put("args", argsMap);
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
            hideWaitDialog();
            Toast.makeText(activity, getString(R.string.http_exception_error), Toast.LENGTH_SHORT)
                .show();
          }
          @Override
          public void onResponse(String response, int id) {
            hideWaitDialog();
            try {
              String validateTokenRes = URLDecoder.decode(response, "utf-8");
              Logger.i(validateTokenRes);
              JSONObject resObject = new JSONObject(validateTokenRes);
              String validateOpFlag = resObject.getString("opFlag");
              if (TextUtils.equals(validateOpFlag, "true")) {
                JSONObject serviceResultObject = new JSONObject(
                    resObject.getString("serviceResult"));
                String validateResult = serviceResultObject.getString("flag");
                String resultStr = serviceResultObject.getString("result");
                if (TextUtils.equals(validateResult, "true")) {
                  //注册成功
                  Toast.makeText(activity,"绑定成功",Toast.LENGTH_SHORT).show();
                  User user = MyApplication.getUser();
                  user.setCellphone(etPhone.getText().toString());
                  UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                  userDao.insertOrReplace(user);
                  finish();
                } else {
                  if (TextUtils.isEmpty(resultStr) || "null".equals(resultStr)) {
                    Toast
                        .makeText(activity, getString(R.string.bind_phone_failed), Toast.LENGTH_SHORT)
                        .show();
                  } else {
                    Toast.makeText(activity, Utils.getErrorMsg(resultStr,1), Toast.LENGTH_SHORT).show();
                  }
                }
              } else {
                String errorMsg = resObject.getString("errorMessage");
                if (TextUtils.isEmpty(errorMsg)) {
                  Toast.makeText(activity, getString(R.string.bind_phone_failed), Toast.LENGTH_SHORT)
                      .show();
                } else {
                  Toast.makeText(activity, Utils.getErrorMsg(errorMsg,1), Toast.LENGTH_SHORT).show();
                }
              }
            } catch (JSONException | UnsupportedEncodingException e) {
              e.printStackTrace();
              Toast.makeText(activity, getString(R.string.bind_phone_failed), Toast.LENGTH_SHORT)
                  .show();
            }
          }
        });
  }

  /**
   * 获取验证码
   *
   * @param phone 手机号
   */
  private void getVerificationCode(String phone) {
    showWaitDialog();
    User user = MyApplication.getUser();
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, String> argsMap = new HashMap<>();
    serviceMap.put("serviceNumber", "0016000");
    serviceMap.put("serviceModule", "imed");
    serviceMap.put("token", user.getToken());
    argsMap.put("token", user.getToken());
    argsMap.put("phone", phone);
    serviceMap.put("args", argsMap);
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
            hideWaitDialog();
            Toast.makeText(activity, getString(R.string.http_exception_error), Toast.LENGTH_SHORT)
                .show();
          }

          @Override
          public void onResponse(String response, int id) {
            hideWaitDialog();
            try {
              String validateTokenRes = URLDecoder.decode(response, "utf-8");
              Logger.i(validateTokenRes);
              JSONObject resObject = new JSONObject(validateTokenRes);
              String validateOpFlag = resObject.getString("opFlag");
              if (TextUtils.equals(validateOpFlag, "true")) {
                JSONObject serviceResultObject = new JSONObject(
                    resObject.getString("serviceResult"));
                String validateResult = serviceResultObject.getString("flag");
                String result = serviceResultObject.getString("result");
                if (TextUtils.equals(validateResult, "true")) {
                  //发送验证码成功
                  Toast.makeText(activity,result,Toast.LENGTH_SHORT).show();

                } else {
                  if (TextUtils.isEmpty(result)) {
                    Toast.makeText(activity,
                        getString(R.string.register_get_verification_code_failed),
                        Toast.LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                  }

                }
              } else {
                String errorMsg = resObject.getString("errorMessage");
                if (TextUtils.isEmpty(errorMsg)) {
                  Toast
                      .makeText(activity, getString(R.string.register_get_verification_code_failed),
                          Toast.LENGTH_SHORT).show();
                } else {
                  Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
                }

              }
            } catch (JSONException | UnsupportedEncodingException e) {
              e.printStackTrace();
              Toast.makeText(activity, getString(R.string.register_get_verification_code_failed),
                  Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 1000:
          if (ss <= 0) {
            handler.removeMessages(1000);
            btnVerificationCode.setEnabled(true);
            btnVerificationCode.setBackgroundResource(R.color.text_orange);
            btnVerificationCode.setText("获取验证码");
          } else {
            handler.sendEmptyMessageDelayed(1000, 1000);
            btnVerificationCode.setEnabled(false);
            btnVerificationCode.setBackgroundResource(R.color.register_verification_code);
            btnVerificationCode.setText("重新获取" + "\n" + "（" + ss + "s）");
            ss--;
          }
          break;
      }
    }
  };

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (handler != null) {
      handler.removeMessages(1000);
      handler = null;
    }
  }

}
