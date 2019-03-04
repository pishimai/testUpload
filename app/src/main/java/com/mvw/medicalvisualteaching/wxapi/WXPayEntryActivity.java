package com.mvw.medicalvisualteaching.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.activity.BaseActivity;
import com.mvw.medicalvisualteaching.config.Constant;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 接收微信支付结构的Activity，设置为透明的接收完毕消息后结束自己
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	public static final String WXPAY_ACTION ="com.mvw.nationalmedicalPhone.wxpay";
	private IWXAPI api;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wx_pay_result);
		context = this;
		api = WXAPIFactory.createWXAPI(this, Constant.AppID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	/**
	 * 0 成功 展示成功页面 -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
	 * -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
	 */
	@Override
	public void onResp(BaseResp resp) {
		PayResp payResp = (PayResp) resp;
		Intent intent = new Intent(WXPAY_ACTION);
		intent.putExtra("extData",payResp.extData);
		Logger.e("微信支付结果状态 code = "+resp.errCode+"  str = "+resp.errStr);
		switch (resp.errCode) {
		case 0:
			Logger.i("  extData "+payResp.extData+"  ");
			intent.putExtra("result",0);
			break;
		case -1:
//			Toast.makeText(context,"微信支付错误",Toast.LENGTH_SHORT).show();
			intent.putExtra("result",-1);
			break;
		case -2:
//			Toast.makeText(context,"微信支付取消",Toast.LENGTH_SHORT).show();
			intent.putExtra("result",-2);
			break;
		default:
//			Toast.makeText(context,"未知错误",Toast.LENGTH_SHORT).show();
			intent.putExtra("result",-3);
			break;
		}
		sendBroadcast(intent);
		// 完成回调后结束当前页面
		finish();
	}
}