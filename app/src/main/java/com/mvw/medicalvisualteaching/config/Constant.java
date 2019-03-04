package com.mvw.medicalvisualteaching.config;

/**
 */
public class Constant {
//-------- 设备区分
    /*
 "TerminalType": "A",
    终端类型
    A——browser
    B——ios
    C——android
 */
    public static final String TerminalType = "C";
    /*
    "DeviceType": "",
        终端类型C
        1——phone
        2——pad
     */
    public static final String DeviceType = "1";
    /*
    clienttype: 代表APP类型  2:androidPhone  4:androidPad
     */
    public static final String ClientType = "2";
    /*
    埋点类型：  安卓手机：androidPhone  安卓平板：androidPad
     */
    public static final String Device = "androidPhone";

//-------SharedPreferences存储记录key
    //是否显示引导页
    public static final String GUIDE_FLAG = "guide_flag";
    //下载位置记录
    public static final String BOOK_STORAGE_POSITION = "book_storage_position";
    //游客token
    public static final String VISITOR_TOKEN = "visitor";
    //下载路径
    public static final String BOOK_PATH = "book_path";
    public static final String BOOK_PATH_PDF = "/book3.0/pdf";
    public static final String BOOK_PATH_TEXTBOOK = "/book3.0/.textbook";
    public static final String BOOK_PATH_TEXTBOOK_PATCH = "/book3.0/textbook/patch";
    public static final String LOG_PATH = "/log";

    //本地下载书籍的文件路径 2d279ef53738c6fe：压缩文件解压文件名称
   // public static final String LOCAL_BOOK_FOLDER = "2d279ef53738c6fe";
    //本地下载书籍中，书籍db文件的md5文件名称
    public static final String LOCAL_BOOK_DB_NAME = "69066658a16532b1";

    //图书的状态
    /**未下载*/
    public static final String UNDOWNLOAD = "0";
    /**等待*/
    public static final String DOWNLOAD_PENDING = "1";
    /**下载中*/
    public static final String DOWNLOAD_LOADING = "2";
    /**暂停*/
    public static final String DOWNLOAD_STOP = "3";
    /**失败*/
    public static final String DOWNLOAD_FAILED = "4";
    /**解压中*/
    public static final String UPZIP_LOADING= "6";
    /**解压失败*/
    public static final String UPZIP_FAILED= "7";
    /**完成*/
    public static final String BOOK_COMPLETED= "8";
    /**更新中*/
//    public static final String BOOK_UPDATING= "9";
    /**更新失败*/
//    public static final String BOOK_UPADTE_FAILED= "10";

    public static final String TEXT_BOOK= "0";//0:教材  1:pdf
    public static final String PLATFORM = "1"; // 0:ios  1:android  2:pc
    public static final String TYPE_BUY = "0"; // 0:正常购买,1:免费书籍的下载(相当于购买),2:借阅
    public static final String TYPE_FREE = "1"; // 0:正常购买,1:免费书籍的下载(相当于购买),2:借阅
    public static final String TYPE_BORROW = "2"; // 0:正常购买,1:免费书籍的下载(相当于购买),2:借阅

    public static final String STATUS_NOT_BUY = "0"; // 购买状态 0:未购买,1:已购买(免费书籍为已购买)
    public static final String STATUS_BUY = "1"; // 购买状态 0:未购买,1:已购买(免费书籍为已购买)
    public static final String STATUS_BORROW = "2"; // 购买状态 0:未购买,1:已购买(免费书籍为已购买) 2:借阅

    public static final String STATUS_NOT_EXPIRED = "0"; // Expired;//是否已过期	0:未过期,1:已过期,2:已购买不过期
    public static final String STATUS_EXPIRED = "1"; // Expired;//是否已过期	0:未过期,1:已过期,2:已购买不过期

    public static final String BOOK_UPDATE = "1";//值为1时：教材需要升级

    public static final int DOWNLOAD_BOOK_FLAG = 100;
    public static final int CANCEL_DOWNLOAD_BOOK_FLAG = 200;
    public static final int PAUSE_DOWNLOAD_BOOK_FLAG = 300;

    /**MSG:发给H5 书籍的状态*/
    public static final String MSG_UPDATE_BOOK_STATE = "MsgUpdateBookState";
    /**MSG:发给H5 点击返回键*/
    public static final String MSG_GO_BACK = "MsgGoBack";
    /**MSG:发给H5 刷新界面数据*/
    public static final String MSG_REFRESH = "MsgRefresh";
    /**MSG:发给H5 显示Toast*/
    public static final String MSG_TOAST_INFO = "MsgToastInfo";
    /**MSG:发给H5 从上一个界面返回当前界面*/
    public static final String MSG_RE_BACK = "MsgReBack";

