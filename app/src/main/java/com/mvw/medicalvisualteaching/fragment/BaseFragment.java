package com.mvw.medicalvisualteaching.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.activity.BaseActivity;
import com.mvw.medicalvisualteaching.callback.BaseJavaScriptInterface;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.mvw.medicalvisualteaching.view.MyXWalkView;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 *
 */
@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment {

	private boolean isLoad;//标示是否加载过
	/** 弹出框 */
	private ProgressDialog pdDialog;
	private RelativeLayout rlNoNetwork;
	private View layout;
	protected XWalkView mXwalkView;
	protected BaseActivity activity;
	protected String url ="";
//	protected MyReceiver myReceiver;
	protected boolean isFinish = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (layout == null){
			layout = inflater.inflate(R.layout.fragment_base, container, false);
		}
		activity = (BaseActivity) getActivity();
		initView(layout);
		initData();
		return layout;
	}


	public BaseFragment(XWalkView xWalkView){
		super();
		this.mXwalkView = xWalkView;
	}
	public BaseFragment(){

	}

	protected void initView(View layout){
		LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.ll_layout);
		if(mXwalkView == null){
			mXwalkView = new MyXWalkView(activity);
		}
		mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
		mXwalkView.setUIClient(new MyUIClient(mXwalkView));
		mXwalkView.addJavascriptInterface(new BaseJavaScriptInterface(activity), "Elf");
		linearLayout.removeAllViews();
		linearLayout.addView(mXwalkView);
		mXwalkView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
	}


	protected void initData(){

	}

	protected void loadUrl(){
		isLoad = true;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Logger.i(" --------- isVisibleToUser ========  "+isVisibleToUser+"  isVisible() = "+isVisible()+"  isLoad = "+!isLoad);
		//判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
		if (isVisibleToUser && isVisible() && !isLoad) {
			loadUrl();
		}
		if(isVisibleToUser && isVisible()){
			resume();
		}
//		if(!isVisibleToUser && isVisible() && isLoad){
//			pause();
//		}
		super.setUserVisibleHint(isVisibleToUser);
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
//        Logger.i("getUserVisibleHint() = "+getUserVisibleHint());
		if (getUserVisibleHint() ) {
			loadUrl();
			resume();
		}
		super.onActivityCreated(savedInstanceState);
	}

	class MyResourceClient extends XWalkResourceClient {
		MyResourceClient(XWalkView view) {
			super(view);
		}
		@Override
		public void onLoadStarted(XWalkView view, String url) {
			isFinish = false;
			super.onLoadStarted(view, url);
		}

		@Override
		public void onLoadFinished(XWalkView view, String url) {
			super.onLoadFinished(view, url);
			activity.hideWaitDialog();
			isFinish = true;
		}

//		@Override
//		public void onReceivedResponseHeaders(XWalkView view, XWalkWebResourceRequest request,
//				XWalkWebResourceResponse response) {
//			int status = response.getStatusCode();
//			if(status != 200){
//				initNoNetworkLayout(layout,url);
//			}
//		}

		@Override
		public void onReceivedLoadError(XWalkView view, int errorCode, String description,
				String failingUrl) {
//			super.onReceivedLoadError(view,errorCode,description,failingUrl);
				initNoNetworkLayout(layout,url);
		}
	}

	class MyUIClient extends XWalkUIClient {
		MyUIClient(XWalkView view) {
			super(view);
		}
	}

	/**
	 * 初始设置没网络布局
   */
	protected void initNoNetworkLayout(View layout, final String url){
//		if(!Utils.isNetworkAvailable(activity)){
			layout.findViewById(R.id.rl_no_net_title).setVisibility(View.GONE);
			rlNoNetwork = (RelativeLayout) layout.findViewById(R.id.rl_no_work);
			rlNoNetwork.setVisibility(View.VISIBLE);
			rlNoNetwork.findViewById(R.id.btn_no_network_refresh).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(mXwalkView != null){
								if(Utils.isNetworkAvailable(getActivity())){
									rlNoNetwork.setVisibility(View.GONE);
									//mXwalkView.clearCache(true);
									mXwalkView.loadUrl(url);
									activity.showWaitDialog();

								}
							}
						}
					});
//		}
	}

	public ProgressDialog showWaitDialog() {
		return showWaitDialog(R.string.loading);
	}

	public ProgressDialog showWaitDialog(int resid) {
		return showWaitDialog(getString(resid));
	}

	public ProgressDialog showWaitDialog(String message) {
		if(pdDialog == null){
			pdDialog = new ProgressDialog(getActivity());
			pdDialog.setCanceledOnTouchOutside(false);
			pdDialog.setCancelable(false);
		}
		if(message != null){
			pdDialog.setMessage(message);
		}
		pdDialog.show();
		return pdDialog;
	}

	public void hideWaitDialog() {
		if(pdDialog != null && pdDialog.isShowing()){
			pdDialog.dismiss();
		}
	}

	public void appCallWeb(String sn,String content){
		if(mXwalkView != null){
			if(content == null || "".equals(content)){
				mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"')", null);
			}else {
				mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"','"+content+"')", null);
			}
		}
	}

	public void resume(){
		if (mXwalkView != null) {
			mXwalkView.resumeTimers();
			mXwalkView.onShow();
		}
	}
	public void pause(){
		if (mXwalkView != null) {
			mXwalkView.pauseTimers();
			mXwalkView.onHide();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onKey(int keyCode, KeyEvent event){}
}
