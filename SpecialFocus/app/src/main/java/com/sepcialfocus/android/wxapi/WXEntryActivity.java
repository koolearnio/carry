package com.sepcialfocus.android.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.sepcialfocus.android.Constant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Constant.api!=null){
			Constant.api.handleIntent(getIntent(), this);
		} else {
			finish();
		}
	}

	@Override
	public void onReq(BaseReq arg0) {
		
	}

	@Override
	public void onResp(BaseResp baseresp) {
		String result = "";
		finish();
		switch (baseresp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "success";
			finish();
			if (baseresp instanceof SendAuth.Resp){
//				SendAuth.Resp resp = (SendAuth.Resp)baseresp;
//				StringBuffer buffer = new StringBuffer();
//				buffer.append("appid=");
//				buffer.append(AppConstant.WEIXIN_APP_ID);
//				buffer.append("&secret=");
//				buffer.append(AppConstant.WEIXIN_APP_SECRET);
//				buffer.append("&code=");
//				buffer.append(resp.code);
//				buffer.append("&grant_type=");
//				buffer.append("authorization_code");
//				
//				HttpTools.get(AppConstant.URL_WEICHAT_ACCESSTOKEN+buffer.toString(), new AppResponseHandlerInterface(this) {
//
//					@Override
//					public void Success(byte[] arg2) {
//						String content = new String(arg2);
//						Intent intent = new Intent(AppConstant.WEIXIN_BROADCAST);
//						Bundle bundle = new Bundle();
//						bundle.putString("weixin", content);
//						intent.putExtras(bundle);
//						sendBroadcast(intent);
//						finish();
//					}
//
//					@Override
//					public void Failure() {
//						finish();
//					}
//					
//				});
				finish();
			} else if (baseresp instanceof SendMessageToWX.Resp){
//				Logger.d("base","baseresp instanceof SendMessageToWX.Resp");
				Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
				finish();
			} else {
//				Logger.d("base",baseresp.toString());
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "cancel";
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "dendied";
			break;
		default:
			result = "unknown";
			break;
		}
	}
}
