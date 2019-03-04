package com.mvw.medicalvisualteaching.fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.exception.FileDownloadHttpException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.User;
import com.mvw.medicalvisualteaching.bean.UserBook;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.BookDao;
import com.mvw.medicalvisualteaching.db.dao.BookDao.Properties;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao;
import com.mvw.medicalvisualteaching.service.BookDownloadService;
import com.mvw.medicalvisualteaching.service.BookUpdateService;
import com.mvw.medicalvisualteaching.utils.DataUtil;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.HttpUtils;
import com.mvw.medicalvisualteaching.utils.SPUtil;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkView;

/**
 * 书架
 */
@SuppressLint("ValidFragment")
public class BookcaseFragment extends BaseFragment {

    public static final String START_DOWNLOAD_ACTION = "com.mvw.nationalmedicalPhone.startDownloadReceiver";
    public static final String STOP_DOWNLOAD_ACTION = "com.mvw.nationalmedicalPhone.stopDownloadReceiver";
    public static final String CANCEL_DOWNLOAD_ACTION = "com.mvw.nationalmedicalPhone.cancelDownloadReceiver";
    public static final String DELETE_BOOK_ACTION = "com.mvw.nationalmedicalPhone.deleteBookReceiver";
    public static final String UPDATE_BOOK_STATE_ACTION = "com.mvw.nationalmedicalPhone.updateBookStateReceiver";
    public static final String REFRESH_BOOKCASE_ACTION = "com.mvw.nationalmedicalPhone.refreshBookcaseReceiver";
    public static final String REFRESH_OBTAINED_ACTION = "com.mvw.nationalmedicalPhone.refreshObtainedReceiver";
    public static final String REFRESH_BOOKSHELF_ACTION = "com.mvw.nationalmedicalPhone.refreshBookShelfReceiver";
    public static final String NET_WORK_STATE="com.mvw.nationalmedicalPhone.networkstate";
    //更新书籍
    public static final String START_UPDATE_BOOK_ACTION = "com.mvw.nationalmedicalPhone.startUpdateBookReceiver";
    private BookDao bookDB;
    private UserBookDao userBookDB;
    /**服务连接*/
    private ServiceConnection connBookDownload;
    private ServiceConnection connBookUpdate;
    /** 下载服务*/
    public BookDownloadService downloadService;
    public BookUpdateService updateService;
    /**书籍状态广播接收*/
    private BookStateReceiver bookStateReceiver;
    private Gson gson;
    public BookcaseFragment(XWalkView xWalkView) {
        super(xWalkView);
    }
    public BookcaseFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    @Override
    protected void initData() {
        super.initData();
        bindDownloadService();
        bindUpdateService();
        bookDB = GreenDaoHelper.getDaoSession().getBookDao();
        userBookDB = GreenDaoHelper.getDaoSession().getUserBookDao();
        gson = new Gson();
    }
    @Override
    protected void initView(View layout) {
        super.initView(layout);
        //注册广播
        bookStateReceiver = new BookStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(START_DOWNLOAD_ACTION);
        intentFilter.addAction(STOP_DOWNLOAD_ACTION);
        intentFilter.addAction(CANCEL_DOWNLOAD_ACTION);
        intentFilter.addAction(DELETE_BOOK_ACTION);
        intentFilter.addAction(REFRESH_BOOKCASE_ACTION);
        intentFilter.addAction(REFRESH_BOOKSHELF_ACTION);
        intentFilter.addAction(REFRESH_OBTAINED_ACTION);
        intentFilter.addAction(START_UPDATE_BOOK_ACTION);
        intentFilter.addAction(NET_WORK_STATE);
        activity.registerReceiver(bookStateReceiver,intentFilter);
    }


    @Override
    public void resume(){
        super.resume();
        Logger.i(" BookcaseFragment on resume ");
        if(activity != null){
            FileUtils.saveLog(activity,"",Constant.ACTION_TYPE_START,url);
        }
    }
    @Override
    public void pause(){
        Logger.i(" BookcaseFragment---on Pause ");
        super.pause();
        if(activity != null) {
            FileUtils.saveLog(activity, "", Constant.ACTION_TYPE_END, url);
        }
    }
    @Override
    protected void loadUrl(){
        super.loadUrl();
        url = AppConfig.BOOK_SHELF_URL;
     //   url = StringUtils.createUrl(url,null,activity);
        String path1 = "file:///android_asset/";
        String path2 = "file:///storage/emulated/0/Android/data/com.mvw.nationalmedicalPhone/files/";
        mXwalkView.setOriginAccessWhitelist(url,new String[]{path1,path2});
        mXwalkView.loadUrl(url);
        activity.showWaitDialog();
        Logger.i("BookcaseFragment -webview------"+mXwalkView.toString());
    }