    /**MSG:发给H5 类型:刷新书架he已购界面*/
    public static final String MSG_REFRESH_TYPE_BOOKSHELF_OBTAINED = "0";
    /**MSG:发给H5 类型:刷新书架界面*/
    public static final String MSG_REFRESH_TYPE_BOOKSHELF = "1";
    /**MSG:发给H5 类型:刷新已购界面*/
    public static final String MSG_REFRESH_TYPE_OBTAINED = "2";

    /**MSG:发给H5 往书架界面添加书籍*/
    public static final String MSG_ADD_TO_BOOK_SHELF = "MsgAddToBookShelf";
    /**MSG:发给H5 往已购界面添加书籍*/
    public static final String MSG_ADD_TO_OBTAINED_BOOK = "MsgAddToObtainedBook";

    /** 微信上注册的APPID */
    public static final String AppID = "wxc77cddfc8a9b6d76";
    public static final String WXAppSecret = "d4624c36b6795d1d99dcf0547af5443d";
    public static final String QQAppID = "1104866883";
    public static final String QQAppKey = "RdJecCxCx1THul91";
    /**开启新界面*/
    public static final String CMD_OPEN_URL = "CmdOpenUrl";
    /**书城书籍购买成功*/
    public static final String CMD_PURCHASE_SUCCESS = "CmdBookPurchaseSuccess";
    /**显示加载框*/
    public static final String OPEN_LOADINGNG = "showLoading";
    /**打开PDF文件*/
    public static final String CMD_OPEN_PDF = "CmdOpenPDFBook";
    /**退出登录*/
    public static final String LOGOUT = "UserLogout";
    /**返回上一界面*/
    public static final String CMD_GO_BACK = "CmdGoBack";
    /**停止播放音频*/
    public static final String CMD_BACK_STOP = "CmdBackStop";
    /**选择存储位置*/
    public static final String GET_DOWNLOAD_LOCATION = "GetDownloadLocations";
    /**获取存储空间大小*/
    public static final String SET_DOWNLOAD_LOCATION = "PostSetDownloadLocation";
    /**拨打热线*/
    public static final String CMD_DIALING = "CmdDialing";
    /**分享*/
    public static final String CMD_SHARE_APP = "CmdShareAPP";
    /**考试系统的命令*/
    public static final String CMD_RETURN_HOME_PAGE = "ReturnHomePage";

    /**接口：书架*/
    public static final String GET_BOOK_SHELF_LIST = "GetBookShelfList";

    /**接口：删除书架图书*/
    public static final String CMD_DELETE_FROM_BOOKSHELF = "CmdDeleteFromBookShelf";

    /**接口：已获得图书*/
    public static final String GET_PURCHASE_BOOK_LIST = "GetPurchasedBookList";

    /**接口：签到*/
    public static final String SIGN = "sign";

    /**接口：获取签到状态*/
    public static final String SIGN_STATUS = "signedStatus";

    /**接口：跨书搜*/
    public static final String GET_CROSS_SEARCH_BOOK_LIST = "GetCrossSearchBookList";
    /**接口：跨书搜*/
    public static final String GET_CROSS_SEARCH_BOOK = "GetCrossSearchBook";
    /**接口：搜索图书列表*/
    public static final String GET_BOOK_SEARCH_LIST = "GetBookSearchList";
    /**接口：获取热门搜索关键字*/
    public static final String GET_HOT_WORD_LIST = "GetHotWordList";
    /**接口：书架下载图书*/
    public static final String CMD_DOWNLOAD_BOOK = "CmdDownloadBook";
    /**接口：书架更新图书*/
    public static final String CMD_UPDATE_BOOK = "CmdUpdateBook";
    /**接口：书城下载图书*/
    public static final String DOWNLOAD_BS_BOOK = "downloadBsBook";
    /**接口：图书更新验证*/
    public static final String GET_BOOK_VERSION = "getBookVersion";
    /**取消下载图书*/
    public static final String CMD_CANCEL_DOWNLOAD_BOOK = "CmdCancelDownloadBook";
    /**暂停下载图书*/
    public static final String CMD_PAUSE_DOWNLOAD_BOOK = "CmdPauseDownloadBook";
    /**检查版本*/
    public static final String CMD_CHECK_UPDATE = "CmdCheckUpdate";

    /**接口：图书列表*/
    public static final String GET_BOOK_CATELOG = "GetBookCatelog";
    public static final String GET_BOOK_CATELOG_LOCAL = "GetBookCatelogLocal";

