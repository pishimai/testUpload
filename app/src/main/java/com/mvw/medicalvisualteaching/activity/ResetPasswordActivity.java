package com.mvw.medicalvisualteaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
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
 * 设置密码界面
 */
public class ResetPasswordActivity extends BaseActivity implements OnClickListener {

  private ResetPasswordActivity activity;
  private EditText etPassword;//密码
  private String phone;
  private String code;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);
    activity = this;
    initView();
  }

  private void initView() {
    etPassword = (EditText) findViewById(R.id.et_reset_password_input);
    TextView tvCommit = (TextView) findViewById(R.id.tv_reset_password_commit);
    tvCommit.setOnClickListener(this);
    findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    phone = getIntent().getStringExtra("phone");
    code = getIntent().getStringExtra("code");
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_reset_password_commit:
        //设置密码
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
        //设置密码
        resetPassword(password);
        break;
      case R.id.ib_back:
        finish();
        break;
    }
  }

  /**
   * 设置密码
   *
   * @param password 密码
   */
  private void resetPassword(String password) {
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
                    Toast.makeText(activity,"设置密码成功",Toast.LENGTH_SHORT).show();
                  }else {
                    Toast.makeText(activity,validateResult,Toast.LENGTH_SHORT).show();
                  }
                  startActivity(new Intent(activity,LoginActivity.class));
                  finish();
                } else {
                  if (TextUtils.isEmpty(validateResult)) {
                    Toast.makeText(activity,
                        getString(R.string.reset_password_failed),
                        Toast.LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(activity, getString(R.string.reset_password_failed), Toast.LENGTH_SHORT).show();
                  }
                }
              } else {
                String errorMsg = resObject.getString("errorMessage");
                if (TextUtils.isEmpty(errorMsg)) {
                  Toast
                      .makeText(activity, getString(R.string.reset_password_failed),
                          Toast.LENGTH_SHORT).show();
                } else {
                  Toast.makeText(activity, getString(R.string.reset_password_failed), Toast.LENGTH_SHORT).show();
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
}
