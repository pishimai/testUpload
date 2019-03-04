package com.mvw.medicalvisualteaching.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工具类
 * Created by a on 2016/11/21.
 */
public class Utils {

    /** 获取设备ID */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, mac, imei;
        tmDevice = tm.getDeviceId();
        tmSerial = tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        mac = getLocalMacAddress(context);
        imei = getIMEI(context);
        String coad = "";

        if(tmDevice != null && !"".equals(tmDevice)){
            coad = coad + tmDevice;
        }
        if(tmSerial != null && !"".equals(tmSerial)){
            coad = coad +"_"+ tmSerial;
        }
        if (mac != null && !"".equals(mac)) {
            coad = coad + "_"+mac;
        }
        if (imei != null && !"".equals(imei)) {
            coad = coad + "_"+imei;
        }
        return coad;
    }
    /** 获取mac地址 */
    public static String getLocalMacAddress(Context activity) {
        Context context = activity.getApplicationContext();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /** 获取IMEI */
    private static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /** 返回当前程序版本名 */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Logger.i("VersionInfo::" + "Exception_" + e);
        }
        return versionName;
    }
    /** 返回当前程序版本号 */
    public static int getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
            if (versioncode == 0 || versioncode < 0) {
                return 0;
            }
        } catch (Exception e) {
            Logger.i("VersionInfo::" + "Exception_" + e);
        }
        return versioncode;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param context
     * @param className
     *            判断的服务名字：包名+类名
     * @return true 在运行, false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /** 得到内置或者外置SD卡的可用空间的大小
     *
     * @param context
     *            上下文环境
     * @param code
     *            内置外置SD卡区别码 1：内置SD卡；2：外置SD卡；
     * @return 返回的空间大小大小 -1代表不可用 */
    public static long getSDCardAvailableSize(Context context, int code) {

        String sdCardPath = getSDCardPath(context, code);
        if (sdCardPath != null) {
            return getPathAvailableSize(new File(sdCardPath));
        }

        return -1;
    }

    /** 得到手机内置和外置SD卡路径SD卡的路径
     *
     * @param context
     *            上下文环境
     * @param code
     *            内置外置SD卡区别码 1：内置SD卡；2：外置SD卡；
     * @return 返回的路径 null代表路径不可用 */
    public static String getSDCardPath(Context context, int code) {

        if (!isExternalStorageWritable()) {
            return null;
        }

        // SD卡存储的路径信息的数组
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);

        // 得到内置SD卡路径
        if (code == 1) {
            // 得到了内置SD卡路径
            if (externalFilesDirs.length >= 1 && externalFilesDirs[0] != null) {
                return externalFilesDirs[0].getPath();
            }

        }
        // 得到外置SD卡路径
        else if (code == 2) {
            // 数组中包含非空的第二项
            if (externalFilesDirs.length >= 2 && externalFilesDirs[1] != null) {
                return externalFilesDirs[1].getPath();
            }
            // 数组中没有非空第二项，这时候需要调用另外的方法来读取SD卡的路径
            else {
                ArrayList<String> list = getExtSDCardPath();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    if (file.exists()) {
                        long size = getPathTotalSize(file);
                        if (size != 0) {
                            // 在此路径下创建一个新的电子书包的文件夹,除了根目录不同外，内置和外置SD卡路径相同，方便在其他手机上使用下载过书籍的SD卡
                            String insidePath = externalFilesDirs[0].getPath();
                            String isdideSubPath = insidePath.substring(insidePath.indexOf("/Android"));
                            File mvwFile = new File(file.getPath() + isdideSubPath);
                            if (!mvwFile.exists()) {
                                mvwFile.mkdirs();
                            }
                            return mvwFile.getPath();
                        }
                    }
                }
            }
        }
        return null;
    }

    /** 获取外置SD卡路径（适合安卓4.3及以下版本）
     *
     * @return 应该就一条记录或空 */
    public static ArrayList<String> getExtSDCardPath() {
        ArrayList<String> arrayList = new ArrayList<>();
        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        arrayList.add(path);
                    }
                }
            }
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /** 得到给定路径可用空间大小 */
    public static long getPathAvailableSize(File file) {
        StatFs statFs = new StatFs(file.getPath());
        long blockSize;
        long availableBlocks;
        // 判断手机Android版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableBlocks = statFs.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /** 计算并返回给定路径的空间总大小信息 */
    @SuppressLint("NewApi")
    public static long getPathTotalSize(File file) {
        StatFs statFs = new StatFs(file.getPath());
        long blockSize;
        long totalBlocks;
        // 判断手机Android版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            totalBlocks = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            totalBlocks = statFs.getBlockCount();
        }
        return totalBlocks * blockSize;
    }
    /** 判断SD卡存储是否可写 */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取网络状态，判断是否是wifi网络
     *
     * @param context 上下文
     */

    public static boolean isWIFI(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return true;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断当前网络是否是4G网络
     *
     * @param
     * @return boolean
     */
    public boolean is4GAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
            && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            /** Current network is LTE */
            if (networkType == 13) {
                /**此时的网络是4G的*/
                return true;
            }
        }
        return false;
    }

  /**
   * md5加密
   * @param pwd pwd
   * @return md5
   */
    public static String md5Digest(String pwd){
        try
        {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(pwd.getBytes());
            byte[] digest = alg.digest();
            return byte2hex(digest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toLowerCase();
    }

    /**
     * 16位md5加密
     * @param pwd pwd
     * @return md5
     */
    public static String md5Digest16(String pwd){
        try
        {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(pwd.getBytes());
            byte[] digest = alg.digest();
            return byte2hex16(digest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }
    private static String byte2hex16(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toLowerCase().substring(8,24);
    }

  /**
   * date格式化
   * @param dateString  时间字符串
   * @param pattern 时间格式  yyyy-MM-dd
   * @return long
   */
    static long stringToDate(String dateString, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().getTime();
    }

    /** 得到当前格式化的时间  */
    public static String getFormatTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(System.currentTimeMillis()));
    }

    /** 得到当前格式化的时间  */
    public static String getFormatTime(String pattern,long time) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    /**
     * 将一段毫秒数格式化为时间格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        long hour = time / (60 * 60 * 1000);
        long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        if (second >= 60) {
            second = second % 60;
            minute += second / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }
        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (second < 10) {
            ss = "0" + String.valueOf(second);
        } else {
            ss = String.valueOf(second);
        }
        return sh + ":" + sm + ":" + ss;
    }

    /** 改变TextView部分文本的 颜色
     *
     * @param textView
     *            要操作的View
     * @param KeyWord
     *            要改变颜色的关键字
     * @param keyWordColor
     *            关键字的颜色 */
    public static void changeKeyWordsColor(TextView textView, String KeyWord, int keyWordColor) {

        String text = textView.getText().toString();

        // 得到关键字在文本字符串中的位置
        int index = text.indexOf(KeyWord);

        // 获取文本内容
        SpannableString spanString = new SpannableString(text);

        // 再构造一个改变字体颜色的Span
        ForegroundColorSpan span = new ForegroundColorSpan(keyWordColor);

        // 将这个Span应用于指定范围的字体
        spanString.setSpan(span, index, index + KeyWord.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        // 设置给EditText显示出来
        textView.setText(spanString);

    }

  /**
   * 获取进程
   */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
    /**根据isbn获取书的video 地址*/
    public static String getBookVideoPath(String isbn){
        String path = "imed_";
        String code = "";
        switch (isbn){
            case "978-7-900574-27-5": //内科学
                code = "01";
                break;
            case "978-7-900574-89-3"://外科学
                code = "02";
                break;
            case "978-7-900574-79-4": //妇产科学
                code = "03";
                break;
            case "978-7-900574-47-3"://儿科学
                code = "04";
                break;
            case "978-7-900574-83-1": //神经病科学
                code = "05";
                break;
            case "978-7-900574-19-0"://医学影像学
                code = "06";
                break;
            case "978-7-900574-56-5": //临床技能学
                code = "07";
                break;
            case "978-7-900574-22-0"://口腔科学
                code = "08";
                break;
            case "978-7-900574-18-3": //诊断学
                code = "09";
                break;
            case "978-7-900574-82-4"://急诊与灾难医学
                code = "10";
                break;
            case "978-7-900574-35-0": //感染病学
                code = "11";
                break;
            case "978-7-900574-29-9"://皮肤性病学
                code = "12";
                break;
            case "978-7-900574-84-8": //精神病学
                code = "13";
                break;
            case "978-7-900574-54-1"://核医学
                code = "14";
                break;
            case "978-7-900574-34-3"://眼科学
                code = "15";
                break;
            case "978-7-900574-69-5"://耳鼻咽喉头颈科学
                code = "16";
                break;
            case "978-7-900574-65-7"://中医学
                code = "17";
                break;
            case "978-7-900574-73-2"://临床药理学
                code = "18";
                break;
            case "978-7-900574-62-6"://麻醉学
                code = "19";
                break;
            case "978-7-900574-68-8"://康复医学
                code = "20";
                break;
            case "978-7-900574-67-1"://全科医学概论
                code = "21";
                break;
            case "978-7-900574-63-3"://卫生学
                code = "22";
                break;
            case "978-7-900574-72-5"://预防医学
                code = "23";
                break;
            case "978-7-900574-77-0"://临床流行病学
                code = "24";
                break;
            case "978-7-900574-59-6"://老年医学
                code = "25";
                break;
            case "978-7-900574-76-3"://法医学
                code = "26";
                break;
            case "978-7-900574-46-6"://系统解剖学
                code = "27";
                break;
            case "978-7-900574-15-2"://生理学
                code = "28";
                break;
            case "978-7-900574-32-9"://药理学
                code = "29";
                break;
            case "978-7-900574-42-8"://生物化学与分子生物学
                code = "30";
                break;
            case "978-7-900574-57-2"://病理学
                code = "31";
                break;
            case "978-7-900574-45-9"://组织学与胚胎学
                code = "32";
                break;
            case "978-7-900574-13-8"://局部解剖学
                code = "33";
                break;
            case "978-7-900574-39-8"://医学免疫学
                code = "34";
                break;
            case "978-7-900574-25-1"://人体寄生虫学
                code = "35";
                break;
            case "978-7-900574-53-4"://医学微生物学
                code = "36";
                break;
            case "978-7-900574-17-6"://病理生理学
                code = "37";
                break;
            case "978-7-900574-75-6"://医用高等数学
                code = "38";
                break;
            case "978-7-900574-14-5"://医学细胞生物学
                code = "39";
                break;
            case "978-7-900574-74-9"://医学统计学
                code = "40";
                break;
            case "978-7-900574-33-6"://医学基础化学
                code = "41";
                break;
            case "978-7-900574-41-1"://医学有机化学
                code = "42";
                break;
            case "978-7-900574-24-4"://医学伦理学
                code = "43";
                break;
            case "978-7-900574-64-0"://医学心里学
                code = "44";
                break;
            case "978-7-900574-60-2"://流行病学
                code = "45";
                break;
            case "978-7-900574-31-2"://医学遗传学
                code = "46";
                break;
            case "978-7-900574-66-4"://医学物理学
                code = "47";
                break;
            case "978-7-900574-71-8"://医学导论
                code = "48";
                break;
            case "978-7-900574-61-9"://医学文献检索
                code = "49";
                break;
            case "978-7-900574-70-1"://卫生法
                code = "50";
                break;
            case "978-7-900574-58-9"://医学信息与医用计算机基础
                code = "51";
                break;
            case "978-7-900574-92-3":// 医学英语词汇
                code = "52";
                break;
            case "978-7-900574-91-6"://医学英语阅读
                code = "53";
                break;
            case "978-7-900574-99-2":// 医学英语视听说
                code = "54";
                break;

        }
        return md5Digest16(path+code);
    }
    /**获取服务器返回的错误信息*/
    public static String getErrorMsg(String msg,int code){
        String error = "";
        if (msg.contains("E091")){
            error = "用户账户已存在！";
        }else if (msg.contains("E092")){
            if (code ==0){//注册
                error = "该手机号已注册，请更换手机号";
            }else if (code ==1){ //绑定手机
                error = "该手机号已绑定，请更换手机号";
            }
        }else if (msg.contains("E093")){
            error = "用户Email已存在！";
        }else if (msg.contains("E094")){
            error = "用户证件号已存在！";
        }else{
            error=msg;
        }
        return error;
    }
}
