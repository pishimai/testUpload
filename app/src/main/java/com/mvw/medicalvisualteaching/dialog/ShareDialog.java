package com.mvw.medicalvisualteaching.dialog;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.mvw.medicalvisualteaching.R;
import com.umeng.social.tool.UMImageMark;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


/**
 * 用户分享对话框
 */
public class ShareDialog extends MyDialog {


    private Activity activity;

    public ShareDialog(Activity activity) {
        super(activity);
        setContentView(R.layout.dialog_share);
        this.activity = activity;
        initMedia();
        init();
    }

    private void init(){
        findViewById(R.id.tv_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.QQ);
            }
        });
        findViewById(R.id.tv_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.WEIXIN);
            }
        });
        findViewById(R.id.tv_wxcircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        findViewById(R.id.tv_qzone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.QZONE);
            }
        });
        findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private UMWeb web;
    private void share(SHARE_MEDIA share_media){
        new ShareAction(activity)
            .withText("临床医学五年制规划教材，多媒体视听，灵活笔记，海量习题，模拟测试，数据资源库")
            .withMedia(web)
            .setPlatform(share_media)
            .setCallback(shareListener).share();
    }

    private void initMedia(){
        UMImageMark umImageMark = new UMImageMark();
        umImageMark.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        umImageMark.setMarkBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.share_pic));
        web = new UMWeb("http://www.imed.org.cn/chan.html");
        web.setTitle("我正在用《国家医学电子书包》，下载地址戳这里：");
        web.setThumb(new UMImage(activity, R.mipmap.share_pic));
        web.setDescription("临床医学五年制规划教材，多媒体视听，灵活笔记，海量习题，模拟测试，数据资源库");
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(activity,"分享成功",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(activity,"分享失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(activity,"分享取消",Toast.LENGTH_LONG).show();

        }
    };

}
