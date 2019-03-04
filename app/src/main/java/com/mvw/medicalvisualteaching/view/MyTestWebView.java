package com.mvw.medicalvisualteaching.view;


import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ActionMode.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.mvw.medicalvisualteaching.R;



@TargetApi(Build.VERSION_CODES.KITKAT)
public class MyTestWebView extends WebView {
	private MyTestWebView mWebView;
	private Context context;
	
	private SelectActionModeCallback actionModeCallback;
	private String[] strArray = new String[4];
	private Menu menu;
	private ActionMode mode;
	private String color;
	private boolean isSelectColor = false;
	private OnWebViewCallBack mCallback;
	
	private String noteStatus;
	
	public MyTestWebView(Context context) {
		super(context);
		this.context = context;
		mWebView = this;
	}

	public MyTestWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
//		contentFragment.setCurrentPager();
//		LogUtil.info("::onScrollChanged------");
	}

	public MyTestWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	public interface OnSelectedListener{
		void onSelected();
		void onUnSelected();
	}
	
	@Override
    public ActionMode startActionMode(Callback callback) {
        actionModeCallback = new SelectActionModeCallback();
        if(canGoBack()){
        	return super.startActionMode(callback); 
        }else{
        	return super.startActionMode(actionModeCallback);
        }
    }
	
	private class MyTask implements Runnable {
		@Override
		public void run() {
			handler.sendEmptyMessage(1);
		}
	}
	
	private ScheduledFuture<?> scheduleWithFixedDelay;
	public class SelectActionModeCallback implements Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			if(!isSelectColor){
				inflater.inflate(R.menu.actionmode, menu);
				MenuItem action_color = menu.findItem(R.id.action_color);
				MyTestWebView.this.menu = menu;
				action_color.setVisible(false);
				getMode();
				ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(10);
				MyTask myTask = new MyTask();
				scheduleWithFixedDelay = stpe.scheduleWithFixedDelay(
						myTask, 500, 500, TimeUnit.MILLISECONDS);
			}else{
				inflater.inflate(R.menu.actioncolor, menu);
				isSelectColor = false;
			}
			MyTestWebView.this.mode = mode;
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_fanyi:
				// 翻译
				evaluateJavascript("javascript:window.getSelection().toString()", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(final String transText) {
						if(transText != null && !transText.equals("")){
							evaluateJavascript("javascript:Cocoa.trigger('translate_terms','"+transText+"')", new ValueCallback<String>() {
								@Override
								public void onReceiveValue(String value) {
									evaluateJavascript("javascript:Cocoa.trigger('ordinate')", new ValueCallback<String>() {
										@SuppressWarnings("deprecation")
										@Override
										public void onReceiveValue(String value) {
											if(value != null){
												value = value.replaceAll("\\\\", "").replaceAll("\\\"", "");
												String[] split = value.split("_");
												if(split != null && split.length==4){
													try {
														int x = Integer.parseInt(split[0]);
														int y = Integer.parseInt(split[1]);
														mCallback.onTranslate(transText, (int)(x * getScale()), (int)(y * getScale() - getScrollY()));
													} catch (NumberFormatException e) {
														e.printStackTrace();
													}
												}
											}
										}
									});
								}
							});
						}
					}
				});
				mode.finish();
				break;
			case R.id.action_gaoliang:
				// 高亮
				setHighlight();
				mode.finish();
				break;
			case R.id.action_zhushi:
				// 笔记
				evaluateJavascript("javascript:Cocoa.trigger('selection')", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String value) {
						evaluateJavascript("javascript:Cocoa.trigger('create_notation', 'notes', '"+color+"')", new ValueCallback<String>() {
							@Override
							public void onReceiveValue(String value) {
								if(value != null){
								}
							}
						});
					}
				});
				mode.finish();
				break;
			case R.id.action_db:

				mode.finish();
				break;
//			case R.id.action_zhushi_hand:
//				Umeng.handwrittenNotes(context);
				// 手写笔记
