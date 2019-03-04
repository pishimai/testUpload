package com.mvw.medicalvisualteaching.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.orhanobut.logger.Logger;
import org.xwalk.core.XWalkView;

/**
 * base
 * Created by a on 2016/11/16.
 */
public class BaseActivity extends FragmentActivity {

    /** 弹出框 */
    private ProgressDialog pdDialog;
    protected XWalkView mXwalkView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 初始设置没网络布局
     */
    protected void initNoNetworkLayout(final String url){
//        if(!Utils.isNetworkAvailable(this)){
            findViewById(R.id.ib_no_net_back).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            final RelativeLayout rlNoNetwork = (RelativeLayout) findViewById(R.id.rl_no_work);
            rlNoNetwork.setVisibility(View.VISIBLE);
            rlNoNetwork.findViewById(R.id.btn_no_network_refresh).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mXwalkView != null){
                            if(Utils.isNetworkAvailable(BaseActivity.this)){
                                rlNoNetwork.setVisibility(View.GONE);
                                //mXwalkView.clearCache(true);
                                mXwalkView.load(url,null);
                                showWaitDialog();
                            }
                        }
                    }
                });
//        }
    }

    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    public ProgressDialog showWaitDialog(String message) {
        if(pdDialog == null){
            pdDialog = new ProgressDialog(this);
            pdDialog.setCanceledOnTouchOutside(false);
        }
        if(message != null){
            pdDialog.setMessage(message);
        }
        if(!pdDialog.isShowing()){
            pdDialog.show();
        }
        return pdDialog;
    }

    public void hideWaitDialog() {
        if(pdDialog != null && pdDialog.isShowing()){
            pdDialog.dismiss();
        }
    }
    /**js交互*/
    public void appCallWeb(String command,String sn,String content){
        if(mXwalkView != null){
            if(content == null || "".equals(content)){
                mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"')", null);
                Logger.i("javascript:Elf.AppCallWeb('"+sn+"')");

            }else {
                mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"','"+content+"')", null);
            }
        }
    }
    /**js交互*/
    public void appCallWeb(String command,String sn,String content,String flag){
        if(mXwalkView != null){
            if(content == null || "".equals(content)){
                mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"')", null);
            }else {
                mXwalkView.evaluateJavascript("javascript:Elf.AppCallWeb('"+sn+"','"+content+"','"+flag+"')", null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideWaitDialog();
    }
}
