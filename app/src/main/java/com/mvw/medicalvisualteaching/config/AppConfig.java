package com.mvw.medicalvisualteaching.config;

/**
 * url Created by a on 2016/11/21.
 */
public class AppConfig {
  //  正式环境
//    public static final String HOST = "https://services2.mvwchina.com/services";//总线
//    public final static String BOOK_SHELF_URL = "https://wap.imed.org.cn/phone/index.html";//书架
//    public final static String BOOK_CITY_URL = "https://mall.imed.org.cn/ui/phone/index.html?platform=1&resourceType=0";//书城
//    public final static String COURSE_URL = "https://mall.imed.org.cn/course/phone/course.html";//教程
//    public final static String EXAM_URL = "https://exam.mvwchina.com/pc/student/student.html?platform=ebook";//考试
//    public final static String DATABASE_URL = "https://mshuju.mvwchina.com/?app_type=imed";//数据库




  public final static String BOOK_ONLINE_READ_URL = "https://oss-cn-beijing.aliyuncs.com/mvw-imed3/mvw_imed_book/";
  public final static String ONLINE_URL = "https://wap.imed.org.cn/";

  /**
   * 隐私协议
   */
  public final static String PRIVACY_POLICY = "http://www.imed.org.cn/xieyi.html";
  //本地跳转界面地址
  public final static String LOCAL_URL = "";


  //3.0以后测试地址
  public static final String HOST = "http://60.205.209.147:5205/services";//总线
  public final static String BOOK_SHELF_URL = "http://47.94.206.185:2025/phone/index.html";//书架
  public final static String BOOK_CITY_URL = "http://mvw-testing.oss-cn-beijing.aliyuncs.com/mall/ui/phone/index.html?platform=1&resourceType=0";//书城
  public final static String COURSE_URL = "http://mvw-testing.oss-cn-beijing.aliyuncs.com/mall/course/phone/course.html";//教程
  public final static String EXAM_URL = "http://47.94.206.185:3003/pc/student/student.html?platform=ebook";//考试调试地址
  public final static String DATABASE_URL = "https://mshuju.mvwchina.com/?app_type=imed";//数据库地址


//   public final static String DATABASE_URL = "http://192.168.8.195:8020/MKB/ui/phone-imed/index.html";//数据库地址
//  public final static String EXAM_URL = "http://192.168.8.43:8020/pc/student/student.html?platform=ebook";//调试前后台切换
//  public final static String BOOK_SHELF_URL = "http://192.168.8.164:8088/IMed3/ui/phone/index.html";//见伟电脑书架
// public final static String BOOK_CITY_URL = "http://192.168.8.162:8091/book/ui/phone/index.html?platform=1&resourceType=0";//书城
}
