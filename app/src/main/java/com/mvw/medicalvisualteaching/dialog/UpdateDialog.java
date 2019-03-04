package com.mvw.medicalvisualteaching.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.mvw.medicalvisualteaching.R;


/**
 * 版本更新提示自定义对话框
 */
public class UpdateDialog extends Dialog {
    public UpdateDialog(Context context) {
        super(context, R.style.UpdateDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.CENTER);//显示在屏幕底部

        WindowManager m = ((Activity)context).getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth()); // 与屏幕同宽
        //p.height = (int) (d.getHeight()*0.5);
        dialogWindow.setAttributes(p);

//        dialogWindow.setWindowAnimations(R.style.AnimationTranslation);
//        setCanceledOnTouchOutside(true);
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
    }
}
