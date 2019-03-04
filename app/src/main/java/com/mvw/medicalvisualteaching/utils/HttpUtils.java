package com.mvw.medicalvisualteaching.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mvw.medicalvisualteaching.activity.MainActivity;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.BookcaseResult;
import com.mvw.medicalvisualteaching.bean.BookcaseServiceResult;
import com.mvw.medicalvisualteaching.bean.Payment;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.medicalvisualteaching.bean.Update;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.bean.UserBook;
import com.mvw.medicalvisualteaching.callback.ResultCallback;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.BookDao;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao.Properties;
import com.mvw.netlibrary.OkHttpUtils;
import com.mvw.netlibrary.callback.Callback;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import okhttp3.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * http
 * Created by a on 2016/11/28.
 */
public class HttpUtils {

    public static void bookShelf(User user,boolean refresh,Handler handler,String sn, final Activity activity){
//        if(refresh){
//            //从本地数据库加载书籍
//            //错误异常时，加载本地数据库的数据
//            String responseStr = bookShelfFromDB(user,refresh);
//            if(responseStr != null){
//                DataUtil.sendServiceResult(true,Constant.GET_BOOK_SHELF_LIST,responseStr,handler,sn);
//            }else {
//                DataUtil.sendServiceResult(false,Constant.GET_BOOK_SHELF_LIST,"",handler,sn);
//            }
//        }else {
            //从网络获取，刷新购买状态
            bookShelfFromNet(user,refresh,handler,sn,activity);
//        }
    }