//				mCallback.onCountBookNoteDot(AppConfig.BOOK_NOTEHAND);
//				evaluateJavascript("javascript:Cocoa.trigger('selection')", new ValueCallback<String>() {
//					@Override
//					public void onReceiveValue(String value) {
//						evaluateJavascript("javascript:Cocoa.trigger('create_notation', 'hwnotes', '"+color+"')", new ValueCallback<String>() {
//							@Override
//							public void onReceiveValue(String value) {
//								if(value != null){
//									LogUtil.info(value);
//									handWritingZhushiNotation = ZNotation.parseHandWritingZhushi(value, context);
////									mCallback.openHandWriting(handWritingZhushiNotation,true);
//								}
//							}
//						});
//					}
//				});
//				mode.finish();
//				break;
//			case R.id.action_zhushi_voice:
				// 声音笔记
//				Umeng.recordingNotes(context);
//				mCallback.onCountBookNoteDot(AppConfig.BOOK_NOTERECORD);
//				evaluateJavascript("javascript:Cocoa.trigger('selection')", new ValueCallback<String>() {
//					@Override
//					public void onReceiveValue(String value) {
//						evaluateJavascript("javascript:Cocoa.trigger('create_notation', 'audionotes', '"+color+"')", new ValueCallback<String>() {
//							@Override
//							public void onReceiveValue(String value) {
//								// 这里重写解析声音笔记的方法
//								if(value != null){
//									voiceZhushiNotation = ZNotation.parseVoiceZhushi(value, context);
//								}
//							}
//						});
//					}
//				});
//				mode.finish();
//				break;
			case R.id.action_color:
				// 颜色
				mode.finish();
				isSelectColor = true;
				startActionMode(actionModeCallback);
				break;
			case R.id.action_blue:
				color = "blueColor";
				updateHighlight();
				mode.finish();
				break;
			case R.id.action_green:
				color = "greenColor";
				updateHighlight();
				mode.finish();
				break;
			case R.id.action_yellow:
				color = "yellowColor";
				updateHighlight();
				mode.finish();
				break;
			case R.id.action_pink:
				color = "pinkColor";
				updateHighlight();
				mode.finish();
				break;
			case R.id.action_violet:
				color = "purpleColor";
				updateHighlight();
				mode.finish();
				break;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			MyTestWebView.this.menu = null;
			scheduleWithFixedDelay.cancel(true);
		}
	}
	
	/**
	 * 设置高亮
	 */
	@SuppressLint("NewApi")
	private void setHighlight() {
		evaluateJavascript("javascript:Cocoa.trigger('selection')", new ValueCallback<String>() {
			@SuppressLint("NewApi")
			@Override
			public void onReceiveValue(String value) {

				evaluateJavascript("javascript:Cocoa.trigger('create_notation', 'highlights', '"+color+"')", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String value) {

					}
				});
			}
		});
	}
	
	/**
	 * 更新高亮
	 */
	private void updateHighlight() {
		evaluateJavascript("javascript:Cocoa.trigger('selection')", new ValueCallback<String>() {
			@Override
			public void onReceiveValue(String value) {
				evaluateJavascript("javascript:Cocoa.trigger('create_notation', 'highlights', '"+color+"')", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String value) {

					}
				});
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if(strArray[0] != null && strArray[1] != null && !"\"null\"".equals(strArray[0]) && !"\"null\"".equals(strArray[1])){
					String gaoliangStatus = strArray[0];
					String zhushiStatus = strArray[1];
//					String handZhushiStatus=strArray[2];
//					String voiceZhushiStatus = strArray[3];
					MyTestWebView.this.noteStatus = zhushiStatus;
					if(menu != null){
						MenuItem action_gaoliang = menu.findItem(R.id.action_gaoliang);
						MenuItem action_color = menu.findItem(R.id.action_color);
						action_gaoliang.setVisible(true);
						action_color.setVisible(false);
						if(gaoliangStatus != null && gaoliangStatus.contains("highlight")){
							action_gaoliang.setTitle("高亮显示");
						}else if(gaoliangStatus != null && gaoliangStatus.contains("remove")){
							action_gaoliang.setTitle("去除高亮显示");
							action_color.setVisible(true);
						}else if(gaoliangStatus != null && gaoliangStatus.contains("extend")){
							action_gaoliang.setTitle("扩展高亮显示");
						}else if(gaoliangStatus != null && gaoliangStatus.contains("combine")){
							action_gaoliang.setTitle("合并高亮显示");
						}else{
							action_gaoliang.setVisible(false);
						}
						MenuItem action_zhushi = menu.findItem(R.id.action_zhushi);
						action_zhushi.setVisible(true);
						if(zhushiStatus != null && zhushiStatus.contains("highlight")){
							action_zhushi.setTitle("笔记");
						}else if(zhushiStatus != null && zhushiStatus.contains("remove")){
							action_zhushi.setTitle("编辑笔记");
						}else if(zhushiStatus != null && zhushiStatus.contains("extend")){
							action_zhushi.setTitle("扩展笔记");
						}else if(zhushiStatus != null && zhushiStatus.contains("combine")){
							action_zhushi.setTitle("合并笔记");
						}else{
							action_zhushi.setVisible(false);
						}
//						MenuItem action_zhushi_voice = menu.findItem(R.id.action_zhushi_voice);
//						action_zhushi_voice.setVisible(true);
//						if(voiceZhushiStatus != null && voiceZhushiStatus.contains("highlight")){
//							action_zhushi_voice.setTitle("声音笔记");
//						}else if(voiceZhushiStatus != null && voiceZhushiStatus.contains("remove")){
//							action_zhushi_voice.setTitle("编辑声音笔记");
//						}else if(voiceZhushiStatus != null && voiceZhushiStatus.contains("extend")){
//							action_zhushi_voice.setTitle("扩展声音笔记");
//							// 由于js有问题，不能做声音笔记和手写笔记的扩展及合并功能，隐藏
//							action_zhushi_voice.setVisible(false);
//						}else if(voiceZhushiStatus != null && voiceZhushiStatus.contains("combine")){
//							action_zhushi_voice.setTitle("合并声音笔记"); 
//							// 由于js有问题，不能做声音笔记和手写笔记的扩展及合并功能，隐藏
//							action_zhushi_voice.setVisible(false);
//						}else{
//							action_zhushi_voice.setVisible(false);
//						}
//						MenuItem action_zhushi_hand = menu.findItem(R.id.action_zhushi_hand);
//						LogUtil.info("::zhushihand>>"+handZhushiStatus);
//						if(handZhushiStatus!=null&&handZhushiStatus.contains("highlight")){
////							action_zhushi_hand.setTitle("手写笔记");
//							action_zhushi_hand.setVisible(true);
//						}/*else if(handZhushiStatus!=null&&handZhushiStatus.contains("remove")){
//							action_zhushi_hand.setTitle("编辑手写笔记");
//						}else if(handZhushiStatus!=null&&handZhushiStatus.contains("extend")){
//							action_zhushi_hand.setTitle("扩展手写笔记");
//						}else if(handZhushiStatus!=null&&handZhushiStatus.contains("combine")){
//							action_zhushi_hand.setTitle("合并手写笔记");
//						}*/else{
//							action_zhushi_hand.setVisible(false);
//						}
					}
				}else{
					if(MyTestWebView.this.mode != null){
						//如果没有选中的内容 就不显示菜单栏
						MyTestWebView.this.mode.finish();
					}
					System.out.println(" action mode finish ...");
				}
				break;
			case 1:
				getMode();
				break;
			}
		};
	};
	
	/**
	 * 返回当前注释的状态
	 * @return
	 */
	public String getNoteState(){
		return noteStatus;
	}
	
	
	private String[] getMode(){
		evaluateJavascript("javascript:Cocoa.trigger('selection')", new ValueCallback<String>() {
			@Override
			public void onReceiveValue(String value) {
				// 高亮
				evaluateJavascript("javascript:Cocoa.trigger('notation_mode', 'highlights')", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String value) {
						strArray[0] = value;
						handler.sendEmptyMessage(0);
					}
				});
				// 注释
				evaluateJavascript("javascript:Cocoa.trigger('notation_mode', 'notes')", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String value) {
						strArray[1] = value;
						handler.sendEmptyMessage(0);
					}
				});
			}
		});
		return strArray;
	}
	
	float x = 0;
	float y = 0;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = ev.getX();
			y = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			if (mode != null) {
				float tempX = ev.getX();
				float tempY = ev.getY();
				if (Math.abs(tempX - x) < 5 && Math.abs(tempY - y) < 5) {
					mode.finish();
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	

	public interface OnWebViewCallBack{
		void onTranslate(String text, int x, int y);
//		void openHandWriting(ZNotation znotation,boolean isFirst);
		void onCountBookNoteDot(String func);
	}
	private void initCallback(Activity activity) {
		try {
			mCallback = (OnWebViewCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWebViewCallBack");
        }
	}
	
	public ActionMode getAMode(){
		return mode;
	}
}
