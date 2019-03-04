package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
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
 * 找回密码界面
 */
public class FindPasswordActivity extends BaseActivity implements OnClickListener {

  private FindPasswordActivity activity;
  private EditText etPhone;//手机号
  private EditText etVerificationCode;//验证码
  private EditText etPassword;//密码
  private Button btnVerificationCode;//获取验证码
  private int ss = 60;
  private TimeCount time;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_password);
    activity = this;
    initView();
    time = new TimeCount(60000,1000);
  }

  private void initView() {
    etPhone = (EditText) findViewById(R.id.et_find_password_phone_input);
    etVerificationCode = (EditText) findViewById(R.id.et_find_password_verification_input);
    etPassword = (EditText) findViewById(R.id.et_reset_password_input);
    btnVerificationCode = (Button) findViewById(R.id.btn_find_password_get_verification);
    btnVerificationCode.setOnClickListener(this);
    Button btnRegister = (Button) findViewById(R.id.btn_next);
    btnRegister.setOnClickListener(this);
    ImageButton imageButton = (ImageButton) findViewById(R.id.ib_back);
    imageButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_next:
        //下一步
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
        if (verificationCode.length() != 6) {
          Toast.makeText(activity, "请输入6位验证码", Toast.LENGTH_SHORT).show();
          return;
        }
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(password)){
          Toast.makeText(activity,"请输入密码",Toast.LENGTH_SHORT).show();
          return;
        }
        if(password.length() < 6 || password.length() > 18){
          Toast.makeText(activity,"密码需为6-18位英文、数字",Toast.LENGTH_SHORT).show();
          return;
        }
        password = Utils.md5Digest(password);
        resetPassword(phone,verificationCode,password);
//        Intent intent = new Intent(activity,ResetPasswordActivity.class);
//        intent.putExtra("phone",phone);
//        intent.putExtra("code",verificationCode);
//        startActivity(intent);
        break;
      case R.id.btn_find_password_get_verification:
        //获取验证码
        String registerPhone = etPhone.getText().toString();
        if (TextUtils.isEmpty(registerPhone) || registerPhone.length() != 11) {
          Toast.makeText(activity, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
          return;
        }
//        ss = 60;
//        handler.sendEmptyMessageDelayed(1000, 1000);
        //获取验证码接口
        time.start();
        getVerificationCode(registerPhone);
        break;
      case R.id.tv_register_agreement:
        //隐私协议
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.PRIVACY_POLICY)));
        break;
      case R.id.tv_register_login:
      case R.id.ib_back:
        finish();
        break;
    }
  }

  /**
   * 获取验证码
   *
   * @param phone 手机号
   */
  private void getVerificationCode(String phone) {
    showWaitDialog();
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, String> argsMap = new HashMap<>();
    serviceMap.put("serviceNumber", "0016000");
    serviceMap.put("serviceModule", "imed");
    serviceMap.put("TerminalType", Constant.TerminalType);
    serviceMap.put("DeviceType", Constant.DeviceType);
    serviceMap.put("token", "");
    argsMap.put("phone", phone);
    argsMap.put("token", "-1");
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

  class TimeCount extends CountDownTimer {
    public TimeCount(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {// 计时完毕
      btnVerificationCode.setText("获取验证码");
      btnVerificationCode.setBackgroundResource(R.color.text_orange);
      btnVerificationCode.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
      btnVerificationCode.setEnabled(false);//防止重复点击
      btnVerificationCode.setBackgroundResource(R.color.register_verification_code);
      //   btnVerificationCode.setText(millisUntilFinished / 1000 + s);
      btnVerificationCode.setText("重新获取" + "\n" + "（" + millisUntilFinished / 1000 + "s）");
    }
  }

  /**
   * 设置密码
   *
   * @param password 密码
   */
  private void resetPassword(String phone,String code, String password) {
    showWaitDialog();
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, String> argsMap = new HashMap<>();
    serviceMap.put("serviceNumber", "0018000");
    serviceMap.put("serviceModule", "imed");
    serviceMap.put("TerminalType", Constant.TerminalType);
    serviceMap.put("DeviceType", Constant.DeviceType);
    serviceMap.put("token", "-1");
    argsMap.put("phone", phone);
    argsMap.put("token", "-1");
    argsMap.put("code", code);
    argsMap.put("pwd", password);
    serviceMap.put("args", argsMap);
    Gson gson = new Gson();
    final String result = gson.toJson(serviceMap);
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
//{"serviceResult":{"result":"手机号已重置","flag":true},"errorMessage":"","opFlag":true,"timestamp":"2017-04-27 11:49:33:014"}
            try {
              String validateTokenRes = URLDecoder.decode(response, "utf-8");
              Logger.i(validateTokenRes);
              JSONObject resObject = new JSONObject(validateTokenRes);
              String validateOpFlag = resObject.getString("opFlag");
              if (TextUtils.equals(validateOpFlag, "true")) {
                JSONObject serviceResultObject = new JSONObject(
                    resObject.getString("serviceResult"));
                String validateResult = serviceResultObject.getString("result");
                String flag = serviceResultObject.getString("flag");
                if (TextUtils.equals(flag, "true")) {
                  //设置密码成功
                  if (TextUtils.isEmpty(validateResult)) {
                    Toast.makeText(activity,"找回密码成功",Toast.LENGTH_SHORT).show();
                  }else {
                    Toast.makeText(activity,validateResult,Toast.LENGTH_SHORT).show();
                  }
                  startActivity(new Intent(activity,LoginActivity.class));
                  finish();
                } else {
                  if (TextUtils.isEmpty(validateResult) || "null".equals(validateResult)) {
                    Toast.makeText(activity,
                        getString(R.string.reset_password_failed),
                        Toast.LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(activity, validateResult, Toast.LENGTH_SHORT).show();
                  }
                }
              } else {
                String errorMsg = resObject.getString("errorMessage");
                if (TextUtils.isEmpty(errorMsg) || "null".equals(errorMsg)) {
                  Toast
                      .makeText(activity, getString(R.string.reset_password_failed),
                          Toast.LENGTH_SHORT).show();
                } else {
                  Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
                }
              }
            } catch (JSONException | UnsupportedEncodingException e) {
              e.printStackTrace();
              Toast.makeText(activity, getString(R.string.reset_password_failed),
                  Toast.LENGTH_SHORT).show();
            }
          }
        });
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
