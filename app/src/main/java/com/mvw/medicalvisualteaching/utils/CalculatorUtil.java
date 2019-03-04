package com.mvw.medicalvisualteaching.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.List;

/**
 * Created by zhaomengen on 2017/10/25.
 */

public class CalculatorUtil {

  public static PackageInfo getAllApps(Context context,String app_flag_1,String app_flag_2) {
    PackageManager pManager = context.getPackageManager();
    // 获取手机内所有应用
    List<PackageInfo> packlist = pManager.getInstalledPackages(0);
    for (int i = 0; i < packlist.size(); i++) {
      PackageInfo pak = (PackageInfo) packlist.get(i);
      if(pak.packageName.contains(app_flag_1)||pak.packageName.contains(app_flag_2)){
        return pak;
      }

    }
    return null;
  }

}