    /**接口：图书章节列表*/
    public static final String GET_BOOK_CHAPTER = "GetBookChapter";
    public static final String GET_BOOK_CHAPTER_LOCAL = "GetBookChapterLocal";
    /**接口：图书章节内容多媒体*/
    public static final String GET_BOOK_MEDIA = "GetBookMedia";
    public static final String GET_BOOK_MEDIA_LOCAL = "GetBookMediaLocal";
    /**接口：作者内容*/
    public static final String GET_AUTHOR_PROFILE = "GetAuthorProfile";
    public static final String GET_AUTHOR_PROFILE_LOCAL = "GetAuthorProfileLocal";
    /**播放音频*/
    public static final String CMD_PLAY_AUDIO = "CmdPlayAudio";
    /**播放视频*/
    public static final String CMD_PLAY_VIDEO = "CmdPlayVideo";

    /**接口：支付*/
    public static final String PAYMENT = "payment";
    /**MSG：充值微信支付结果*/
    public static final String WX_PAY_RESULT = "wxPayResult";
    /**MSG：书籍购买微信支付结果*/
    public static final String BOOK_WX_PAY_RESULT = "bookWXPayResult";
    /**MSG:支付宝支付结果*/
    public static final String PAY_RESULT = "payResult";
    /**MSG:支付成功，通知书城*/
    public static final String BOOK_PAY_RESULT = "MsgOpenSuccess";

    /**接口: 上传用户个人资料*/
    public static final String UPDATE_CUSTOMER_INFO = "PostUpdateCustomerInfo";

    /**接口: 获取用户个人资料*/
    public static final String GET_CUSTOMER_INFO = "GetCustomerInfo";

    /**接口：支付服务端验证结果*/
    public static final String CONFIRM_TRADE = "confirmTrade";

    /**接口：样式界面版本验证*/
    public static final String JS_CSS_UPDATE = "jsCssUpdate";
    /**接口：充值界面初始化数据*/
    public static final String GET_RECHARGE_PACKAGE_LIST = "GetRechargePackageList";
    /**充值*/
    public static final String RECHARGE_PAY = "PostRechargePay";
    /**接口：判断书籍是否购买*/
    public static final String GET_IS_PURCHASED_BOOK = "GetIsPurchasedBook";
    /**接口：获取书籍自测题*/
    public static final String GET_BOOK_CHAPTER_EXAM_LIST = "GetBookChapterExamList";
    /**接口：充值消费记录*/
    public static final String GET_RECHARGE_CONSUMPTION_LIST = "GetRechargeAndConsumptionList";
    /**接口：优惠券*/
    public static final String GET_COUPON_LIST = "GetCouponList";
    /**接口：激活验证码*/
    public static final String POST_VERIFICATION_CODE = "PostVerificationCode";
    /**接口：重置密码*/
    public static final String POST_RESET_PASSWORD = "PostResetPassword";
    /**接口：意见反馈*/
    public static final String POST_FEEDBACK = "PostFeedback";
    /**获取书籍下载状态*/
    public static final String GET_BOOK_STATE = "GetBookState";
    /**获取版本信息*/
    public static final String GET_APP_VERSION = "GetAPPVersion";
    /**获取版本信息*/
    public static final String GET_NETWORK_STATE = "GetNetworkState";
    /**接口:获取书架轮播图*/
    public static final String GET_BOOK_SHELF_AD="GetBookShelfAd";
    /**获取段落数据*/
    public static final String GET_BOOK_PARAGRAPH = "GetBookParagraph";
    public static final String GET_BOOK_PARAGRAPH_LOCAL = "GetBookParagraphLocal";


    //埋点的开始结束
    public static final String ACTION_TYPE_START = "0";
    public static final String ACTION_TYPE_END = "1";
    /**埋点：app启动*/
    public static final String ACTION_APP_STARTUP = "001";
    /**埋点：app挂起*/
    public static final String ACTION_APP_HANG = "002";
    /**埋点：立即更新 */
    public static final String ACTION_UPDATE_APP = "019";
    /**埋点：稍后更新 */
    public static final String ACTION_CANCEL_UPDATE_APP = "020";

    //本地音频文件后缀
    public static final String AUDIO_SUFFIX=".mp3";
    //进入后台运行
    public static final String MSG_APP_STARTING = "MsgAppStarting";
    //App重新进入前台
    public static final String MSG_APP_REACTIVATE= "MsgAppReactivate";
    //获取考试答案
    public static final String CMD_GET_EXAMANSWERS = "GetExamAnswers";
    //保存考试答案
    public static final String CMD_SAVE_ANSWER = "PostSaveAnswer";
    //清除保存的答案
    public static final String CMD_CLEAR_EXAM_ANSWERS = "CmdClearExamAnswers";
}
