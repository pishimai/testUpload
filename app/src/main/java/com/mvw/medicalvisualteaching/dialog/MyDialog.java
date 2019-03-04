package com.mvw.medicalvisualteaching.dialog;


import static com.mvw.medicalvisualteaching.R.style;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context, style.LoginDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.BOTTOM);//显示在屏幕底部

        WindowManager m = ((Activity)context).getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth()); // 与屏幕同宽
        //p.height = (int) (d.getHeight()*0.5);
        dialogWindow.setAttributes(p);

//        dialogWindow.setWindowAnimations(style.dialogWindowAnim);
        setCanceledOnTouchOutside(true);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }
}
