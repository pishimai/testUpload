package com.mvw.medicalvisualteaching.view;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import org.xwalk.core.XWalkView;

/**
 * 拦截XWalkView返回键处理
 */
public class MyXWalkView extends XWalkView {
	public MyXWalkView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyXWalkView(Context context, Activity activity) {
		super(context, activity);
		// TODO Auto-generated constructor stub
	}

	public MyXWalkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){

			return false;
		}
		return super.dispatchKeyEvent(event);
	}

}
