package com.mvw.medicalvisualteaching.utils;

import android.content.Context;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a on 2016/11/21.
 */
public class StringUtils {

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                return false;
            }
        }
        return true;
    }

    public static boolean isPassword(String password) {
        String str = "^[a-zA-Z0-9-_]{6,16}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }

  /**
   * 判断字符串是否是数字
   */
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static String createUrl(String url,Map<String,String> params,Context context){
        if(params == null){
            params = new HashMap<>();
        }
        /*
        clienttype: 代表APP类型  2:androidPhone  4:androidPad
        clientversion :APP版本号
        clientid:设备号
         */
        params.put("clienttype", Constant.ClientType);
        params.put("clientversion",Utils.getAppVersionName(context));
        params.put("clientid",Utils.getDeviceId(context));
        if(!url.startsWith("http") && !url.startsWith("file")){
            //不是http开头,加载本地的html
            if("".equals(AppConfig.LOCAL_URL)){
                url = AppConfig.ONLINE_URL+url;
            }else {
                url = AppConfig.LOCAL_URL+url;
            }
        }
        String[] urlArr = url.split("\\?");
        Map<String,String> urlParams = new HashMap<>();
        if(urlArr.length==2){
            String[] paramsArr = urlArr[1].split("&");
            for(String str : paramsArr){
                if(str.contains("=")){
                    String[] strArr = str.split("=");
                    if(strArr.length == 1){
                        urlParams.put(strArr[0],"");
                    }else if(strArr.length == 2){
                        urlParams.put(strArr[0],strArr[1]);
                    }
                }
            }
            for(Entry<String,String> entry : params.entrySet()){
                urlParams.put(entry.getKey(),entry.getValue());
            }
            String args = "";
            for(Entry<String,String> entry : urlParams.entrySet()){
                args += entry.getKey()+"="+entry.getValue()+"&";
            }
            url = urlArr[0] + "?"+args;
        }else {
            String args = "";
            for(Entry<String,String> entry : params.entrySet()){
                args += entry.getKey()+"="+entry.getValue()+"&";
            }
            if(url.contains("?")){
                url = url + "&"+ args;
            }else {
                url = url + "?"+args;
            }
        }
        url = url.substring(0,url.length()-1);
        return url;
    }

    public static String getUrlFileName(String url){
        if(url != null && !"".equals(url)){
            String[] list = url.split("/");
            if(list.length > 1){
                String file = list[list.length-1];
                return file;
            }
        }
        return null;
    }
}
