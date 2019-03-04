package com.mvw.medicalvisualteaching.utils;

import static com.mvw.medicalvisualteaching.fragment.BookcaseFragment.UPDATE_BOOK_STATE_ACTION;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.activity.BaseActivity;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.DownloadLocation;
import com.mvw.medicalvisualteaching.bean.RequestResult;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.bean.UserBook;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.BookDao;
import com.mvw.medicalvisualteaching.db.dao.BookDao.Properties;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao;
import com.mvw.medicalvisualteaching.fragment.BookcaseFragment;
import com.orhanobut.logger.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data
 * Created by zhaomengen on 2017/2/23.
 */

public class DataUtil {

  /**
   * 删除书架上的书籍 本地删除
   * @param deleteIsbn 书籍isbn
   * @param activity Context
   * @param handler handler
   * @param sn sn
   * @throws JSONException
   * @throws UnsupportedEncodingException
   */
  public static void deleteBookFromShelf(String deleteIsbn, Activity activity,Handler handler, String sn)
      throws JSONException, UnsupportedEncodingException {
      User user = MyApplication.getUser();
      UserBookDao deleteUserBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
      BookDao deleteBookDao = GreenDaoHelper.getDaoSession().getBookDao();
      Book deleteLocalBook = deleteBookDao.queryBuilder().where(Properties.Isbn.eq(deleteIsbn)).unique();
      //发送删除广播，暂停下载中的书籍
      Intent deleteIntent = new Intent(BookcaseFragment.DELETE_BOOK_ACTION);
      deleteIntent.putExtra("book",deleteIsbn);
      activity.sendBroadcast(deleteIntent);
      List<UserBook> userBooks = deleteUserBookDao.queryBuilder().where(UserBookDao.Properties.BIsbn.eq(deleteIsbn)).list();
     if(userBooks != null){
       int deleteCount = userBooks.size();
       for (UserBook userBook : userBooks){
         if(TextUtils.equals(userBook.getUId(),user.getId())){
           if(deleteCount > 1){
             //该书籍有其他用户也拥有，删除与本用户的关系
             deleteUserBookDao.deleteByKey(user.getId()+"+"+deleteIsbn);
           }else {
             //该书籍只有本用户拥有，删除用户关系和书籍数据
             deleteUserBookDao.deleteByKey(user.getId()+"+"+deleteIsbn);
             deleteBookDao.delete(deleteLocalBook);
             if(deleteLocalBook != null){
               //删除本地书籍文件
               FileUtils.deleteBook(deleteLocalBook);
             }
           }
         }
       }
     }

      Map<String,String> args = new HashMap<>();
      args.put("isbn",deleteIsbn);
      sendServiceResult(true,true,Constant.CMD_DELETE_FROM_BOOKSHELF,"",args,handler,sn);
  }

  public static <T> void sendLocalBookServiceResult(String command,T object,Handler handler,String sn){
    Map<String,Object> result = new ArrayMap<>();
    Map<String,Object> serviceResult = new ArrayMap<>();
    serviceResult.put("result",object);
    serviceResult.put("flag",true);
    result.put("serviceResult",serviceResult);
    result.put("errorMessage","");
    result.put("opFlag",true);
    result.put("timestamp","");
    Message msg = Message.obtain();
    Result resultObj = new Result();
    resultObj.setCommand(command);
    resultObj.setSuccess(true);
    resultObj.setResponse("");
    resultObj.setSn(sn);
    resultObj.setData(result);
    msg.obj = resultObj;
    handler.sendMessage(msg);
  }

  /**
   * 发送结果
   * @param isSuccess 请求结果
   * @param command 命令
   * @param response 结果
   * @param handler h
   * @param sn 协议流水
   */
  public static void sendServiceResult(boolean isSuccess, String command,String response, Handler handler, String sn){
    Message msg = Message.obtain();
    Result resultObj = new Result();
    resultObj.setCommand(command);
    resultObj.setSuccess(isSuccess);
    resultObj.setResponse(response);
    resultObj.setSn(sn);
    msg.obj = resultObj;
    handler.sendMessage(msg);
  }

  /**
   * 发送结果
   * @param opFlag 流程结果
   * @param result 请求结果
   * @param command 命令协议
   * @param errorMessage errorMessage
   * @param args 参数
   * @param handler handler
   * @param sn sn
   */
  public static void sendServiceResult(boolean opFlag, boolean result, String command,
      String errorMessage, Map<String, String> args, Handler handler, String sn){
    sendServiceResult(opFlag,command,responseServiceResult(opFlag,result,errorMessage,args),handler,sn);
  }

