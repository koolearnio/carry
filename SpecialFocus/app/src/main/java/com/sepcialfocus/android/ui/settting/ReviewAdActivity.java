/**
 * 工程名: MainActivity
 * 文件名: ReviewAdActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-10-3下午12:05:50
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * 类名: ReviewAdActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-10-3 下午12:05:50 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ReviewAdActivity extends BaseFragmentActivity implements View.OnClickListener{
	View splash;
	RelativeLayout splashLayout;
	ImageView  mBackImg;
	TextView mTitleTv;
	String platform;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_review_ads);
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		if(getIntent().getStringExtra("title")!=null){
			mTitleTv.setText(getIntent().getStringExtra("title"));
		}else{
			mTitleTv.setText(getResources().getString(R.string.mine_review_ad_str));
		}
		
//		AdManager.getInstance(this).init(AppConstant.YOUMI_APPID, AppConstant.YOUMI_APPSECRET, false);
//		// 第二个参数传入目标activity，或者传入null，改为setIntent传入跳转的intent
//		splashView = new SplashView(this, null);
//		// 设置是否显示倒数
//		splashView.setShowReciprocal(false);
//		// 隐藏关闭按钮
//		splashView.hideCloseBtn(true);
//
////		Intent intent = new Intent(this, MineActivity.class);
////		splashView.setIntent(intent);
//		splashView.setIsJumpTargetWhenFail(false);

//<<<<<<< HEAD
////		Intent intent = new Intent(this, MineActivity.class);
////		splashView.setIntent(intent);
//		splashView.setIsJumpTargetWhenFail(false);
//
//		splash = splashView.getSplashView();
//		splashLayout = ((RelativeLayout) findViewById(R.id.splashview));
//		splashLayout.setVisibility(View.GONE);
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
//		splashLayout.addView(splash, params);
////		MKLog.d("shit",getDeviceInfo(this));
//			SpotManager.getInstance(this).showSplashSpotAds(this, splashView,
//					new SpotDialogListener() {
//				
//				@Override
//				public void onShowSuccess() {
//					splashLayout.setVisibility(View.VISIBLE);
//					splashLayout.startAnimation(AnimationUtils.loadAnimation(ReviewAdActivity.this, R.anim.pic_enter_anim_alpha));
//					MKLog.d("youmisdk", "展示成功");
//					MobclickAgent.onEvent(ReviewAdActivity.this,"welcome_ad", 
//							new HashMap<String,String>().put("首页广告","展示成功"));
//				}
//				
//				@Override
//				public void onShowFailed() {
//					MKLog.d("youmisdk", "展示失败");
//					MobclickAgent.onEvent(ReviewAdActivity.this,"welcome_ad", 
//							new HashMap<String,String>().put("首页广告","展示失败"));
//				}
//				
//				@Override
//				public void onSpotClosed() {
//					MKLog.d("youmisdk", "展示关闭");
//				}
//				
//				@Override
//				public void onSpotClick() {
//					MobclickAgent.onEvent(ReviewAdActivity.this,"welcome_ad", 
//							new HashMap<String,String>().put("首页广告","点击"));
//					MKLog.i("YoumiAdDemo", "插屏点击");
//				}
//			});
//=======
//		splash = splashView.getSplashView();
//		splashLayout = ((RelativeLayout) findViewById(R.id.splashview));
//		splashLayout.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_title_back:
			finish();
			break;
		}
	}
	
}