    /**
     * 获取书架书籍
     */
    private static void bookShelfFromNet(final User user, final boolean refresh,Handler handler,String sn, final Activity activity){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0203100");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("uuid",user.getId());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        final Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_BOOK_SHELF_LIST){
                @Override
                public Result parseResponse(Result result) {
                    try {
                        long startTime = System.currentTimeMillis();
                        //对书架获取数据进行处理分析
                        String str = URLDecoder.decode(result.getResponse(),"utf-8");

                        //"errorMessage":"","opFlag":"true","timestamp":"2017-02-08 10:54:29:234"
                        JSONObject jsonObject = new JSONObject(str);
                        String errorMessage = jsonObject.getString("errorMessage");
                        String opFlag = jsonObject.getString("opFlag");
                        String timestamp = jsonObject.getString("timestamp");
                        String serviceResultStr = jsonObject.getString("serviceResult");
                        Logger.i("bookdata----"+serviceResultStr);
//                        ObjectMapper objectMapper = new ObjectMapper();
                        BookcaseServiceResult serviceResult = gson.fromJson(serviceResultStr,BookcaseServiceResult.class);
                        BookcaseResult bookcaseResult = serviceResult.getResult();
                        List<Book> resultList = bookcaseResult.getBooks();
                        Logger.e("解析 时间 "+(System.currentTimeMillis() - startTime));
                        String flag = serviceResult.getFlag();

                        BookDao bookDB = GreenDaoHelper.getDaoSession().getBookDao();
                        UserBookDao userBookDB = GreenDaoHelper.getDaoSession().getUserBookDao();
                        User user = MyApplication.getUser();
                        int storagePosition = SPUtil.getInstance(activity).getInt(Constant.BOOK_STORAGE_POSITION,-1);
                        String basePath = Utils.getSDCardPath(activity,storagePosition);
                        //查询本地存储的所有书籍
                        List<Book> listLocalBooks = bookDB.queryBuilder().list();
                        Map<String,Book> dbBooks = new HashMap<>();
                        for(Book localBook : listLocalBooks){
                            dbBooks.put(localBook.getIsbn(), localBook);
                        }
                        //查询用户所有的书籍下载状态值
                        List<UserBook> userBooks = userBookDB.queryBuilder().where(Properties.UId.eq(user.getId())).list();
                        Map<String,UserBook> dbUserBookState = new HashMap<>();
                        for(UserBook userBook : userBooks){
                            dbUserBookState.put(userBook.getBIsbn(),userBook);
                        }
                        if(TextUtils.equals("true",flag)) {
                            //用户id记录用户是否第一次获取书架数据
                            if(SPUtil.getInstance(activity).getBoolean(user.getId(),true)){
                                //用户第一次获取书架的数据，存本地
                                List<Book> updateBookList = new ArrayList<>();
                                List<UserBook> updateUserBookList = new ArrayList<>();
                                for(int i=0; i<resultList.size(); i++){
                                    //对书架里的每本书进行处理
                                    Book netBook = resultList.get(i);
                                    //在此下载图书封面图片
                                    String cover = netBook.getCover();
                                    String path = FileUtils.hasPic(activity,cover);
                                    if(path != null){
                                        netBook.setCoverBase("file://"+path);
                                        netBook.setCover("file://"+path);
                                    }else {
                                        netBook.setCoverBase(cover);
                                    }
                                    if(activity instanceof MainActivity){
                                        Message message = Message.obtain();
                                        message.obj = cover;
                                        message.what = 1000;
                                        ((MainActivity) activity).getHandler().sendMessage(message);
                                    }
                                    //下载路径设置
                                    String downloadPath ;
                                    if(netBook.getTextbook() == null || Constant.TEXT_BOOK.equals(netBook.getTextbook())){
                                        //教材路径
                                        downloadPath = basePath + Constant.BOOK_PATH_TEXTBOOK;
                                    }else {
                                        //pdf路径
                                        downloadPath = basePath + Constant.BOOK_PATH_PDF;
                                    }
                                    netBook.setDownloadPath(downloadPath);
                                    netBook.setDownloadState(Constant.UNDOWNLOAD);//默认未下载
                                    long start1 = System.currentTimeMillis();
                                    updateBookList.add(netBook);
//                                    bookDB.insertOrReplace(netBook);
                                    //存储用户对应书籍的信息
                                    UserBook ub = DataUtil.getUserBook(netBook,Constant.UNDOWNLOAD,netBook.getBuyStatus(),"");
                                    updateUserBookList.add(ub);
                                    long time1 = System.currentTimeMillis() - start1;
//                                    Logger.i(" 耗时 i = ： "+time1);
//                                    bookList.add(netBook);
                                }
                                bookDB.insertOrReplaceInTx(updateBookList);
                                userBookDB.insertOrReplaceInTx(updateUserBookList);
                                SPUtil.getInstance(activity).save(user.getId(),false);
                                Collections.sort(resultList);
                                Map<String,Object> resultMap = new ArrayMap<>();
                                resultMap.put("errorMessage",errorMessage);
                                resultMap.put("opFlag",opFlag);
                                resultMap.put("timestamp",timestamp);
                                Map<String,Object> serviceMap = new ArrayMap<>();
                                serviceMap.put("flag",flag);
                                //设置book
                                Map<String,Object> reMap = new ArrayMap<>();
                                reMap.put("books",resultList);
                                //设置顶部旋转内容
                                reMap.put("rotateData",bookcaseResult.getRotateData());

                                serviceMap.put("result",reMap);
                                resultMap.put("serviceResult",gson.toJson(serviceMap));
                                Logger.i(" 书架耗时："+(System.currentTimeMillis() - startTime));
                                startTime = System.currentTimeMillis();
                                String response = URLEncoder.encode(gson.toJson(resultMap),"utf-8");
                                Logger.i(" 书架编码耗时："+(System.currentTimeMillis() - startTime));
                                result.setResponse(response);
                            }else {
                                long t = System.currentTimeMillis();
                                Map<String,Book> netBookMap = new HashMap<>();
                                for(int i=0; i<resultList.size(); i++) {
                                    Book netBook = resultList.get(i);
                                    netBookMap.put(netBook.getIsbn(),netBook);
                                }
                                //只判断服务端接口的购买状态，刷新本地
                                List<Book> bookList = new ArrayList<>();
                                List<UserBook> userBookList = new ArrayList<>();
                                for(Entry<String,UserBook> entry : dbUserBookState.entrySet()){
                                    String isbn = entry.getKey();
                                    UserBook userBook = entry.getValue();
                                    Book dbLocalBook = dbBooks.get(isbn);//本地数据库的书籍
                                    if(TextUtils.equals(dbLocalBook.getTextbook(),Constant.TEXT_BOOK)){
                                        //该用户的书为教材
                                        Book netBook = netBookMap.get(isbn);//接口获取的全是教材
                                        netBook.setDownloadState(dbLocalBook.getDownloadState());
                                        netBook.setTotal(dbLocalBook.getTotal());
                                        netBook.setDownloaded(dbLocalBook.getDownloaded());
                                        netBook.setDownloadPath(dbLocalBook.getDownloadPath());
                                        netBook.setDownloadFile(dbLocalBook.getDownloadFile());
                                        netBook.setOrder(userBook.getOrder());
                                        if(userBook.getSectionId() != null){
                                            netBook.setSectionId(userBook.getSectionId());
                                        }
                                        //在此下载图书封面图片
                                        String cover = netBook.getCover();
                                        String path = FileUtils.hasPic(activity,cover);
                                        if(path != null){
                                            netBook.setCoverBase("file://"+path);
                                            netBook.setCover("file://"+path);
                                        }else {
                                            netBook.setCoverBase(cover);
                                        }
                                        if(activity instanceof MainActivity){
                                            Message message = Message.obtain();
                                            message.obj = cover;
                                            message.what = 1000;
                                            ((MainActivity) activity).getHandler().sendMessage(message);
                                        }

//                                        bookDB.update(netBook);//更新本地书籍

                                        //该用户在本地有这本书，修改这本书的购买状态
//                                        DataUtil.saveUserBook(userBookDB,netBook,userBook.getState(),netBook.getBuyStatus());
                                        UserBook ub = DataUtil.getUserBook(netBook,userBook.getState(),netBook.getBuyStatus(),userBook.getPatchVersion());
                                        userBookList.add(ub);

                                        //下载状态初始化
                                        String state = userBook.getState();
                                        if(!refresh){
                                            //不是刷新时获取数据，书籍状态进行初始处理
                                            if(Constant.UPZIP_LOADING.equals(state)){//安装中的变为暂停
                                                state = Constant.DOWNLOAD_STOP;
                                            }
                                            else if(Constant.DOWNLOAD_PENDING.equals(state) || Constant.DOWNLOAD_LOADING.equals(state)){
                                                state = Constant.DOWNLOAD_STOP;
                                            }
                                        }
                                        netBook.setDownloadState(state);
                                        //判断是否需要更新书籍
                                        netBook.setIsUpdate(DataUtil.getBookUpdateState(netBook,userBook));

                                        bookList.add(netBook);
                                    }else {
                                        dbLocalBook.setOrder(userBook.getOrder());
                                        if(userBook.getSectionId() != null){
                                            dbLocalBook.setSectionId(userBook.getSectionId());
                                        }
                                        //在此下载图书封面图片
                                        String cover = dbLocalBook.getCover();
                                        String path = FileUtils.hasPic(activity,cover);
                                        if(path != null){
                                            dbLocalBook.setCoverBase("file://"+path);
                                            dbLocalBook.setCover("file://"+path);
                                        }else {
                                            dbLocalBook.setCoverBase(cover);
                                        }
                                        if(activity instanceof MainActivity){
                                            Message message = Message.obtain();
                                            message.obj = cover;
                                            message.what = 1000;
                                            ((MainActivity) activity).getHandler().sendMessage(message);
                                        }

                                        dbLocalBook.setBuyStatus(userBook.getBuyStatus());
                                        //pdf如果借阅的话，需要计算借阅时间
                                        if(TextUtils.equals(Constant.STATUS_BORROW,dbLocalBook.getBuyStatus())){
                                            //该书籍是借阅书籍
                                            //离线的状态下，借阅的天数需要进行计算
                                            //"bookDeadline":"2017-05-01 19:06:43"
//                                            long bookDeadLine = Utils.stringToDate(dbLocalBook.getBookDeadline(),"yyyy-MM-dd HH:mm:ss");
//                                            if(System.currentTimeMillis() < bookDeadLine){
//                                                //当前时间小于到期时间
//                                                dbLocalBook.setIsExpired(Constant.STATUS_NOT_EXPIRED);//设置为未到期
//                                                int day = (int) ((bookDeadLine - System.currentTimeMillis())/(1000*60*60*24));
//                                                dbLocalBook.setDay(day+"");//设置到期天数
//                                            }else {
//                                                //当前时间大于到期时间，书籍已到期
//                                                dbLocalBook.setIsExpired(Constant.STATUS_EXPIRED);
//                                                dbLocalBook.setDay("0");
//                                                dbLocalBook.setBuyStatus(Constant.STATUS_NOT_BUY);
//                                            }
                                        }
                                        //下载状态初始化
                                        String state = userBook.getState();
                                        if(!refresh){
                                            //不是刷新时获取数据，书籍状态进行初始处理
                                            if(Constant.UPZIP_LOADING.equals(state)){//安装中的变为暂停
                                                state = Constant.DOWNLOAD_STOP;
                                            }
                                            else if(Constant.DOWNLOAD_PENDING.equals(state) || Constant.DOWNLOAD_LOADING.equals(state)){
                                                state = Constant.DOWNLOAD_STOP;
                                            }
                                        }
                                        dbLocalBook.setDownloadState(state);
                                        bookList.add(dbLocalBook);
                                    }
                                }
                                bookDB.updateInTx(bookList);
                                userBookDB.updateInTx(userBookList);

                                Logger.i(" 查询修改耗时："+(System.currentTimeMillis() - t));
                                Collections.sort(bookList);
                                Map<String,Object> resultMap = new ArrayMap<>();
                                resultMap.put("errorMessage",errorMessage);
                                resultMap.put("opFlag",opFlag);
                                resultMap.put("timestamp",timestamp);
                                Map<String,Object> serviceMap = new ArrayMap<>();
                                serviceMap.put("flag",flag);
                                //设置book
                                Map<String,Object> reMap = new ArrayMap<>();
                                reMap.put("books",bookList);
                                //设置顶部旋转内容
                                reMap.put("rotateData",bookcaseResult.getRotateData());

                                serviceMap.put("result",reMap);
                                resultMap.put("serviceResult",gson.toJson(serviceMap));
//                            Logger.i("result = "+resultJson.toString());
                                Logger.i(" 书架耗时："+(System.currentTimeMillis() - startTime));
                                startTime = System.currentTimeMillis();
                                String response = URLEncoder.encode(gson.toJson(resultMap),"utf-8");
                                Logger.i(" 书架编码耗时："+(System.currentTimeMillis() - startTime));
                                result.setResponse(response);
                            }
                        }else {
                            result.setResponse(bookShelfFromDB(user,refresh));
                        }
                    }catch ( JSONException | IOException e) {
                        e.printStackTrace();
                        result.setResponse(bookShelfFromDB(user,refresh));
                    }
                    return super.parseResponse(result);
                }

                @Override
                public String onErrorResponse() {
                    //错误异常时，加载本地数据库的数据
                    return bookShelfFromDB(user,refresh);
                }
            });
    }

  /**
   * 从本地获取书架数据
   * @return response
   */
  private static String bookShelfFromDB(User user,boolean refresh){
      try {
          UserBookDao userBookDB = GreenDaoHelper.getDaoSession().getUserBookDao();
          BookDao bookDB = GreenDaoHelper.getDaoSession().getBookDao();
          List<Book> localBooks = new ArrayList<>();
          //获取所有的用户书籍
          List<UserBook> userBookList = userBookDB.queryBuilder().where(Properties.UId.eq(user.getId())).list();
          for(UserBook userBook : userBookList){
              Book localBook = bookDB.queryBuilder().where(BookDao.Properties.Isbn.eq(userBook.getBIsbn())).unique();
              localBook.setBuyStatus(userBook.getBuyStatus());
              localBook.setBookDeadline(userBook.getBookDeadline());
              localBook.setIsExpired(userBook.getIsExpired());
              localBook.setType(userBook.getType());
              localBook.setOrder(userBook.getOrder());
              if(userBook.getSectionId() != null){
                  localBook.setSectionId(userBook.getSectionId());
              }
              if(TextUtils.equals(Constant.STATUS_BORROW,userBook.getBuyStatus())){
                  //该书籍是借阅书籍
                  //离线的状态下，借阅的天数需要进行计算
                  //"bookDeadline":"2017-05-01 19:06:43"
//                  long bookDeadLine = Utils.stringToDate(userBook.getBookDeadline(),"yyyy-MM-dd HH:mm:ss");
//                  if(System.currentTimeMillis() < bookDeadLine){
//                      //当前时间小于到期时间
//                      localBook.setIsExpired(Constant.STATUS_NOT_EXPIRED);//设置为未到期
//                      int day = (int) ((bookDeadLine - System.currentTimeMillis())/(1000*60*60*24));
//                      localBook.setDay(day+"");//设置到期天数
//                  }else {
//                      //当前时间大于到期时间，书籍已到期
//                      localBook.setIsExpired(Constant.STATUS_EXPIRED);
//                      localBook.setDay("0");
//                      localBook.setBuyStatus(Constant.STATUS_NOT_BUY);
//                  }
              }

              String state = userBook.getState();
              if(!refresh){
                  if(Constant.UPZIP_LOADING.equals(state)){
                      state = Constant.DOWNLOAD_STOP;
                  }
                  else if(Constant.DOWNLOAD_PENDING.equals(state) || Constant.DOWNLOAD_LOADING.equals(state)){
                      state = Constant.DOWNLOAD_STOP;
                  }
              }
              localBook.setDownloadState(state);
              localBooks.add(localBook);
          }
          Collections.sort(localBooks);
          Gson gson = new GsonBuilder().serializeNulls().create();
          String jsonBooks = gson.toJson(localBooks);
          JSONArray jsonBookArray = new JSONArray(jsonBooks);
          JSONObject resultJson = new JSONObject();
          resultJson.put("errorMessage","");
          resultJson.put("opFlag","true");
          resultJson.put("timestamp","");
          JSONObject serviceJson = new JSONObject();
          serviceJson.put("flag","true");
          JSONObject reObject = new JSONObject();
          reObject.put("books",jsonBookArray);
          reObject.put("rotateData",new JSONArray());
          serviceJson.put("result",reObject);
          resultJson.put("serviceResult",serviceJson.toString());
//          Logger.i("result = "+resultJson.toString());
          return URLEncoder.encode(resultJson.toString(),"utf-8");
      } catch (JSONException | UnsupportedEncodingException e) {
          e.printStackTrace();
      }
      return null;
  }
    /**
     * 获取已获得图书
     */
    public static void obtainedBooks(User user,String obtainedPage,String obtainedPageSize, Handler handler, String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0203000");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("page",obtainedPage);
        argsMap.put("pageSize",obtainedPageSize);
        argsMap.put("uuid",user.getId());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_PURCHASE_BOOK_LIST){
                @Override
                public Result parseResponse(Result result) {
                    try {
                        String str = URLDecoder.decode(result.getResponse(),"utf-8");
                        //"errorMessage":"","opFlag":"true","timestamp":"2017-02-08 10:54:29:234"
                        JSONObject jsonObject = new JSONObject(str);
                        String errorMessage = jsonObject.getString("errorMessage");
                        String opFlag = jsonObject.getString("opFlag");
                        String timestamp = jsonObject.getString("timestamp");
                        String serviceResultStr = jsonObject.getString("serviceResult");
//                        Logger.i(serviceResultStr);
                        JSONObject serviceResultObject = new JSONObject(serviceResultStr);
                        JSONObject resultObject = serviceResultObject.getJSONObject("result");
                        JSONArray resultArray = resultObject.getJSONArray("books");
                        String flag = serviceResultObject.getString("flag");

                        Gson gson = new Gson();
                        BookDao bookDB = GreenDaoHelper.getDaoSession().getBookDao();
                        UserBookDao userBookDB = GreenDaoHelper.getDaoSession().getUserBookDao();
                        User user = MyApplication.getUser();

                        //查询所有的书籍
                        List<Book> listLocalBooks = bookDB.queryBuilder().list();
                        Map<String,Book> dbBooks = new HashMap<>();
                        for(Book localBook : listLocalBooks){
                            dbBooks.put(localBook.getIsbn(), localBook);
                        }
                        //查询用户所有的书籍下载状态值
                        List<UserBook> userBooks = userBookDB.queryBuilder().where(Properties.UId.eq(user.getId())).list();
                        Map<String,String> dbStates = new HashMap<>();
                        for(UserBook userBook : userBooks){
                            dbStates.put(userBook.getBIsbn(),userBook.getState());
                        }
                        if(TextUtils.equals("true",flag)) {
                            List<Book> books = new ArrayList<>();
                            for(int i=0; i<resultArray.length(); i++) {
                                //对书架里的每本书进行处理
                                JSONObject object = resultArray.getJSONObject(i);
                                Book book = gson
                                    .fromJson(object.toString(), Book.class);
                                if(dbStates.containsKey(book.getIsbn())){//该用户有这本书
                                    Book localBook = dbBooks.get(book.getIsbn());
                                    if(localBook != null){
                                        //把用户下载书的状态更新
                                        book.setTotal(localBook.getTotal());
                                        book.setDownloaded(localBook.getDownloaded());
                                        book.setDownloadPath(localBook.getDownloadPath());
                                        book.setDownloadFile(localBook.getDownloadFile());
                                        book.setDownloadState(dbStates.get(book.getIsbn()));
                                    }
                                }
                                books.add(book);
                            }
                            String jsonBooks = gson.toJson(books);
                            JSONArray jsonBookArray = new JSONArray(jsonBooks);
                            JSONObject resultJson = new JSONObject();
                            resultJson.put("errorMessage",errorMessage);
                            resultJson.put("opFlag",opFlag);
                            resultJson.put("timestamp",timestamp);
                            JSONObject serviceJson = new JSONObject();
                            serviceJson.put("flag",flag);
                            JSONObject reObject = new JSONObject();
                            reObject.put("books",jsonBookArray);
                            serviceJson.put("result",reObject);
                            resultJson.put("serviceResult",serviceJson.toString());
                            Logger.i("  已获得图书result = "+resultJson.toString());
                            String response = URLEncoder.encode(resultJson.toString(),"utf-8");
                            result.setResponse(response);
                            return result;
                        }
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                public String onErrorResponse() {
                    return bookObtain();
                }
            });
    }

    /**
     * 从获取已获得的书籍数据 网络加载错误时 传递一个空数组
     * @return response
     */
    private static String bookObtain(){
        try {
            JSONObject resultJson = new JSONObject();
            resultJson.put("errorMessage","");
            resultJson.put("opFlag","true");
            resultJson.put("timestamp","");
            JSONObject serviceJson = new JSONObject();
            serviceJson.put("flag","true");
            JSONObject reObject = new JSONObject();
            reObject.put("books",new JSONArray());
            reObject.put("rotateData",new JSONArray());
            serviceJson.put("result",reObject);
            resultJson.put("serviceResult",serviceJson.toString());
            return URLEncoder.encode(resultJson.toString(),"utf-8");
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

  /**
   * 获取充值界面初始化数据
   */
    public static void recharge(User user, Handler handler, String sn){
        /*
        {serviceModule:"BS-Service",serviceNumber:'0301200',token: BOOK.token, args:{
        uuid:"1",platform:""
    }}
         */
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0301200");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("platform",Constant.PLATFORM);//1:android    0 ：ios
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_RECHARGE_PACKAGE_LIST));
    }



    /**
     * 判断书籍是否购买
     */
    public static void buyStatus(User user,String isbnStatus, Handler handler, String sn){
        /*
        {serviceModule:"BS-Service",serviceNumber:'0201400',token: "", args:{
       ISBN:"",bookName:""
    }}
         */
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0201400");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("isbn",isbnStatus);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_IS_PURCHASED_BOOK));
    }

    /**
     * 获取书籍里的自测题
     */
    public static void getBookChapterExam(User user,String isbn,String chapterNo,String type, Handler handler, String sn){
        /*
        {isbn:Book.params.isbn,chapterNo:dataId,type:dataType}
         */
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0006000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("isbn",isbn);
        argsMap.put("chapterNo",chapterNo);
        argsMap.put("type",type);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_BOOK_CHAPTER_EXAM_LIST));
    }

    /**
     * 签到
     */
    public static void sign(String params, Handler handler, String sn){

    }
    /**
     * 获取签到状态
     */
    public static void signStatus(String params, Handler handler, String sn){

    }

    /**
     * 跨书搜图书列表
     */
    public static void crossSearch(String token,String keyword, Handler handler, String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0004000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("keyword",keyword);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_CROSS_SEARCH_BOOK_LIST));
    }

  /***
   * 跨书搜图书详情列表
   * @param token token
   * @param keyword 关键字
   * @param isbnSearch 书籍isbn
   */
    public static void crossSearchBook(String token,String keyword,String isbnSearch, Handler handler, String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0005000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("keyword",keyword);
        argsMap.put("isbn",isbnSearch);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_CROSS_SEARCH_BOOK));
    }

  /**
   * 搜索图书列表
   */
    public static void searchBookList(String token,String condition,String pageSize,String page, Handler handler, String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0202100");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("condition",condition);
        argsMap.put("pageSize",pageSize);
        argsMap.put("page",page);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_CROSS_SEARCH_BOOK));
    }

  /**
   * 获取热门搜索关键字
   */
    public static void getHotWordList(String token,Handler handler, String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0202400");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_CROSS_SEARCH_BOOK));
    }

    /**
     * 获取图书章的列表
     */
    public static void getBookCatelog(String token,final String isbn, final Handler handler, final String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0002000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("isbn",isbn);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_BOOK_CATELOG){
                @Override
                public Result parseResponse(Result result) {
                    try {
                        String bookResponse = URLDecoder.decode(result.getResponse(),"utf-8");
                        JSONObject jsonObject = new JSONObject(bookResponse);
                        JSONObject data = jsonObject.getJSONObject("serviceResult");
                        JSONObject data2 = data.getJSONObject("result");
                        UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
                        UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(MyApplication
                            .getUser().getId()+"+"+isbn)).unique();
                        String sid = "";
                        if (userBook!=null && !TextUtils.isEmpty(userBook.getSectionId())){
                            sid = userBook.getSectionId();
                        }
                        data2.put("sectionId",sid);
                        data.put("result",data2);
                        jsonObject.put("serviceResult",data);
                        result.setResponse(URLEncoder.encode(jsonObject.toString(),"utf-8"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return result;
                }
            });
    }

    /**
     * 获取图书节列表
     */
    public static void bookChapter(String token,String sectionId, final Handler handler, final String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0003000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
//        argsMap.put("bookId",bookId);
        argsMap.put("sectionId",sectionId);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_BOOK_CHAPTER));
    }

    /**
     * 获取图书特效数据
     */
    public static void bookMedia(String token,String isbn,String mediaId,String type, final Handler handler, final String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0008000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("token",token);
        argsMap.put("isbn",isbn);
        argsMap.put("type",type);
        argsMap.put("mediaId",mediaId);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_BOOK_MEDIA));
    }

    /**
     * 获取图书作者简介
     */
    public static void getBookAuthor(String token,String isbn,String authorId, final Handler handler, final String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0007000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("token",token);
        argsMap.put("isbn",isbn);
        argsMap.put("authorId",authorId);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_AUTHOR_PROFILE));
    }
    /**
     * 获取段落数据
     */
    public static void getBookParagraph(String token,String isbn,String authorId, final Handler handler, final String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0009000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("token",token);
        argsMap.put("isbn",isbn);
        argsMap.put("paragraphId",authorId);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_BOOK_PARAGRAPH));
    }

    /**
     * 获取支付订单信息
     */
    public static void getOrderInfo(String token,String uuid,String bookId, final String payType,String amount,String rechargeId,
        final String rechargeType,String discountId, final Handler handler, final String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0301000");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("platform",Constant.PLATFORM);//android
        argsMap.put("uuid",uuid);
        argsMap.put("token",token);
        argsMap.put("rechargeId",rechargeId);
        argsMap.put("bookId",bookId);
        argsMap.put("amount",amount);
        argsMap.put("payType",payType);
        argsMap.put("discountId",discountId);
        argsMap.put("terminalType","android");
        argsMap.put("DeviceType",Constant.DeviceType);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
//        String url = "http://192.168.8.162:5005/bus/services";
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.PAYMENT){
                @Override
                public Result parseResponse(Result result) {
                    try {
                        String paymentResponse = URLDecoder.decode(result.getResponse(),"utf-8");
                        Logger.i(paymentResponse);
                        JSONObject payObject = new JSONObject(paymentResponse);
                        String payObjectString = payObject.getString("serviceResult");
                        JSONObject payResultObject = new JSONObject(payObjectString);
                        String payFlag = payResultObject.getString("flag");
                        if(TextUtils.equals("true",payFlag)) {
                            //支付宝
                            JSONObject payResult = payResultObject.getJSONObject("result");
                            Payment payment = new Payment();
                            if (TextUtils.equals("1",payType)){
                                //支付宝
                                payment.setPartner(payResult.getString("partner"));
                                payment.setSeller_id(payResult.getString("seller_id"));
                                payment.setOut_trade_no(payResult.getString("out_trade_no"));
                                payment.setSubject(payResult.getString("subject"));
                                payment.setBody(payResult.getString("body"));
                                payment.setTotal_fee(payResult.getString("total_fee"));
                                payment.setNotify_url(payResult.getString("notify_url"));
                                payment.setService(payResult.getString("service"));
                                payment.setPayment_type(payResult.getString("payment_type"));
                                payment.set_input_charset(payResult.getString("_input_charset"));
                                payment.setIt_b_pay(payResult.getString("it_b_pay"));
                                payment.setReturn_url(payResult.getString("return_url"));
                                payment.setSign(payResult.getString("sign"));

                                payment.setPayType(payType);
                                payment.setRechargeType(rechargeType);//0:购书,1充值
                                result.setData(payment);
                            }else {
                                //微信
                                payment.setSign(payResult.getString("sign"));
                                payment.setTimestamp(payResult.getString("timestamp"));
                                payment.setNoncestr(payResult.getString("noncestr"));
                                payment.setPartnerid(payResult.getString("partnerid"));
                                payment.setTradeNo(payResult.getString("tradeNo"));
                                payment.setPrepayid(payResult.getString("prepayid"));
                                payment.setPkg(payResult.getString("package"));
                                payment.setAppid(payResult.getString("appid"));
//                                payment.setTradeType(payResult.getString("tradeType"));
                                payment.setTradeType(rechargeType);
                                payment.setPayType(payType);
                                payment.setRechargeType(rechargeType);//支付类型：0:购书,1充值
                                result.setData(payment);
                            }
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                    return super.parseResponse(result);
                }
            });
    }

    /**
     * 确认支付订单
     */
    public static void confirmTrade(String token,String uuid,String outTradeNo,String tradeType, String status,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0301100");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",token);
        argsMap.put("uuid",uuid);
        argsMap.put("outTradeNo",outTradeNo);
        argsMap.put("tradeType",tradeType);
        argsMap.put("status",status);
        argsMap.put("token",token);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.CONFIRM_TRADE));
    }

    /**
     * 上传用户个人资料
     */
    public static void updateCustomerInfo(User user,String key, String value, Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0011000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("accountId",user.getId());
        argsMap.put("key",key);
        argsMap.put("value",value);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.UPDATE_CUSTOMER_INFO));
    }

  /**
   * 下载书籍成功后进行统计记录
   * @param user 用户
   * @param bookId bookId
   * @param callback callback
   */
    public static void updateDownloadCompleted(User user,String bookId,Callback callback){
        /*
        {serviceModule:"BS-Service",serviceNumber:'0201300',token: "", args:{
       uuid:"1",bookId:"",platform:""
    }};
         */
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0201300");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("uuid",user.getId());
        argsMap.put("token",user.getToken());
        argsMap.put("bookId",bookId);
        argsMap.put("platform",Constant.PLATFORM);//0:ios,   1:anroid,   2:windows
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(callback);
    }

  /**
   获取用户信息
   */
    public static void getCustomerInfo(User user,Handler handler,String sn){
        /*
        {serviceModule:"BS-Service",serviceNumber:'0201300',token: "", args:{
       uuid:"1",bookId:"",platform:""
    }};
         */
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0010000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_CUSTOMER_INFO));
    }
    /**
        消费记录
     */
    public static void getRechargeAndConsumptionList(User user,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0301400");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_RECHARGE_CONSUMPTION_LIST));
    }
    /**
    优惠券
     */
    public static void getCouponList(User user,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0501000");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.GET_COUPON_LIST));
    }
    /**
        激活验证码
     */
    public static void postVerificationCode(User user,String code,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0510000");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("code",code);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.POST_VERIFICATION_CODE));
    }

    /**
     重置密码
     */
    public static void postResetPassword(User user,String password,String newPassword,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0014000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("password",password);
        argsMap.put("newPwd",newPassword);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.POST_RESET_PASSWORD));
    }

    /**
     燃烧token
     */
    public static void burnHumanToken(User user,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","burnHumanToken");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,""));
    }

    /**
     版本更新检查
     */
    public static void checkVersion(User user,String version,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0020000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("token",user.getToken());
        argsMap.put("version",version);
        argsMap.put("TerminalType",Constant.TerminalType);
        argsMap.put("DeviceType", Constant.DeviceType);
        argsMap.put("platform",Constant.Device);
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.CMD_CHECK_UPDATE){
                @Override
                public Result parseResponse(Result result) {
                    String res = result.getResponse();
                    try {
                        String response = URLDecoder.decode(res,"utf-8");
                        Logger.i(response);
                        JSONObject resObject = new JSONObject(response);
                        String opFlag = resObject.getString("opFlag");
                        if(TextUtils.equals(opFlag,"true")){
                            JSONObject serviceObject = resObject.getJSONObject("serviceResult");
                            String flag = serviceObject.getString("flag");
                            if(TextUtils.equals(flag,"true")){
                                JSONObject resultObject = serviceObject.getJSONObject("result");
                                Update update = new Update();
                                if(resultObject.has("path")){
                                    String path = resultObject.getString("path");
                                    update.setPath(path);
                                }
                                if(resultObject.has("action")){
                                    String action = resultObject.getString("action");
                                    update.setAction(action);
                                }
                                if(resultObject.has("introduce")){
                                    String introduce = resultObject.getString("introduce");
                                    update.setIntroduce(introduce);
                                }
//                                update.setAction("force");
//                                update.setPath("http://ebook.imed.org.cn/imed/bookDir/imed01.jc03_v1.0.0/imed01.jc03_v1.0.0.1_android/75ec704982bc4246b5a09616efe9dfac.med.enc");
//                                update.setIntroduce("书包3.0\n测试");
                                result.setData(update);
                                return result;
                            }
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                    result.setSuccess(false);
                    return super.parseResponse(result);
                }
            });
    }

    /**
     意见反馈
     */
    public static void postFeedback(User user,String title,String content,Handler handler,String sn){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0015000");
        serviceMap.put("serviceModule","imed");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("title",title);
        argsMap.put("content",content);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,Constant.POST_FEEDBACK));
    }

    /**
     获取图书内容
     */
    public static void getBookDetail(User user,String bookId,Handler handler,String sn,String command){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0201500");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("TerminalType",Constant.TerminalType);
        serviceMap.put("DeviceType", Constant.DeviceType);
        serviceMap.put("token",user.getToken());
        argsMap.put("id",bookId);
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,command));
    }
    /**获取书架轮播图*/
    public static void getBookAd(User user, Handler handler, String sn,String command){
        Map<String,Object> serviceMap = new ArrayMap<>();
        Map<String,String> argsMap = new ArrayMap<>();
        serviceMap.put("serviceNumber","0203300");
        serviceMap.put("serviceModule","BS-Service");
        serviceMap.put("token",user.getToken());
        argsMap.put("token",user.getToken());
        serviceMap.put("args",argsMap);
        Gson gson = new Gson();
        String result = gson.toJson(serviceMap);
        Logger.i(result);
        OkHttpUtils.postString()
            .url(AppConfig.HOST)
            .content(result)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            .build()
            .execute(new ResultCallback(handler,sn,command));
    }
}