  /**
   * 发送结果
   * @param opFlag 流程结果
   * @param result 请求结果
   * @param command 请求流水
   * @param errorMessage errorMessage
   * @param args 参数
   * @param handler handler
   * @param sn sn
   */
  public static void sendServiceResult(boolean isSuccess,boolean opFlag,boolean result,String command,String errorMessage,Map<String,String> args,Handler handler,String sn){
    sendServiceResult(isSuccess,command,responseServiceResult(opFlag,result,errorMessage,args),handler,sn);
  }

  /**
   * 返回结果
   * @param opFlag 流程结果
   * @param result 请求结果
   * @param errorMessage 提示
   * @param args 参数
   * @return 结果
   */
  public static String responseServiceResult(boolean opFlag,boolean result,String errorMessage,Map<String,String> args){
    try {
      JSONObject argsObject = new JSONObject();
      if(args != null){
        for(Map.Entry<String,String> entry : args.entrySet()){
          argsObject.put(entry.getKey(),entry.getValue());
        }
      }
      argsObject.put("result",result+"");
      argsObject.put("flag",result+"");
      if(opFlag){
        argsObject.put("errorMessage",errorMessage);
      }else {
        argsObject.put("errorMessage","");
      }

      JSONObject jsonObject = new JSONObject();
      if(opFlag){
        jsonObject.put("errorMessage",errorMessage);
      }else {
        jsonObject.put("errorMessage",errorMessage);
      }
      jsonObject.put("opFlag",result+"");
      jsonObject.put("timestamp","");
      jsonObject.put("serviceResult",argsObject.toString());
      return URLEncoder.encode(jsonObject.toString(),"utf-8");
    } catch (JSONException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    //{"serviceResult":"{"result":"false"}"}
    return "";
  }

  /**
   * 本地书籍判断
   */
  public static String checkLocalBook(String bsBookJson,Activity activity,String from){
    BookDao bsBookDownloadDao = GreenDaoHelper.getDaoSession().getBookDao();
    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
    User user = MyApplication.getUser();
    Gson gson = new Gson();
    Logger.i(bsBookJson);
    Book bsBook = gson.fromJson(bsBookJson,Book.class);//要下载的书籍
    //先查询本地有没有该书籍
    Book localBook = bsBookDownloadDao.queryBuilder().where(Properties.Isbn.eq(bsBook.getIsbn())).unique();
    if(localBook == null){
      //本地没有这本书籍
      int storagePosition = SPUtil.getInstance(activity).getInt(Constant.BOOK_STORAGE_POSITION,1);
      if(TextUtils.equals(Constant.TEXT_BOOK, bsBook.getTextbook())){
        bsBook.setDownloadPath(Utils.getSDCardPath(activity,storagePosition)+Constant.BOOK_PATH_TEXTBOOK);
      }else {
        bsBook.setDownloadPath(Utils.getSDCardPath(activity,storagePosition)+Constant.BOOK_PATH_PDF);
      }
      bsBookDownloadDao.insertOrReplace(bsBook);
    }else {
      bsBook.setDownloadState(localBook.getDownloadState());
      bsBook.setTotal(localBook.getTotal());
      bsBook.setDownloaded(localBook.getDownloaded());
      bsBook.setDownloadPath(localBook.getDownloadPath());
      bsBook.setDownloadFile(localBook.getDownloadFile());
      bsBookDownloadDao.insertOrReplace(bsBook);
    }
    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+bsBook.getIsbn())).unique();
    if(userBook == null){
      //购买状态默认设置为已购买  默认是未下载状态
      DataUtil.saveUserBook(userBookDao,bsBook,Constant.UNDOWNLOAD,Constant.STATUS_BUY);
      if(from != null && from.equals("obtained")){
        //从已购图书点击下载，通知书架刷新界面
//        DataUtil.sendBookBroadcast(activity,BookcaseFragment.REFRESH_BOOKCASE_ACTION,"");
//        MyApplication.bookShelfRefresh = true;
      }
    }else {
      DataUtil.saveUserBook(userBookDao,bsBook,Constant.UNDOWNLOAD,bsBook.getBuyStatus());
    }
    return bsBook.getIsbn();
  }

  /**
   * 书籍下载，暂停，取消，删除 广播
   */
  public static void sendBookBroadcast(Activity activity, String action, String extraString){
    Intent intent = new Intent(action);
    intent.putExtra("book",extraString);
    activity.sendBroadcast(intent);
  }
  /**
   * 书籍下载，暂停，取消，删除 广播
   */
  public static void sendBookBroadcast(Activity activity, String action, String extraString,String from,String nonWifi){
    Intent intent = new Intent(action);
    intent.putExtra("book",extraString);
    intent.putExtra("from",from);
    intent.putExtra("noWifi",nonWifi);
    activity.sendBroadcast(intent);
  }
  /**
   * 发送书籍状态广播,通知书籍详情
   */
  public static void sendBookState(Activity activity,String isbn, String state){
    Intent intent = new Intent(UPDATE_BOOK_STATE_ACTION);
    intent.putExtra("isbn",isbn);
    intent.putExtra("state",state);
    activity.sendBroadcast(intent);
  }
  /**
   * 更新保存用户书籍的状态信息
   * @param userBookDB UserBook表操作
   * @param book 用户相关的书籍
   * @param state 该用户的书籍状态
   * @param buyStatus 该用户的书籍购买状态
   */
