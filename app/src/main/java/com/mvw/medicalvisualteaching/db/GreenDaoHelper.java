package com.mvw.medicalvisualteaching.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.mvw.medicalvisualteaching.db.dao.BookDao;
import com.mvw.medicalvisualteaching.db.dao.DaoMaster;
import com.mvw.medicalvisualteaching.db.dao.DaoSession;
import com.mvw.medicalvisualteaching.db.dao.ExamDao;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao;
import com.mvw.medicalvisualteaching.db.dao.UserDao;
import com.orhanobut.logger.Logger;
import org.greenrobot.greendao.database.Database;

/**
 *
 * Created by zhaomengen on 2017/2/15.
 */

public class GreenDaoHelper {
  private static final String DB_NAME = "NationalMedicalPhone3.0";//数据库名称
  private static final String PASSWORD = "123456";
  private static DaoSession mDaoSession;
  //本地下载书籍数据库
  private static Database dbBook;
  private static SQLiteDatabase databaseBook;
  private static DaoSession mDaoSessionBook;
  private static android.database.sqlite.SQLiteDatabase sDatabase;

  /**
   * 初始化greenDao，这个操作建议在Application初始化的时候添加；
   */
  public static void initDatabase(Context context) {
    // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
    // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
    MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(context, DB_NAME, null);
//    Database db = mHelper.getEncryptedWritableDb(PASSWORD);
    Database db = mHelper.getWritableDb();
    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
    DaoMaster mDaoMaster = new DaoMaster(db);
    mDaoSession = mDaoMaster.newSession();
  }
  public static void initLocalBookDatabasePassword(Context context,String path,String password){

    databaseBook = SQLiteDatabase.openDatabase(path, null,Context.MODE_PRIVATE);
    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
    DaoMaster mDaoMasterBook = new DaoMaster(databaseBook);
    mDaoSessionBook = mDaoMasterBook.newSession();
  }

  public static void initLocalBookDatabase(String path){
    sDatabase = android.database.sqlite.SQLiteDatabase
        .openDatabase(path,null,0);
  }


  public static DaoSession getDaoSession() {
    return mDaoSession;
  }
  public static DaoSession getDaoSessionBook() {
    return mDaoSessionBook;
  }

  public static android.database.sqlite.SQLiteDatabase getLocalBookDatabase() {
    return sDatabase;
  }

  public static void closeLocalBookDatabase(){
    if(dbBook != null){
      dbBook.close();
    }
    if(databaseBook != null){
      databaseBook.close();
      databaseBook = null;
    }
    if(sDatabase != null){
      sDatabase.close();
    }
  }

  private static class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
    // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
    MySQLiteOpenHelper(Context context, String name, CursorFactory factory) {
      super(context, name, factory);
    }

    @Override
    public void onCreate(Database db) {
      super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
      Logger.i("on update-----oldVersion---"+oldVersion+"----newVersion----"+newVersion);
      if (oldVersion < 3){
        MigrationHelper.getInstance().migrate(db,UserBookDao.class, BookDao.class, UserDao.class);
        ExamDao.createTable(db,true);
        Logger.i("升级数据库");
      }
    }
  }
}
