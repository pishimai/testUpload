package com.mvw.medicalvisualteaching.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.Log;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.netlibrary.OkHttpUtils;
import com.mvw.netlibrary.callback.StringCallback;
import com.orhanobut.logger.Logger;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.Call;
import okhttp3.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by on 2016/12/5.
 */
public class FileUtils {

  private static final int REQ_WIDTH = 480 ;
  private static final int REQ_HEIGHT = 840 ;

  /**
   * 获取文件大小
   */
  public static long getFileLength(File file) {
    long size = 0;
    if (file != null && file.exists()) {
      size = file.length();
    }
    return size;
  }

  /**
   * 拷贝assets目录下的文件
   */
  public static void copyAssets(Context context, String filename, String targetPath)
      throws IOException {
    File targetFile = new File(targetPath);
    AssetManager am = context.getAssets();
    InputStream is = am.open(filename);
    FileOutputStream fos = new FileOutputStream(targetFile);
    byte[] buffer = new byte[1024];
    int len = 0;
    while ((len = is.read(buffer)) != -1) {
      fos.write(buffer, 0, len);
    }
    fos.close();
    is.close();
  }

  /**
   * 将文件读取为字符串
   */
  private static String readFileContentStr(String fullFilename) {
    String readOutStr = null;
    DataInputStream dis = null;
    try {
      dis = new DataInputStream(new FileInputStream(fullFilename));
      long len = new File(fullFilename).length();
      if (len > Integer.MAX_VALUE) {
        throw new IOException("File " + fullFilename + " too large, was " + len + " bytes.");
      }
      byte[] bytes = new byte[(int) len];
      dis.readFully(bytes);
      readOutStr = new String(bytes, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (dis != null) {
        try {
          dis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return readOutStr;
  }

  /**
   * 创建文件夹
   */
  public static void mkdirs(File file) {
    if (file != null && !file.exists() && file.isDirectory()) {
      File parentFile = file.getParentFile();
      if (!parentFile.exists() && parentFile.isDirectory()) {
        mkdirs(parentFile);
      } else {
        file.mkdirs();
      }

    }
  }

  public static void deleteFile(File file) {
    if(!file.exists()){
      return;
    }
    if (file.isFile()) {
      if (file.exists()) {
        file.delete();
      }
    } else {
      File[] files = file.listFiles();
      if (files!=null && files.length>0){
        for (File listFile : files) {
          deleteFile(listFile);
        }
      }
      file.delete();
    }
  }

  /**
   * 删除本地下载书籍文件
   *
   * @param deleteLocalBook 要删除书籍
   */
  public static void deleteBook(Book deleteLocalBook) {
    String tempName = Utils.md5Digest(deleteLocalBook.getPath());
    //下载成功，删除文件
    String path;
    if (TextUtils.equals(Constant.TEXT_BOOK, deleteLocalBook.getTextbook())) {
      //
      path = deleteLocalBook.getDownloadPath() + File.separator + deleteLocalBook.getIsbn();
    } else {
      path = deleteLocalBook.getDownloadPath() + File.separator + tempName;
    }
    File bookDec = new File(path);
    if (bookDec.exists()) {
      deleteFile(bookDec);
    }
    //删除下载中的文件
    File tempFile = new File(
        deleteLocalBook.getDownloadPath() + File.separator + tempName + ".temp");
    if (tempFile.exists()) {
      tempFile.delete();
    }
  }

  /**
   * 删除本地下载书籍更新文件
   *
   * @param deleteLocalBook 要删除书籍
   */
  public static void deleteBookPatch(Book deleteLocalBook) {
    String tempName = Utils.md5Digest(deleteLocalBook.getPatchPath());
    //下载成功，删除文件
    String path;
    if (TextUtils.equals(Constant.TEXT_BOOK, deleteLocalBook.getTextbook())) {
      //
      path = deleteLocalBook.getDownloadPatchPath() + File.separator + deleteLocalBook.getIsbn();
      File bookDec = new File(path);
      if (bookDec.exists()) {
        deleteFile(bookDec);
      }
    }
    //删除下载中的文件
    File tempFile = new File(
        deleteLocalBook.getDownloadPatchPath() + File.separator + tempName + ".temp");
    if (tempFile.exists()) {
      tempFile.delete();
    }
  }

  public static void saveLog(Context context, String userAction, String actionType, String url) {
    saveLog(context, userAction, actionType, url, "", "", "", "");
  }

  public static void saveLog(Context context, String userAction, String actionType, String url,
      String isbn) {
    saveLog(context, userAction, actionType, url, isbn, "", "", "");
  }

  public static void saveLog(Context context, String userAction, String actionType, String url,
      String isbn, String chapterId, String sectionId, String online) {
    Logger.i("埋点：action = " + userAction + " actionType = " + actionType + " url = " + url);
    Log log = new Log();
    User user = MyApplication.getUser();
    if (user != null) {
      log.setUserId(user.getId());
    }
    log.setAppVersion(Utils.getAppVersionName(context));
    log.setDate(System.currentTimeMillis());
    log.setAction(userAction);
    log.setActionType(actionType);
    log.setUrl(url);
    log.setIsbn(isbn);
    log.setChapterId(chapterId);
    log.setSectionId(sectionId);
    log.setOnline(online);
    log.setDevice(Constant.Device);
    String path = Utils.getSDCardPath(context, 1) + Constant.LOG_PATH;
    String fileName = Utils.getFormatTime("yyyyMMdd") + ".txt";
    writeLog(log, path, fileName);
  }

  private static void writeLog(Log log, String path, String fileName) {
    OutputStream out = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String json = objectMapper.writeValueAsString(log) + ";";
      File file = new File(path);
      if (!file.exists()) {
        file.mkdirs();
      }
      path = path + File.separator + fileName;
      out = new FileOutputStream(path, true);
      out.write(json.getBytes("utf-8"));
      out.flush();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /*
 * InputStream --> String
 */
  private static String inputStream2String(InputStream is) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(is));
    StringBuilder buffer = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
      buffer.append(line);
    }
    return buffer.toString();
  }

  public static synchronized void commitLog(Context context) {
    if (Utils.isNetworkAvailable(context) && Utils.isWIFI(context)) {
      //有网且为wifi环境下上传日志
      String path = Utils.getSDCardPath(context, 1) + Constant.LOG_PATH;
      File parentFile = new File(path);
      if (parentFile.exists()) {
        final File[] files = parentFile.listFiles(new FileFilter() {
          @Override
          public boolean accept(File pathname) {
            Logger.i(pathname.getName());
            return !pathname.getName().contains(Utils.getFormatTime("yyyyMMdd") + ".txt");
          }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (File file : files) {
          InputStream in;
          try {
            in = new FileInputStream(file);
            String log = inputStream2String(in);
            stringBuilder.append(log);
            in.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        String content = stringBuilder.toString();
        Logger.i(content);
        User user = MyApplication.getUser();
        if (user != null && !TextUtils.isEmpty(content)) {
          Map<String, Object> serviceMap = new HashMap<>();
          Map<String, String> argsMap = new HashMap<>();
          serviceMap.put("serviceNumber", "0019000");
          serviceMap.put("serviceModule", "imed");
          serviceMap.put("TerminalType", Constant.TerminalType);
          serviceMap.put("DeviceType", Constant.DeviceType);
          serviceMap.put("token", user.getToken());
          argsMap.put("token", user.getToken());
          argsMap.put("behavior", content);
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

                }

                @Override
                public void onResponse(String response, int id) {
                  try {
                    response = URLDecoder.decode(response, "utf-8");
                    Logger.i("0019000 = "+response);
//{"serviceResult":{"result":null,"flag":true},"errorMessage":"","opFlag":true,"timestamp":"2017-05-03 15:19:55:955"}
                    JSONObject resObject = new JSONObject(response);
                    JSONObject serviceResult = resObject.getJSONObject("serviceResult");
                    String flag = serviceResult.getString("flag");
                    if (TextUtils.equals(flag, "true")) {
                      //上传成功
                      for (File file : files) {
                        if (file.exists()) {
                          file.delete();
                        }
                      }
                    }
                  } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                  }


                }
              });
        }
      }
    }
  }


  public static String hasPic(Activity activity,String url){
    String fileName = StringUtils.getUrlFileName(url);
    File file  = activity.getExternalFilesDir(null);
    final String path = file.getPath()+File.separator+"bookCover";
    File coverDes = new File(path);
    if(!coverDes.exists()){
      coverDes.mkdir();
    }
    File imageFile = new File(path+File.separator+fileName);
    if(imageFile.exists()){
      return  imageFile.getPath();
    }else {
      return null;
    }
  }


  //Glide保存图片
  public static String savePicture(final Activity activity, final String fileName, String url) {
    File file  = activity.getExternalFilesDir(null);
    final String path = file.getPath()+File.separator+"bookCover";
    File coverDes = new File(path);
    if(!coverDes.exists()){
      coverDes.mkdir();
    }
    File imageFile = new File(path+File.separator+fileName);
    if(!imageFile.exists()){
      //本地不存在该图片需要下载
      Glide.with(activity).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
        @Override
        public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
          try {
            FileUtils.saveFileToSD(path,fileName,bytes);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }else {
      return coverDes.getPath();
    }
    return null;
  }

  //往SD卡写入文件的方法
  private static void saveFileToSD(String path, String filename, byte[] bytes) throws Exception {
    String targetPath = path + File.separator + filename;
    FileOutputStream output = new FileOutputStream(targetPath);
    output.write(bytes);
    //将bytes写入到输出流中
    output.flush();
    output.close();//关闭输出流
  }

  /**
   * 根据路径获得突破并压缩返回bitmap用于显示
   */
  public static Bitmap getSmallBitmap(String filePath) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filePath, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, REQ_WIDTH, REQ_HEIGHT);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;

    return BitmapFactory.decodeFile(filePath, options);
  }
  /**
   * 计算图片的缩放值
   */
  private static int calculateInSampleSize(BitmapFactory.Options options,
      int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      // Calculate ratios of height and width to requested height and
      // width
      final int heightRatio = Math.round((float) height
          / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);

      // Choose the smallest ratio as inSampleSize value, this will
      // guarantee
      // a final image with both dimensions larger than or equal to the
      // requested height and width.
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
  }

  public static void copyFolder(String oldPath, String newPath){
    try {
      (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
      File a=new File(oldPath);
      String[] file=a.list();
      File temp=null;
      for (int i = 0; i < file.length; i++) {
        if(oldPath.endsWith(File.separator)){
          temp=new File(oldPath+file[i]);
        }
        else{
          temp=new File(oldPath+File.separator+file[i]);
        }

        if(temp.isFile()){
          FileInputStream input = new FileInputStream(temp);
          FileOutputStream output = new FileOutputStream(newPath + "/" +
              (temp.getName()).toString());
          byte[] b = new byte[1024 * 5];
          int len;
          while ( (len = input.read(b)) != -1) {
            output.write(b, 0, len);
          }
          output.flush();
          output.close();
          input.close();
        }
        if(temp.isDirectory()){//如果是子文件夹
          copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
        }
      }
      Logger.i("复制完成");
    }
    catch (Exception e) {
      Logger.i("复制整个文件夹内容操作出错");
      e.printStackTrace();

    }
  }

  // 文件解密方法
  public static void dencryptMAC2(String filename, String tofilename) throws Exception {

    String str = "18e45da374402f16";
    File filePath1 = new File(filename);
    File filePath2 = new File(tofilename);

    filePath2.createNewFile();
    InputStream in1 = new FileInputStream(filePath1);
    OutputStream os1 = new FileOutputStream(filePath2);

    byte[] srtbyte = str.getBytes("UTF-8");
    // Reader reader = null;
    String algorithm = "AES";
    SecretKey sks = new SecretKeySpec(srtbyte, algorithm);
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);

    CipherInputStream cin = new CipherInputStream(in1, cipher);

    // int length = in1.available();
    byte[] c = new byte[1024];
    int count = 0;
    while ((count = cin.read(c)) != -1) {
      os1.write(c, 0, count);
      os1.flush();
    }

    os1.close();
    in1.close();
    cin.close();
  }
}
