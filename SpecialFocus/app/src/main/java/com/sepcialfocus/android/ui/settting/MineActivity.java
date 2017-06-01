/**
 * 工程名: MainActivity
 * 文件名: MineActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-9-20上午10:55:33
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.share.CustomShareBoard;
import com.umeng.onlineconfig.OnlineConfigAgent;


/**
 * 类名: MineActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-20 上午10:55:33 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MineActivity extends BaseFragmentActivity implements View.OnClickListener{

	ImageView mBackImg;
	TextView mTitleTv;
	
	RelativeLayout mHistoryRl;
	RelativeLayout mAddColumnRl;
	RelativeLayout mFavorRl;
	RelativeLayout mFeedbackRl;
//	RelativeLayout mSystemMsgRl;
	RelativeLayout mShareRl;
//	RelativeLayout mAboutUsRl;
	RelativeLayout mSettingRl;
	RelativeLayout mReviewAdRl;
	
	CustomShareBoard mCustomShareBoard;
	
	private String shareContent = "";
	private String titleContent = "";
	private String targetUrl = "";
	Intent intent ;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_mine);
		initView();
		shareContent = OnlineConfigAgent.getInstance().getConfigParams(this, AppConstant.umeng_share_content);
		titleContent = OnlineConfigAgent.getInstance().getConfigParams(this, AppConstant.umeng_share_title);
		targetUrl = OnlineConfigAgent.getInstance().getConfigParams(this, AppConstant.umeng_share_targetUrl);
	}
	
	

	@Override
	protected void initView() {
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		mTitleTv.setText("我的");
		
		mHistoryRl = (RelativeLayout)findViewById(R.id.mine_history_rl);
		mAddColumnRl = (RelativeLayout)findViewById(R.id.mine_columns_rl);
		mFavorRl = (RelativeLayout)findViewById(R.id.mine_favorite_rl);
		mFeedbackRl = (RelativeLayout)findViewById(R.id.mine_feedback_rl);
//		mSystemMsgRl = (RelativeLayout)findViewById(R.id.mine_msg_rl);
		mShareRl = (RelativeLayout)findViewById(R.id.mine_share_rl);
//		mAboutUsRl = (RelativeLayout)findViewById(R.id.mine_about_us_rl);
		mSettingRl = (RelativeLayout)findViewById(R.id.mine_setting_rl);
		mReviewAdRl = (RelativeLayout)findViewById(R.id.mine_review_ad_rl);
		mHistoryRl.setOnClickListener(this);
		mAddColumnRl.setOnClickListener(this);
		mFavorRl.setOnClickListener(this);
		mFeedbackRl.setOnClickListener(this);
//		mSystemMsgRl.setOnClickListener(this);
		mShareRl.setOnClickListener(this);
//		mAboutUsRl.setOnClickListener(this);
		mSettingRl.setOnClickListener(this);
		mReviewAdRl.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.img_title_back:
			finish();
			break;
		case R.id.mine_history_rl:
			startActivity(new Intent(MineActivity.this,HistoryActivity.class));
			break;
		case R.id.mine_columns_rl:
			intent = new Intent(MineActivity.this,DragSortMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_favorite_rl:
			intent = new Intent(MineActivity.this,HistoryActivity.class);
			intent.putExtra("key", false);
			startActivity(intent);
			break;
		case R.id.mine_feedback_rl:
			intent = new Intent(MineActivity.this,FeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_review_ad_rl: // 回看动画
//			intent = new Intent(MineActivity.this,ReviewAdActivity.class);
//			startActivity(intent);
			reviewAdRl();
			break;
		case R.id.mine_share_rl:
			if(mCustomShareBoard == null || !mCustomShareBoard.isShowing()){
				// content title targetUrl;
				openSharewindow(v, shareContent, titleContent,targetUrl, AppConstant.SHARE_ICON);
			}
			break;
		case R.id.mine_setting_rl:
			intent = new Intent(MineActivity.this,SettingActivity.class);
			startActivity(intent);
			break;
		}
		
	}

	private void reviewAdRl(){
		try{
			Uri uri = Uri.parse("market://details?id="+getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(MineActivity.this, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * TODO 自定义分享界面.<br/>
	 * 
	 * @author wenpeng 2015-6-15下午6:21:58
	 * @param view
	 * @since 1.0
	 */
	private void openSharewindow(View view,String content,String title,
			String targetUrl,String iconUrl) {
		mCustomShareBoard = new CustomShareBoard(this);
		mCustomShareBoard.setShareContent(content,title,targetUrl,iconUrl);
		mCustomShareBoard.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				setBackground(1.0f);
			}
		});
		setBackground(0.3f);
		mCustomShareBoard.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private void setBackground(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}
	
	
	

}

