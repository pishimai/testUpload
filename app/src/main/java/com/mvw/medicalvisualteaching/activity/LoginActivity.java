package com.mvw.medicalvisualteaching.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.UserDao;
import com.mvw.medicalvisualteaching.dialog.LoginDialog;
import com.mvw.medicalvisualteaching.utils.CalculatorUtil;
import com.mvw.medicalvisualteaching.utils.SPUtil;
import com.mvw.medicalvisualteaching.utils.StringUtils;
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
import org.xwalk.core.XWalkView;

/**
 * 登录 Created by a on 2016/11/18.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

  private Activity activity;
  private EditText etPhone;//账号
  private EditText etPassword;//密码
  private LoginDialog loginDialog;//随便看看对话框

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    activity = this;
    initView();
  }

  private void initView() {
    etPhone = (EditText) findViewById(R.id.et_login_phone_email_input);
    etPassword = (EditText) findViewById(R.id.et_login_password_input);

//        etPhone.setText("15011140668");
//        etPassword.setText("123456");

    TextView tvLogin = (TextView) findViewById(R.id.tv_login);
    TextView tvRegister = (TextView) findViewById(R.id.tv_login_register);
    TextView tvFindPassword = (TextView) findViewById(R.id.tv_login_find_password);
    TextView tvTourist = (TextView) findViewById(R.id.tv_login_look_around);
    tvLogin.setOnClickListener(this);
    tvRegister.setOnClickListener(this);
    tvFindPassword.setOnClickListener(this);
    tvTourist.setOnClickListener(this);
    //对话框布局
    View touristDialogLayout = View.inflate(activity, R.layout.dialog_guest_login, null);
    touristDialogLayout.findViewById(R.id.tv_guest_phone_login).setOnClickListener(this);
    touristDialogLayout.findViewById(R.id.tv_guest_login).setOnClickListener(this);
    loginDialog = new LoginDialog(activity);
    if (loginDialog.getWindow() != null) {
      loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
    }
    loginDialog.setContentView(touristDialogLayout);
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_login:
       // openJS();
        //登录
        String strPhone = etPhone.getText().toString();
        String strPassword = etPassword.getText().toString();
        if (StringUtils.isEmpty(strPhone)) {
          Toast.makeText(activity, getString(R.string.username_cannot_null), Toast.LENGTH_SHORT)
              .show();
          return;
        }
        if (StringUtils.isEmpty(strPassword)) {
          Toast.makeText(activity, getString(R.string.password_cannot_null), Toast.LENGTH_SHORT)
              .show();
          return;
        }
        if (strPassword.length() < 6 || strPassword.length() > 18) {
//                    Toast.makeText(activity,"请输入6-18位密码",Toast.LENGTH_SHORT).show();
//                    return;
        }
        String md5Password = Utils.md5Digest(strPassword);
        login(strPhone, md5Password, strPassword, false);
//
//                Logger.i(md5Password);
        break;
      case R.id.tv_login_register:
        //注册
        startActivity(new Intent(activity, RegisterActivity.class));
        break;
      case R.id.tv_login_find_password:
        //找回密码
        startActivity(new Intent(activity, FindPasswordActivity.class));
        break;
      case R.id.tv_login_look_around:
        //随便看看
        loginDialog.show();
        break;
      case R.id.tv_guest_login:
        String token = SPUtil.getInstance(activity).getString(Constant.VISITOR_TOKEN, null);
        if (token == null) {
          //没有进行游客登陆过
          login("", "", "", true);
        } else {
          //进行了游客登录
          User user = new User();
          user.setToken(token);
          user.setGuest("true");
          getUserInfo(token, user.getGuest());
        }
        break;
      case R.id.tv_guest_phone_login:
        loginDialog.dismiss();
        break;
    }
  }

  private void openJS(){
    PackageInfo pak = CalculatorUtil.getAllApps(LoginActivity.this, "Calculator","calculator"); //大小写
    if(pak != null){
      Intent intent = new Intent();
      intent = this.getPackageManager().getLaunchIntentForPackage(pak.packageName);
      startActivity(intent);
    }else{
      Toast.makeText(this, "未找到计算器", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 获取Token
   *
   * @param phone 账号
   * @param password 密码
   * @param guest 是否是游客
   */
  private void login(String phone, String passwordMd5, String password, final boolean guest) {
    //判断版本是否支持，仅支持4.4.4以上版本
    Logger.i("RELEASE =" + VERSION.RELEASE + " CODENAME=" + VERSION.CODENAME + " INCREMENTAL="
        + VERSION.INCREMENTAL);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      Toast.makeText(activity, "该版本仅支持安卓4.4.4以上版本", Toast.LENGTH_SHORT).show();
      return;
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      if (!VERSION.RELEASE.equals("4.4.4")) {
        Toast.makeText(activity, "该版本仅支持安卓4.4.4以上版本", Toast.LENGTH_SHORT).show();
        return;
      }
    }
    showWaitDialog("正在登录");
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, Object> userMap = new HashMap<>();
    serviceMap.put("serviceNumber", "humanLogin");
    serviceMap.put("serviceModule", "imed");
    serviceMap.put("TerminalType", Constant.TerminalType);
    serviceMap.put("DeviceType", Constant.DeviceType);
    if (guest) {
      serviceMap.put("serviceNumber", "humanAnonymousLogin");
      Map<String, String> human = new ArrayMap<>();
      human.put("account", Utils.getDeviceId(activity));//3.0上线以后加一个字段
      human.put("instituteNumber","VO000000002");
      userMap.put("human", human);
    } else {
      userMap.put("login", phone);
      userMap.put("pwd", passwordMd5);
      userMap.put("pwd2", password);
    }
    serviceMap.put("args", userMap);
    Gson gson = new Gson();
    String result = gson.toJson(serviceMap);
    Logger.i(result);
    Map<String, String> params = new HashMap<>();
    params.put(result, "");
    OkHttpUtils.post().url(AppConfig.HOST).params(params).build().execute(new StringCallback() {
      @Override
      public void onError(Call call, Exception e, int id) {
        hideWaitDialog();
        Logger.i(e.getMessage());
        Toast.makeText(activity, getString(R.string.http_exception_error), Toast.LENGTH_LONG)
            .show();
      }

      @Override
      public void onResponse(String response, int id) {
//{"errorMessage":"","opFlag":true,"serviceResult":{"errorMessage":"","result":true,"token":"f6fa02eea2294abb9b9dc06e46d3f696"},"timestamp":""}
        try {
          String loginRes = URLDecoder.decode(response, "utf-8");
          Logger.i(loginRes);
          JSONObject resObject = new JSONObject(loginRes);
          String opFlag = resObject.getString("opFlag");
          if (TextUtils.equals(opFlag, "true")) {
            JSONObject serviceResultObject = new JSONObject(
                resObject.getString("serviceResult"));
            String loginResult = serviceResultObject.getString("result");
            if (TextUtils.equals(loginResult, "true")) {
              String token = serviceResultObject.getString("token");
              User user = new User();
              user.setToken(token);
              user.setGuest(guest + "");
              getUserInfo(token, user.getGuest());
            } else {
              hideWaitDialog();
              String errorMsg = serviceResultObject.getString("errorMessage");
              if (TextUtils.isEmpty(errorMsg)) {
                Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG)
                    .show();
              } else {
                Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
              }
            }
          } else {
            hideWaitDialog();
            String errorMsg = resObject.getString("errorMessage");
            if (TextUtils.isEmpty(errorMsg)) {
              Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
            }
          }
        } catch (JSONException | UnsupportedEncodingException e) {
          e.printStackTrace();
          Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
          hideWaitDialog();
        }
      }
    });
  }

  private void getUserInfo(final String token, final String guest) {
    showWaitDialog();
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, String> argsMap = new HashMap<>();
    serviceMap.put("serviceNumber", "0010000");
    serviceMap.put("serviceModule", "imed");
    serviceMap.put("TerminalType", Constant.TerminalType);
    serviceMap.put("DeviceType", Constant.DeviceType);
    serviceMap.put("token", token);
    argsMap.put("token", token);
    serviceMap.put("args", argsMap);
    final Gson gson = new Gson();
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
            Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
          }

          @Override
          public void onResponse(String response, int id) {
            try {
              String loginRes = URLDecoder.decode(response, "utf-8");
              Logger.i(loginRes);
              JSONObject resObject = new JSONObject(loginRes);
              String opFlag = resObject.getString("opFlag");
              if (TextUtils.equals(opFlag, "true")) {
                JSONObject serviceResultObject = new JSONObject(
                    resObject.getString("serviceResult"));
                JSONObject loginResult = serviceResultObject.getJSONObject("result");
                boolean resultFlag = serviceResultObject.getBoolean("flag");
                if (resultFlag) {
                  User user = gson.fromJson(loginResult.toString(), User.class);
                  if (user == null) {
                    user = new User();
                  }
                  XWalkView xWalkView = new XWalkView(activity);
                  user.setToken(token);
                  user.setGuest(guest);
                  validateToken(user);
                } else {
                  hideWaitDialog();
                  String errorMsg = serviceResultObject.getString("errorMessage");
                  if (TextUtils.isEmpty(errorMsg)) {
                    Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG)
                        .show();
                  } else {
                    Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                  }
                }
              } else {
                hideWaitDialog();
                String errorMsg = resObject.getString("errorMessage");
                if (TextUtils.isEmpty(errorMsg)) {
                  Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG)
                      .show();
                } else {
                  Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                }
              }
            } catch (JSONException | UnsupportedEncodingException e) {
              e.printStackTrace();
              Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
              hideWaitDialog();
            }
          }
        });

  }

  /**
   * token验证
   */
  private void validateToken(final User user) {
        /*
        {
        serviceModule:"BS-Service", serviceNumber: 'validateToken', token: BOOK.token, args: {
            token: BOOK.token
        }
    }
         */
    Map<String, Object> serviceMap = new HashMap<>();
    Map<String, String> argsMap = new HashMap<>();
    serviceMap.put("serviceNumber", "queryLoginUserInfo");
    serviceMap.put("serviceModule", "BS-Service");
    serviceMap.put("TerminalType", Constant.TerminalType);
    serviceMap.put("DeviceType", Constant.DeviceType);
    serviceMap.put("token", user.getToken());
    argsMap.put("token", user.getToken());
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
            Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
          }

          @Override
          public void onResponse(String response, int id) {
            try {
              String validateTokenRes = URLDecoder.decode(response, "utf-8");
              Logger.i(validateTokenRes);
              JSONObject resObject = new JSONObject(validateTokenRes);
              String validateOpFlag = resObject.getString("opFlag");
              if (TextUtils.equals(validateOpFlag, "true")) {
                JSONObject serviceResultObject = new JSONObject(
                    resObject.getString("serviceResult"));
                String validateResult = serviceResultObject.getString("result");
                if (TextUtils.equals(validateResult, "true")) {
                  //token验证成功后再存储用户信息
                  UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                  userDao.insertOrReplace(user);
                  MyApplication.setUser(user);
                  MyApplication.getUser();
                  if ("true".equals(user.getGuest())) {
                    //是游客登录，存储游客token
                    SPUtil.getInstance(activity).save(Constant.VISITOR_TOKEN, user.getToken());
                  }
                  //跳转主界面
                  hideWaitDialog();
                  Intent intent = new Intent(activity, MainActivity.class);
                  intent.putExtra("user", user);
                  startActivity(intent);
                  finish();
                  return;
                } else {
                  String errorMsg = serviceResultObject.getString("errorMessage");
                  if (TextUtils.isEmpty(errorMsg)) {
                    Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG)
                        .show();
                  } else {
                    Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                  }

                }
              } else {
                String errorMsg = resObject.getString("errorMessage");
                if (TextUtils.isEmpty(errorMsg)) {
                  Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG)
                      .show();
                } else {
                  Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                }
              }
            } catch (JSONException | UnsupportedEncodingException e) {
              e.printStackTrace();
              Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
            hideWaitDialog();
          }
        });
  }

}