//  public static void saveUserBook(UserBookDao userBookDB,Book book, String state, String buyStatus){
//    saveUserBook(userBookDB,book,state,buyStatus,"");
//  }

  /**
   * 更新保存用户书籍的状态信息
   * @param userBookDB UserBook表操作
   * @param book 用户相关的书籍
   * @param state 该用户的书籍状态
   * @param buyStatus 该用户的书籍购买状态
   */
  public static void saveUserBook(UserBookDao userBookDB,Book book, String state, String buyStatus){
    User user = MyApplication.getUser();
    String id = user.getId()+"+"+ book.getIsbn();
    UserBook userBook = userBookDB.queryBuilder().where(UserBookDao.Properties.Id.eq(id)).unique();
    if(userBook == null){
      userBook = new UserBook();
    }
    userBook.setId(id);
    userBook.setBIsbn(book.getIsbn());
    userBook.setUId(user.getId());
    userBook.setState(state);
    userBook.setBuyStatus(buyStatus);//用户购买状态
    userBook.setBookDeadline(book.getBookDeadline());//到期时间
    userBook.setDay(book.getDay());//到期天数
    userBook.setIsExpired(book.getIsExpired());//借阅标识
    userBook.setType(book.getType());
    userBook.setSectionId(book.getSectionId());//阅读节id
    userBook.setOrder(book.getOrder());
    userBook.setPatchVersion(userBook.getPatchVersion());
    userBookDB.insertOrReplace(userBook);
  }

  /**
   * 更新本地下载完成的书籍的版本
   */
  public static void updateUBPatchVersion(UserBookDao userBookDB,String isbn,String patchVersion){
    User user = MyApplication.getUser();
    String id = user.getId()+"+"+ isbn;
    UserBook userBook = userBookDB.queryBuilder().where(UserBookDao.Properties.Id.eq(id)).unique();
    if(userBook == null){
      return;
    }
    userBook.setPatchVersion(patchVersion);
    userBookDB.insertOrReplace(userBook);
  }

  public static void saveUserBookOrder(String isbn,boolean isPdf){
    User user = MyApplication.getUser();
    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
    if(userBook != null){
      List<UserBook> userBookList = userBookDao.queryBuilder().orderDesc(UserBookDao.Properties.Order).limit(1).list();
      int order = 0;
      if(userBookList != null && userBookList.size()>0){
        order = userBookList.get(0).getOrder()+1;
      }
      userBook.setOrder(order);
      if (isPdf){
        userBook.setSectionId("1");
      }
//      userBook.setState(Constant.BOOK_COMPLETED);
      userBookDao.insertOrReplace(userBook);
    }
  }
  public static void saveUserBookOrder(String isbn){
    saveUserBookOrder(isbn,false);
  }
  public static UserBook getUserBook(Book book, String state, String buyStatus,String patchVersion){
    User user = MyApplication.getUser();
    String id = user.getId()+"+"+ book.getIsbn();
    UserBook userBook = new UserBook();
    userBook.setId(id);
    userBook.setBIsbn(book.getIsbn());
    userBook.setUId(user.getId());
    userBook.setState(state);
    userBook.setBuyStatus(buyStatus);//用户购买状态
    userBook.setBookDeadline(book.getBookDeadline());//到期时间
    userBook.setDay(book.getDay());//到期天数
    userBook.setIsExpired(book.getIsExpired());//借阅标识
    userBook.setType(book.getType());
    userBook.setSectionId(book.getSectionId());//阅读节id
    userBook.setOrder(book.getOrder());
    userBook.setPatchVersion(patchVersion);
    return userBook;
  }

  /**
   * 获取下载位置信息
   */
  public static void getDownloadLocations(Activity activity,Handler handler,String sn)
      throws JSONException, UnsupportedEncodingException {
    RequestResult requestResult = new RequestResult();
    DownloadLocation phonePath = new DownloadLocation();
    phonePath.setName("手机内存");
    phonePath.setAvailableSize(Utils.getSDCardAvailableSize(activity,1));
    phonePath.setId(1);
    DownloadLocation sdPath = new DownloadLocation();
    sdPath.setName("SD卡");
    sdPath.setAvailableSize(Utils.getSDCardAvailableSize(activity,2));
    sdPath.setId(2);
    //判断选择了哪一个
    int storagePosition = SPUtil.getInstance(activity).getInt(Constant.BOOK_STORAGE_POSITION,-1);
    if(storagePosition == 1){
      //选择了内存
      phonePath.setSelected("true");
      sdPath.setSelected("false");
    }else if(storagePosition == 2){
      //sd卡
      phonePath.setSelected("false");
      sdPath.setSelected("true");
    }else {
      phonePath.setSelected("false");
      sdPath.setSelected("false");
    }
    List<DownloadLocation> locations = new ArrayList<>();
    locations.add(phonePath);
    locations.add(sdPath);
    Gson gson = new Gson();
    JSONArray array = new JSONArray(gson.toJson(locations));
    JSONObject serviceObject = new JSONObject();
    serviceObject.put("flag","true");
    serviceObject.put("errorMessage","");
    serviceObject.put("result",array);
    requestResult.setServiceResult(serviceObject.toString());
    String response = gson.toJson(requestResult);
    response = URLEncoder.encode(response,"utf-8");
    sendServiceResult(true,Constant.GET_DOWNLOAD_LOCATION,response,handler,sn);
  }

  /**
   * 更新书籍下载状态
   */
  public static void getBookState(Activity activity,String isbn){
    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
    User user = MyApplication.getUser();
    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
    //在WebActivity进行通知详情页下载状态设置
    if(userBook != null){
      sendBookState(activity,isbn,userBook.getState());
    }else {
      sendBookState(activity,isbn,Constant.UNDOWNLOAD);
    }
  }
  public static void getBookState(String isbn,Handler handler,String sn)
      throws JSONException, UnsupportedEncodingException {
    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
    User user = MyApplication.getUser();
    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
    Map<String,Object> result = new ArrayMap<>();
    result.put("downloadState",userBook.getState());
    result.put("isbn",isbn);
    sendLocalBookServiceResult(Constant.GET_BOOK_STATE,result,handler,sn);
  }

  /**
   *  获取app版本
   */
  public static void getAppVersion(Activity activity,Handler handler,String sn)
      throws JSONException, UnsupportedEncodingException {
    Gson gson = new Gson();
    RequestResult requestResult = new RequestResult();
    JSONObject resultObject = new JSONObject();
    resultObject.put("appVersion",Utils.getAppVersionName(activity));
    resultObject.put("appBusUrl", AppConfig.HOST);
    JSONObject serviceObject = new JSONObject();
    serviceObject.put("flag","true");
    serviceObject.put("errorMessage","");
    serviceObject.put("result",resultObject);
    requestResult.setServiceResult(serviceObject.toString());
    String response = gson.toJson(requestResult);
    response = URLEncoder.encode(response,"utf-8");
    sendServiceResult(true,Constant.GET_APP_VERSION,response,handler,sn);
  }

  /**
   * 获取网络状态
   */
  public static void getNetwork(Activity activity,Handler handler,String sn)
      throws JSONException, UnsupportedEncodingException {
    //取网络状态 0:未知 & 1:无网络 & 2:4G & 3:WIFI
    String networkState = "1";
    if(Utils.isNetworkAvailable(activity)){
      if(!Utils.isWIFI(activity)){
        //有网络情况下不是wifi就按移动网络算
        networkState = "2";
      }else {
        //wifi
        networkState = "3";
      }
    }
    Gson gson = new Gson();
    RequestResult requestResult = new RequestResult();
    JSONObject resultObject = new JSONObject();
    resultObject.put("network",networkState);
    JSONObject serviceObject = new JSONObject();
    serviceObject.put("flag","true");
    serviceObject.put("errorMessage","");
    serviceObject.put("result",resultObject);
    requestResult.setServiceResult(serviceObject.toString());
    String response = gson.toJson(requestResult);
    response = URLEncoder.encode(response,"utf-8");
    sendServiceResult(true,Constant.GET_NETWORK_STATE,response,handler,sn);
  }

  /**
   * 通知web显示toast
   */
  public static void showWebToast(BaseActivity activity,String info){
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("msg",info);
//      String response = URLEncoder.encode(jsonObject.toString(),"utf-8");
      String response = jsonObject.toString();
      activity.appCallWeb(Constant.MSG_TOAST_INFO,Constant.MSG_TOAST_INFO,response);
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public static String getBookUpdateState(Book book,UserBook userBook){
    String isUpdate = "0";
    String localVersion = userBook.getPatchVersion();
    String netVersion = book.getPatchVersion();
    if(!TextUtils.equals(netVersion,localVersion)){
      //不相等说明有更新
      if(Constant.BOOK_COMPLETED.equals(userBook.getState())){
        isUpdate = Constant.BOOK_UPDATE;
      }
    }
    return isUpdate;
  }

}