    /**
     * 绑定下载服务
     */
    private void bindDownloadService() {
        if (connBookDownload != null) {
            return;
        }
        Intent service = new Intent(activity, BookDownloadService.class);
        connBookDownload = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BookDownloadService.DownloadBinder downloadBinder = (BookDownloadService.DownloadBinder) service;
                downloadService = downloadBinder.getService();
                downloadService.addOnDownloadListener(downloadListener);
            }
        };
        activity.startService(service);
        activity.bindService(service, connBookDownload, 0);
    }

    /**
     * 绑定书籍更新服务
     */
    private void bindUpdateService() {
        if (connBookUpdate != null) {
            return;
        }
        Intent service = new Intent(activity, BookUpdateService.class);
        connBookUpdate = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BookUpdateService.UpdateBinder downloadBinder = (BookUpdateService.UpdateBinder) service;
                updateService = downloadBinder.getService();
                updateService.addOnDownloadListener(updateListener);
            }
        };
        activity.startService(service);
        activity.bindService(service, connBookUpdate, 0);
    }

    private String taskFlag;
    BookDownloadService.BookDownloadListener downloadListener = new BookDownloadService.BookDownloadListener() {

        @Override
        public void downloadStart(BaseDownloadTask task) {
            Book taskTag = (Book) task.getTag();
            saveToLocal(taskTag,task,Constant.DOWNLOAD_LOADING);
        }

        @Override
        public void downloadCompleted(BaseDownloadTask task) {
            Logger.e("下载完成，成功");
            Logger.e(task.getFilename());
            Logger.e(task.getPath());
            Logger.e(task.getTargetFilePath());
            Book taskTag = (Book) task.getTag();
            if(!TextUtils.equals(Constant.TEXT_BOOK,taskTag.getTextbook())){
                //pdf直接完成
                String state = Constant.BOOK_COMPLETED;
                downloadCallWeb(taskTag,task,state);
                saveToLocal(taskTag,task,state);
            }
            DataUtil.sendBookState(activity,taskTag.getIsbn(),Constant.BOOK_COMPLETED);
            //通知服务端下载完成
            User user = MyApplication.getUser();
            HttpUtils.updateDownloadCompleted(user, taskTag.getIsbn(), null);
        }
        @Override
        public void downloadError(BaseDownloadTask task, Throwable e) {
            if(e instanceof FileDownloadOutOfSpaceException){
                Toast.makeText(activity, R.string.download_out_of_space,Toast.LENGTH_SHORT).show();
            }else if(e instanceof UnknownHostException || e instanceof FileDownloadHttpException){
                //Toast.makeText(activity,R.string.http_exception_error,Toast.LENGTH_SHORT).show();
            }else {
               // Toast.makeText(activity, R.string.http_exception_error,Toast.LENGTH_SHORT).show();
            }
            if(e != null){
                e.printStackTrace();
            }
            Book taskTag = (Book) task.getTag();
            saveToLocal(taskTag,task,Constant.DOWNLOAD_STOP);
            //下载失败
            downloadCallWeb(taskTag,task,Constant.DOWNLOAD_STOP);
            DataUtil.sendBookState(activity,taskTag.getIsbn(),Constant.DOWNLOAD_STOP);
        }

        @Override
        public void downloadWarn(BaseDownloadTask task) {
        }

        @Override
        public void downloadPending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Logger.e("开始进入等待下载。。。");
            Book taskTag = (Book) task.getTag();
            saveToLocal(taskTag,task,Constant.DOWNLOAD_PENDING);
        }

        @Override
        public void downloadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Book taskTag = (Book) task.getTag();
            Logger.i("soFarBytes-----"+soFarBytes);
            if(!taskTag.getIsbn().equals(taskFlag)){
                taskFlag = taskTag.getIsbn();
                Logger.e("开始下载了。。。");
            }
            if(downloadService != null && downloadService.getTaskList() != null
                && downloadService.getTaskList().containsKey(taskTag.getIsbn())){
                downloadCallWeb(taskTag,task,Constant.DOWNLOAD_LOADING);
            }
        }

        @Override
        public void downloadPaused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Logger.e("进入暂停下载");
            //暂停下载
            Book taskTag = (Book) task.getTag();
            Object tag = task.getTag(Constant.DOWNLOAD_BOOK_FLAG);
            int flag = 0;
            if(tag != null){
                flag = (int) tag;
            }
            if( flag != Constant.CANCEL_DOWNLOAD_BOOK_FLAG){
                saveToLocal(taskTag,task,Constant.DOWNLOAD_STOP);
                DataUtil.sendBookState(activity,taskTag.getIsbn(),Constant.DOWNLOAD_STOP);
            }
        }

        @Override
        public void upZipStart(Book localBook) {
            Logger.i("开始解压");
            localBook.setDownloadState(Constant.UPZIP_LOADING);
            bookDB.update(localBook);
            upZipCallWeb(localBook,Constant.UPZIP_LOADING);
        }

        @Override
        public void upZipProgress(Book localBook, int soFar, int total) {
//            Logger.i("解压中  "+soFar);
        }

        @Override
        public void upZipCompleted(Book localBook) {
            Logger.i("解压完成");
            localBook.setDownloadState(Constant.BOOK_COMPLETED);
            bookDB.update(localBook);
            //记录当前下载完成的书籍的版本，用于以后判断是否需要更新书籍
            upZipCallWeb(localBook,Constant.BOOK_COMPLETED);
            DataUtil.updateUBPatchVersion(userBookDB,localBook.getIsbn(),localBook.getPatchVersion());
            String tempName = Utils.md5Digest(localBook.getPath());
            File tempFile = new File(localBook.getDownloadPath()+File.separator+tempName);
            FileUtils.deleteFile(tempFile);//解压成功删除下载zip文件
        }

        @Override
        public void upZipError(final Book localBook) {
            Logger.i("解压失败");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    localBook.setDownloadState(Constant.UPZIP_FAILED);
                    bookDB.update(localBook);
                    upZipCallWeb(localBook,Constant.UPZIP_FAILED);
                    String tempName = Utils.md5Digest(localBook.getPath());
                    File tempFile = new File(localBook.getDownloadPath()+File.separator+tempName);
                    FileUtils.deleteFile(tempFile);//解压失败删除下载zip文件
                }
            });
        }
    };

    BookUpdateService.BookDownloadListener updateListener = new BookUpdateService.BookDownloadListener() {

        @Override
        public void downloadStart(BaseDownloadTask task) {

        }
        @Override
        public void downloadCompleted(BaseDownloadTask task) {
            Logger.e("更新 下载完成，成功");
            Logger.e(task.getFilename());
            Logger.e(task.getPath());
            Logger.e(task.getTargetFilePath());
        }
        @Override
        public void downloadError(BaseDownloadTask task, Throwable e) {
            //下载失败，给用户提示更新失败
            Book book = (Book) task.getTag();
            downloadCallWeb(book,task,Constant.DOWNLOAD_STOP);
        }
        @Override
        public void downloadWarn(BaseDownloadTask task) {

        }
        @Override
        public void downloadPending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Logger.e("更新 开始进入等待下载。。。");
            Book book = (Book) task.getTag();
            downloadCallWeb(book,task,Constant.DOWNLOAD_PENDING);
        }

        @Override
        public void downloadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Book taskTag = (Book) task.getTag();
            if(updateService != null && updateService.getTaskList() != null
                && updateService.getTaskList().containsKey(taskTag.getIsbn())){

                downloadCallWeb(taskTag,task,Constant.DOWNLOAD_LOADING);
            }

        }

        @Override
        public void downloadPaused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Logger.e("更新 进入暂停下载");
            //暂停下载
            Book book = (Book) task.getTag();
            downloadCallWeb(book,task,Constant.DOWNLOAD_STOP);
        }

        @Override
        public void upZipStart(Book localBook) {
            Logger.i("更新 开始解压");
            downloadCallWeb(localBook,null,Constant.UPZIP_LOADING);
        }

        @Override
        public void upZipProgress(Book localBook, int soFar, int total) {
//            Logger.i("解压中  "+soFar);
        }

        @Override
        public void upZipCompleted(Book localBook) {
            Logger.i("更新 解压完成");
            localBook.setIsUpdate("0");
            upZipCallWeb(localBook,Constant.BOOK_COMPLETED);
            DataUtil.updateUBPatchVersion(userBookDB,localBook.getIsbn(),localBook.getPatchVersion());
            String tempName = Utils.md5Digest(localBook.getPatchPath());
            File tempFile = new File(localBook.getDownloadPatchPath()+File.separator+tempName);
            FileUtils.deleteFile(tempFile);//解压成功删除下载zip文件
        }

        @Override
        public void upZipError(final Book localBook) {
            Logger.i("解压失败");
            //解压失败 ，给用户提示升级失败
            downloadCallWeb(localBook,null,Constant.UPZIP_FAILED);
            String tempName = Utils.md5Digest(localBook.getPatchPath());
            File tempFile = new File(localBook.getDownloadPatchPath()+File.separator+tempName);
            FileUtils.deleteFile(tempFile);//解压失败删除下载zip文件

        }
    };

  /**
   * 本地记录书籍的下载状态和进度
   * @param taskTag 书籍
   * @param task 下载书籍任务
   * @param state 书籍当前状态
   */
    private void saveToLocal(final Book taskTag, final BaseDownloadTask task, final String state){
        new Thread(){
            @Override
            public void run() {
                taskTag.setDownloadState(state);
                taskTag.setTotal(task.getSmallFileTotalBytes());
                taskTag.setDownloaded(task.getSmallFileSoFarBytes());
                bookDB.insertOrReplace(taskTag);
                DataUtil.saveUserBook(userBookDB,taskTag,state,taskTag.getBuyStatus());
                if(Constant.BOOK_COMPLETED.equals(state)){
                    DataUtil.saveUserBookOrder(taskTag.getIsbn());
                }
            }
        }.start();
    }

  /**
   * 下载过程中通知web
   * @param book 通知目标书籍
   * @param task 书籍任务
   * @param state 当前书籍状态
   */
    private void downloadCallWeb(Book book, BaseDownloadTask task, String state){
        if(task != null){
            book.setTotal(task.getSmallFileTotalBytes());
            book.setDownloaded(task.getSmallFileSoFarBytes());
        }
        book.setDownloadState(state);
        String bookJson = gson.toJson(book);
        Logger.i(bookJson);
        try {
            bookJson = URLEncoder.encode(bookJson,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        appCallWeb(Constant.MSG_UPDATE_BOOK_STATE,bookJson);
    }

  /**
   * 解压过程中通知web
   * @param book 书籍
   * @param state 当前解压状态
   */
    private void upZipCallWeb(Book book,String state){
        DataUtil.saveUserBook(userBookDB,book,state,book.getBuyStatus());
        downloadCallWeb(book,null,state);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mXwalkView != null) {
            mXwalkView.onDestroy();
        }
        if (connBookDownload != null){
            if(downloadService != null){
                downloadService.stopAllDownloadTask();
            }
            activity.unbindService(connBookDownload);
        }
        if(connBookUpdate != null){
            if(updateService != null){
                updateService.stopAllDownloadTask();
            }
            activity.unbindService(connBookUpdate);
        }
        if(bookStateReceiver != null){
            activity.unregisterReceiver(bookStateReceiver);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mXwalkView != null) {
            mXwalkView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void appCallWeb(String sn,String content) {
        super.appCallWeb(sn,content);
    }

    /**
     * 书籍的状态接收
     */
    class BookStateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //要下载的目标图书
            String extraString = intent.getStringExtra("book");
            String from = intent.getStringExtra("from");
            String nonWifi = intent.getStringExtra("nonWifi");
            Logger.e("ACTION = "+intent.getAction());
            Logger.e("extraString = "+extraString);
           switch (intent.getAction()){
               case START_DOWNLOAD_ACTION:
                   //下载  bookJson
                   downloadBook(extraString,from,nonWifi);
                   break;
               case STOP_DOWNLOAD_ACTION:
                   if (!TextUtils.isEmpty(from)&&from.equals("net_stop")){
                       isStopTask();
                   }else{
                       //暂停下载
                       stopDownloadBook(extraString);
                   }
                   break;
               case DELETE_BOOK_ACTION:
                   //删除书籍
                    deleteBook(extraString);
                   break;
               case CANCEL_DOWNLOAD_ACTION:
                   //取消下载
                   cancelDownload(extraString);
                   break;
               case REFRESH_BOOKCASE_ACTION:
                   if (extraString.equals(Constant.MSG_REFRESH)){ //全部刷新
                       appCallWeb(Constant.MSG_REFRESH,null);
                       String path1 = "file:///android_asset/";
                       String path2 = "file:///storage/emulated/0/Android/data/com.mvw.nationalmedicalPhone/files/";
                       mXwalkView.setOriginAccessWhitelist(url,new String[]{path1,path2});
                       mXwalkView.reload(XWalkView.RELOAD_NORMAL);
                   }else{
                       //刷新书架
                       refresh(Constant.MSG_REFRESH_TYPE_BOOKSHELF);
                   }
                   break;
               case REFRESH_OBTAINED_ACTION:
                   //刷新已购
                   if(from != null){
                       //往已购添加添加单个图书
                       Book book = gson.fromJson(extraString,Book.class);
                       book.setDownloadState(Constant.UNDOWNLOAD);
//                       bookDB.insertOrReplace(book);
//                       DataUtil.saveUserBook(userBookDB,book,Constant.UNDOWNLOAD,book.getBuyStatus());
                       String bookJson = gson.toJson(book);
                       try {
                           bookJson = URLEncoder.encode(bookJson,"utf-8");
                       } catch (UnsupportedEncodingException e) {
                           e.printStackTrace();
                       }
                       appCallWeb(Constant.MSG_ADD_TO_OBTAINED_BOOK,bookJson);
                   }else {
                       refresh(Constant.MSG_REFRESH_TYPE_OBTAINED);
                   }
                   break;
               case START_UPDATE_BOOK_ACTION:
                   //开始更新教材
//                   updateBook(extraString);
                   break;
               case NET_WORK_STATE:
                   //有网络情况
                   appCallWeb("MsgNetworkState","");
                   break;
               case REFRESH_BOOKSHELF_ACTION://书架添加书籍
                   Book book = gson.fromJson(extraString,Book.class);
                   String bookJson = gson.toJson(book);
                   try {
                       bookJson = URLEncoder.encode(bookJson,"utf-8");
                   } catch (UnsupportedEncodingException e) {
                       e.printStackTrace();
                   }
                   appCallWeb(Constant.MSG_ADD_TO_BOOK_SHELF,bookJson);
                   break;
           }
        }
    }
    private void refresh(String type){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",type);
            Logger.i(jsonObject.toString());
            String response = jsonObject.toString();
            response = URLEncoder.encode(response,"utf-8");
            appCallWeb(Constant.MSG_REFRESH,response);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onKey(int keyCode, KeyEvent event) {
        super.onKey(keyCode, event);
        Logger.i("  onKey ===  javascript:Elf.AppCallWeb('"+Constant.MSG_GO_BACK+"')");
        mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+Constant.MSG_GO_BACK+"')",null);
    }

  /***
   * 更新教材
   */
  private void updateBook(Book book){
        //查询下载处理中的图书有没有目标图书
        Book dbLocalBook = bookDB.queryBuilder().where(Properties.Isbn.eq(book.getIsbn())).unique();
        //从已获得图书下载，书架没有这本书
        if(dbLocalBook != null){
            dbLocalBook.setIsUpdate(Constant.BOOK_UPDATE);
            dbLocalBook.setPatchPath(dbLocalBook.getPatchPath());
            dbLocalBook.setNonWifi(book.getNonWifi());
            if(TextUtils.isEmpty(dbLocalBook.getPatchPath())){
                Toast.makeText(activity,"没有该更新文件下载地址",Toast.LENGTH_SHORT).show();
                return;
            }
            //判断当前空间大小
            int storagePosition = SPUtil.getInstance(activity).getInt(Constant.BOOK_STORAGE_POSITION,1);
            downloadCallWeb(dbLocalBook,null,Constant.DOWNLOAD_PENDING);
//            DataUtil.saveUserBook(userBookDB,dbLocalBook,Constant.DOWNLOAD_PENDING,Constant.STATUS_BUY);
            if(TextUtils.equals(Constant.TEXT_BOOK, dbLocalBook.getTextbook())){
                //下载教材更新包
                dbLocalBook.setDownloadFile(dbLocalBook.getIsbn());
                dbLocalBook.setDownloadPatchPath(Utils.getSDCardPath(activity,storagePosition)+Constant.BOOK_PATH_TEXTBOOK_PATCH);
            }
            //更新book是否在4G下面能下载
            bookDB.insertOrReplace(dbLocalBook);
        }
        if(updateService != null && dbLocalBook != null){
            updateService.addDownloadTask(dbLocalBook);
        }
    }

    private void downloadBook(String bookJson,String from,String nonWifi){
        Book book = gson.fromJson(bookJson,Book.class);
        User user = MyApplication.getUser();
        UserBook ub = userBookDB.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+book.getIsbn())).unique();
        if (ub!=null){
            ub.setNonWifi(nonWifi);
        }
        if(ub != null && Constant.BOOK_COMPLETED.equals(ub.getState())){
            //更新书籍的下载
            updateBook(book);
        }else {
            //检查本地书籍信息
            String isbn = DataUtil.checkLocalBook(bookJson,activity,from);
            //查询下载处理中的图书有没有目标图书
            Book dbLocalBook = bookDB.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
            //保存nonWifi状态
            dbLocalBook.setNonWifi(nonWifi);
            bookDB.insertOrReplace(dbLocalBook);
            //正常书籍的下载
            //从已获得图书下载，书架没有这本书
            if(dbLocalBook != null){
                dbLocalBook.setIsUpdate("0");
                if(downloadService != null && downloadService.getTaskCount() >= 5){
                    Toast.makeText(activity,"最大支持5本书籍同时下载",Toast.LENGTH_SHORT).show();
                    downloadCallWeb(dbLocalBook,null,Constant.UNDOWNLOAD);
                    return;
                }
                if(TextUtils.isEmpty(dbLocalBook.getPath())){
                    Toast.makeText(activity,"没有该书籍下载地址",Toast.LENGTH_SHORT).show();
                    downloadCallWeb(dbLocalBook,null,Constant.UNDOWNLOAD);
                    return;
                }
                //判断当前空间大小
                int storagePosition = SPUtil.getInstance(activity).getInt(Constant.BOOK_STORAGE_POSITION,-1);
                String size = dbLocalBook.getSize();
                long bookSize = Long.parseLong(size);
                long availableSize = Utils.getSDCardAvailableSize(activity,storagePosition);
                if(bookSize*1024*1024*3>availableSize){
                    //要下载的书大小的三倍大于下载位置的空大小，提示空间不足
//                DataUtil.showWebToast(activity,"存储空间不足");
                    Toast.makeText(activity,"内存空间不足",Toast.LENGTH_SHORT).show();
                    downloadCallWeb(dbLocalBook,null,Constant.UNDOWNLOAD);
                    return;
                }
                downloadCallWeb(dbLocalBook,null,Constant.DOWNLOAD_PENDING);
                DataUtil.saveUserBook(userBookDB,dbLocalBook,Constant.DOWNLOAD_PENDING,Constant.STATUS_BUY);
                if(TextUtils.equals(Constant.TEXT_BOOK, dbLocalBook.getTextbook())){
                    //下载教材
                    dbLocalBook.setDownloadFile(dbLocalBook.getIsbn());
                    dbLocalBook.setDownloadPath(Utils.getSDCardPath(activity,storagePosition)+Constant.BOOK_PATH_TEXTBOOK);
                }else {
                    //下载pdf
                    dbLocalBook.setDownloadFile(dbLocalBook.getIsbn());
                    dbLocalBook.setDownloadPath(Utils.getSDCardPath(activity,storagePosition)+Constant.BOOK_PATH_PDF);
                }
                List<UserBook> userBooks = userBookDB.queryBuilder().where(UserBookDao.Properties.BIsbn.eq(dbLocalBook.getIsbn())).list();
                for(UserBook userBook : userBooks){
                    if(TextUtils.equals(userBook.getState(), Constant.BOOK_COMPLETED)){
                        //本地有下载成功该书籍的记录，不再进行下载流程
                        String targetPath = dbLocalBook.getDownloadPath()+File.separator+ dbLocalBook.getDownloadFile();
                        File targetFile = new File(targetPath);
                        if(targetFile.exists()){
                            //存在，下载完成，通知界面下载完成
                            Toast.makeText(activity,"下载完成",Toast.LENGTH_SHORT).show();
                            downloadCallWeb(dbLocalBook,null,Constant.BOOK_COMPLETED);
                            DataUtil.saveUserBook(userBookDB,dbLocalBook,Constant.BOOK_COMPLETED,Constant.STATUS_BUY);
                            DataUtil.updateUBPatchVersion(userBookDB,dbLocalBook.getIsbn(),userBook.getPatchVersion());
                            return;
                        }
                    }
                }
            }
            if(downloadService != null && dbLocalBook != null){
                downloadService.addDownloadTask(dbLocalBook);
            }
        }
    }

  /**
   * 暂停下载
   */
  private void stopDownloadBook(String isbn){
      Book localBook = bookDB.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
      User user = MyApplication.getUser();
      UserBook userBook = userBookDB.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
      if(userBook != null &&  Constant.BOOK_COMPLETED.equals(userBook.getState())){
          //更新下的暂停
          if(localBook != null){
              localBook.setIsUpdate(Constant.BOOK_UPDATE);
              downloadCallWeb(localBook,null,Constant.DOWNLOAD_STOP);
          }
          if(localBook != null && updateService != null){
              updateService.stopDownloadTask(localBook);
          }
      }else {
          //正常下载下的暂停
          if(localBook != null && userBook != null){
              downloadCallWeb(localBook,null,Constant.DOWNLOAD_STOP);
              DataUtil.saveUserBook(userBookDB,localBook,Constant.DOWNLOAD_STOP,userBook.getBuyStatus());
          }
          if(localBook != null && downloadService != null){
              downloadService.stopDownloadTask(localBook);
          }
      }
    }
    /**切换网络后根据用户下载选择判断*/
    private void isStopTask(){
        if (downloadService!=null){
            Map<String,BaseDownloadTask> map =downloadService.getTaskList();
            Set<Entry<String, BaseDownloadTask>> entrySet = map.entrySet();
            //将关系集合entrySet进行迭代，存放到迭代器中
            Iterator<Entry<String, BaseDownloadTask>> it2 = entrySet.iterator();
            while(it2.hasNext()){
                Map.Entry<String, BaseDownloadTask> me = it2.next();//获取Map.Entry关系对象me
                String key2 = me.getKey();//通过关系对象获取key
                Book book = bookDB.queryBuilder().where(Properties.Isbn.eq(key2)).unique();
                if (book!=null && !TextUtils.isEmpty(book.getNonWifi())&&book.getNonWifi().equals("1")){
                    downloadService.stopDownloadTask(book);
                    downloadCallWeb(book,null,Constant.DOWNLOAD_STOP);
                }
            }
        }
    }

  /**
   * 删除书籍
   */
  private void deleteBook(String isbn){
        Book localBook = bookDB.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
        if (localBook!=null &&downloadService != null){
            downloadService.stopDownloadTask(localBook);
        }
    }

  /**
   * 取消下载
   */
  private void cancelDownload(String isbn){
        final Book localBook = bookDB.queryBuilder().where(Properties.Isbn.eq(isbn)).unique();
      User user = MyApplication.getUser();
      UserBook userBook = userBookDB.queryBuilder().where(UserBookDao.Properties.Id.eq(user.getId()+"+"+isbn)).unique();
      if(userBook != null &&  Constant.BOOK_COMPLETED.equals(userBook.getState())) {
          //更新下的取消
          if(updateService != null && localBook != null){
              updateService.cancelDownloadTask(localBook);
          }
          //删除本地下载中的文件
          if(localBook != null){
              localBook.setIsUpdate(Constant.BOOK_UPDATE);
              downloadCallWeb(localBook,null,Constant.UNDOWNLOAD);
              new Thread(){
                  @Override
                  public void run() {
                      FileUtils.deleteBookPatch(localBook);
                  }
              }.start();
          }
      }else {
          //正常下载下的取消
          if(downloadService != null && localBook != null){
              downloadService.cancelDownloadTask(localBook);
          }
          //删除本地下载中的文件
          if(localBook != null && userBook != null){
              downloadCallWeb(localBook,null,Constant.UNDOWNLOAD);
              DataUtil.saveUserBook(userBookDB,localBook,Constant.UNDOWNLOAD,userBook.getBuyStatus());
              new Thread(){
                  @Override
                  public void run() {
                      FileUtils.deleteBook(localBook);
                  }
              }.start();
          }
      }
  }

}
