/**
 * 工程名: MainActivity
 * 文件名: SettingActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-10-4上午9:24:45
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mike.aframe.utils.PreferenceHelper;
import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.utils.UpdateManager;
import com.sepcialfocus.android.widgets.SwitchButton;
import com.sepcialfocus.android.widgets.SwitchButton.OnChangeListener;

/**
 * 类名: SettingActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-10-4 上午9:24:45 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class SettingActivity extends BaseFragmentActivity implements View.OnClickListener{

	ImageView mBackImg;
	TextView mTitleTv;
	SwitchButton mSwitchNoImgBtn;
	SwitchButton mSwitchNoWindowBtn;
	
	// 无图模式开关
	private boolean mSwitchNoImgFlag;
	// 隐藏悬浮窗开关
	private boolean mSwitchNoWindowFlag;
	
	private RelativeLayout mFontSizeRl;
	
	private RelativeLayout mCheckVersionRl;
	private RelativeLayout mLocalDownloadRl;
	private Intent intent;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting);
		mSwitchNoImgFlag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_IMG, AppConstant.FLAG_IMG, false);
		mSwitchNoWindowFlag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_WINDOW, AppConstant.FLAG_WINDOW, false);
		initView();
	}
	
	protected void initView(){
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		mTitleTv.setText(getResources().getString(R.string.mine_setting_str));
		
		mSwitchNoImgBtn = (SwitchButton)findViewById(R.id.switch_no_img);
		mSwitchNoWindowBtn = (SwitchButton)findViewById(R.id.switch_no_window);
		mSwitchNoImgBtn.setBoolean(mSwitchNoImgFlag);
		mSwitchNoWindowBtn.setBoolean(mSwitchNoWindowFlag);
		mSwitchNoImgBtn.setOnChangeListener(new OnChangeListener() {
			
			@Override
			public void onChange(SwitchButton sb, boolean state) {
				mSwitchNoImgFlag = state;
			}
		});
		
		mSwitchNoWindowBtn.setOnChangeListener(new OnChangeListener() {
			
			@Override
			public void onChange(SwitchButton sb, boolean state) {
				mSwitchNoWindowFlag = state;
			}
		});
		
		mFontSizeRl = (RelativeLayout)findViewById(R.id.mine_font_size_rl);
		mFontSizeRl.setOnClickListener(this);
//		mLocalDownloadRl = (RelativeLayout)findViewById(R.id.setting_load_recomm_rl);
//		mLocalDownloadRl.setOnClickListener(this);
		mCheckVersionRl = (RelativeLayout)findViewById(R.id.setting_check_version_rl);
		mCheckVersionRl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_title_back:
			goBack();
			break;
		case R.id.mine_font_size_rl:
			intent = new Intent(SettingActivity.this,FontSizeActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_check_version_rl:
			hh();
			break;
//		case R.id.setting_load_recomm_rl:
//			intent = new Intent(SettingActivity.this,ReviewAdActivity.class);
//			intent.putExtra("title", getResources().getString(R.string.setting_load_recommon_str));
//			startActivity(intent);
//			break;
		}
	}

	private void hh(){
		UpdateManager.getUpdateManager().checkAppUpdate(this, true);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void goBack(){
		PreferenceHelper.write(this, AppConstant.FLAG_IMG, AppConstant.FLAG_IMG, mSwitchNoImgFlag);
		PreferenceHelper.write(this, AppConstant.FLAG_WINDOW, AppConstant.FLAG_WINDOW, mSwitchNoWindowFlag);
		finish();
	}
	
}

