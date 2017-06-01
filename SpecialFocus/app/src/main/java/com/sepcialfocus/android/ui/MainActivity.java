/**
 * ������: SpecialFocus
 * �ļ���: MainActivity.java
 * ����: com.sepcialfocus.android.ui
 * ����: 2015-9-1����9:40:41
 * Copyright (c) 2015, ����С����ӽ����Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.koolearnio.sdk.view.Monitor;
import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.MD5Utils;
import com.mike.aframe.utils.PreferenceHelper;
import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.NavBean;
import com.sepcialfocus.android.configs.AppConfig;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.receiver.CheckUpdateReceiver;
import com.sepcialfocus.android.ui.adapter.ArticleFragmentPagerAdapter;
import com.sepcialfocus.android.ui.article.ArticleFragment;
import com.sepcialfocus.android.ui.article.MainFragment;
import com.sepcialfocus.android.ui.settting.DragSortMenuActivity;
import com.sepcialfocus.android.ui.settting.MineActivity;
import com.sepcialfocus.android.utils.UpdateManager;
import com.sepcialfocus.android.widgets.CustomDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

import net.youmi.android.spot.SpotManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ����: MainActivity <br/>
 * ����: 程序入口. <br/>
 * ����: 2015-9-1 ����9:40:41 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MainActivity extends BaseFragmentActivity
	implements OnPageChangeListener,View.OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener{
	
	private HorizontalScrollView mHorizontalScrollView ;
	private LinearLayout mLinearLayout;
	private int mScreenWidth;
	
	private int currentFragmentIndex;
	private boolean isEnd;
	
	private ArrayList<NavBean> mUrlsList = null;
	private ViewPager mFragmentViewPager = null;
	private ArticleFragmentPagerAdapter mFragmentPagerAdapter = null;
	private ArrayList<Fragment> mFragmentList;
	
	private RelativeLayout mDragSoftImg;
	
	private KJDB mKJDb;

	private long exitTime = 0;
	private ImageView mJumpMineImg;
	
	private GestureDetector detector = null;
	
	int mDragImgWidth = 0;
	int mDragImgHeight = 0;
	
	private CustomDialog dialog = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
//		AdManager.getInstance(this).init(AppConstant.YOUMI_APPID, AppConstant.YOUMI_APPSECRET, false);
		SpotManager.getInstance(this).loadSpotAds();
		SpotManager.getInstance(this).setAnimationType(
				SpotManager.ANIM_ADVANCE);
		SpotManager.getInstance(this).setSpotOrientation(
				SpotManager.ORIENTATION_PORTRAIT);
		// 友盟发送策略
		MobclickAgent.updateOnlineConfig(this);
		mFragmentList = new ArrayList<Fragment>();
		mKJDb = KJDB.create(this);
		initView();
		initMenu();
		initFragment();
		UpdateManager.getUpdateManager().checkAppUpdate(this, false);
		// 获取在线参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);
		detector = new GestureDetector(this, this);
		setDector();
	}
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		AppConfig.imgFlag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_IMG, AppConstant.FLAG_IMG, false);
		AppConfig.windowFlag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_WINDOW, AppConstant.FLAG_WINDOW, false);
		boolean flag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_NOTIFICATION, AppConstant.FLAG_NOTIFICATION, false);
		if(!flag){
			PreferenceHelper.write(this, AppConstant.FLAG_NOTIFICATION, AppConstant.FLAG_NOTIFICATION,true);
			setAlarm();
		}
		if(!AppConfig.windowFlag){
			mDragSoftImg.setVisibility(View.VISIBLE);
		}else{
			mDragSoftImg.setVisibility(View.GONE);
		}
	}




	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initMenu();
		initFragment();
	}



	protected void initView(){
		mJumpMineImg = (ImageView)findViewById(R.id.jump_mine_img);
		mJumpMineImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,MineActivity.class);
				startActivity(intent);
			}
		});
		mDragSoftImg = (RelativeLayout)findViewById(R.id.drag_soft_img);
		mDragSoftImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,DragSortMenuActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("key", mUrlsList);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mDragSoftImg.setOnTouchListener(this);
		mFragmentViewPager = (ViewPager)findViewById(R.id.fragment_viewpager);
		mFragmentPagerAdapter = new ArticleFragmentPagerAdapter(
				getSupportFragmentManager());
		mFragmentViewPager.setOffscreenPageLimit(1);
		mFragmentViewPager.setAdapter(mFragmentPagerAdapter);
		mFragmentViewPager.setOnPageChangeListener(this);
		
	}
	
	private void initFragment(){
		mFragmentList.clear();
		int length = mUrlsList.size();
		for(int i = 1 ; i <= length ; i++){
			Bundle bundle = new Bundle();
			bundle.putString("key", URLs.HOST+mUrlsList.get(i-1).getMenuUrl());
			if(i == 1){
				Fragment fragment = new MainFragment();
				fragment.setArguments(bundle);
				mFragmentList.add(fragment);
			}else{
				Fragment fragment = new ArticleFragment();
				fragment.setArguments(bundle);
				mFragmentList.add(fragment);
			}
		}
		mFragmentPagerAdapter.setFragments(mFragmentList);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_DRAGGING) {
			isEnd = false;
		} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
			isEnd = true;
			if (mFragmentViewPager.getCurrentItem() == currentFragmentIndex) {
				// 未跳入下一个页面
//				mImageView.clearAnimation();
//				Animation animation = null;
//				// 恢复位置
//				animation = new TranslateAnimation(endPosition, currentFragmentIndex * item_width, 0, 0);
//				animation.setFillAfter(true);
//				animation.setDuration(1);
//				mImageView.startAnimation(animation);
				mHorizontalScrollView.invalidate();
//				endPosition = currentFragmentIndex * item_width;
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int arg2) {
		if(!isEnd){
//			if(currentFragmentIndex == position){
//				endPosition = item_width * currentFragmentIndex + 
//						(int)(item_width * positionOffset);
//			}
//			if(currentFragmentIndex == position+1){
//				endPosition = item_width * currentFragmentIndex - 
//						(int)(item_width * (1-positionOffset));
//			}
			
//			Animation mAnimation = new TranslateAnimation(beginPosition, endPosition, 0, 0);
//			mAnimation.setFillAfter(true);
//			mAnimation.setDuration(0);
//			mImageView.startAnimation(mAnimation);
			mHorizontalScrollView.invalidate();
//			beginPosition = endPosition;
		}
	}

	@Override
	public void onPageSelected(int position) {
//		Animation animation = new TranslateAnimation(endPosition, position* item_width, 0, 0);
//		
//		beginPosition = position * item_width;
		RelativeLayout rel = (RelativeLayout)mLinearLayout.getChildAt(currentFragmentIndex);
		rel.getChildAt(1).setVisibility(View.INVISIBLE);
		((TextView)rel.getChildAt(0)).setTextColor(getResources().getColor(R.color.title_color));
		currentFragmentIndex = position;
		RelativeLayout rel2 = (RelativeLayout)mLinearLayout.getChildAt(currentFragmentIndex);
		rel2.getChildAt(1).setVisibility(View.VISIBLE);
		((TextView)rel2.getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color_green));
//		if (animation != null) {
//			animation.setFillAfter(true);
//			animation.setDuration(0);
//			mImageView.startAnimation(animation);
		int scrollX = 0;
		for(int i = 0 ;i < currentFragmentIndex;i++){
			scrollX += mLinearLayout.getChildAt(i).getWidth();
		}
			mHorizontalScrollView.smoothScrollTo(scrollX, 0);
//		}
	}
	

	@SuppressWarnings("unchecked")
	private ArrayList<NavBean> getMenuList(){
		List<NavBean> list  = mKJDb.findAllByWhere(NavBean.class, "show = \'1\'");
		if(list!=null && list.size()>0){
			return (ArrayList<NavBean>) list;
		}else{
			list = new ArrayList<NavBean>();
		}
		String[] menuName = getResources().getStringArray(R.array.menu_str);
		String[] menuUrl = getResources().getStringArray(R.array.menu_url);
		for(int i = 0 ; i<menuName.length ; i++){
			NavBean bean = new NavBean();
			bean.setMd5(MD5Utils.md5(menuName[i]+menuUrl[i]));
			bean.setMenu(menuName[i]);
			bean.setMenuUrl(menuUrl[i]);
			if(i<6){
				bean.setShow("1");
				list.add(bean);
			}else{
				bean.setShow("0");
			}
			bean.setCategory(1);
			mKJDb.save(bean);
		}
		return (ArrayList<NavBean>)list;
	}
	
	private void initMenu(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		mLinearLayout = (LinearLayout) findViewById(R.id.hsv_content);
		mLinearLayout.removeAllViews();
		mUrlsList = getMenuList();
		int length = mUrlsList.size();
		for (int i = 0 ; i < length ; i++) {
			RelativeLayout layout = new RelativeLayout(this);
			ImageView mImageView = new ImageView(this);
			TextView view = new TextView(this);
			view.setText(mUrlsList.get(i).getMenu());
			view.setTextSize(getResources().getDimension(R.dimen.title_size));
			view.setGravity(Gravity.CENTER);
			if(i==0){
				view.setTextColor(getResources().getColor(R.color.text_color_green));
			}else{
				view.setTextColor(getResources().getColor(R.color.title_color));
			}
			int itemWidth = (int) (view.getPaint().measureText(mUrlsList.get(i).getMenu())+getResources().getDimension(R.dimen.title_add_width));
			RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			layout.addView(view,0, params);
			mImageView.setBackgroundColor(getResources().getColor(R.color.text_color_green));
			RelativeLayout.LayoutParams params2 =  new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.title_img_height));
			params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layout.addView(mImageView,1,params2);
			if(i==0){
				mImageView.setVisibility(View.VISIBLE);
			}else{
				mImageView.setVisibility(View.INVISIBLE);
			}
			mLinearLayout.addView(layout, itemWidth, (int)getResources().getDimension(R.dimen.title_height));
			layout.setOnClickListener(this);
			layout.setTag(i);
		}
	}
	
	

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onClick(View v) {
		Monitor.onViewClick(v);
		mFragmentViewPager.setCurrentItem((Integer)v.getTag());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/** 一个鄙人感觉不错的退出体验*/
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            exitTime = System.currentTimeMillis();   
	            Toast.makeText(MainActivity.this, "再按一次退出悦读圈", Toast.LENGTH_SHORT).show();
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
		return super.onKeyDown(keyCode, event);
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.drag_soft_img){
			detector.onTouchEvent(event);
		}
		return false;
	}


	@Override
	public boolean onDown(MotionEvent e) {
		mDragImgHeight = mDragSoftImg.getHeight();
		mDragImgWidth = mDragSoftImg.getWidth();
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		
	}



	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
//		RelativeLayout.LayoutParams params = (LayoutParams) mDragSoftImg.getLayoutParams();
//		params.setMargins((int)(mDragSoftImg.getLeft()+distanceX), (int)mDragSoftImg.getTop(), 
//				(int)(mDragSoftImg.getLeft()+distanceX+mDragImgWidth), (int)(mDragSoftImg.getTop()+mDragImgHeight));
//		mDragSoftImg.setLayoutParams(params);
//		mDragSoftImg.requestLayout();
		return false;
	}



	@Override
	public void onLongPress(MotionEvent e) {
		
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY) {
//		if (event1.getRawX() > event2.getRawX()) {
//		    Toast.makeText(this, "swipe left",Toast.LENGTH_SHORT).show();
//		   } else {
//		   Toast.makeText(this, "swipe right",Toast.LENGTH_SHORT).show();
//		   }
		return true;
	}
	
	private void setDector(){
		detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() { 
           //短快的点击算一次单击 
            @Override 
            public boolean onSingleTapConfirmed(MotionEvent e) { 
//            	Toast.makeText(MainActivity.this, "单击",Toast.LENGTH_SHORT).show();
                return false; 
            } 
            //双击时产生一次 
            @Override 
            public boolean onDoubleTap(MotionEvent e) { 
//            	Toast.makeText(MainActivity.this, "双击",Toast.LENGTH_SHORT).show();
                return false; 
            } 
          //双击时产生两次 
            @Override 
            public boolean onDoubleTapEvent(MotionEvent e) { 
                return false; 
            } 
        }); 
	}
	
	
	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog(){
		dialog = new CustomDialog(this,R.style.custom_dialog);
		View view = View.inflate(this, R.layout.custom_dialog, null);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setView(view);
		((TextView)view.findViewById(R.id.pop_title)).setText("是否退出");
		((TextView)view.findViewById(R.id.tv_pop_win_text)).setVisibility(View.GONE);

//		SplashView splashView = new SplashView(this, null);
//		// 设置是否显示倒数
//		splashView.setShowReciprocal(false);
//		// 隐藏关闭按钮
//		splashView.hideCloseBtn(false);
//
////		Intent intent = new Intent(this, MineActivity.class);
////		splashView.setIntent(intent);
//		splashView.setIsJumpTargetWhenFail(false);
//
//		View splash;
//		splash = splashView.getSplashView();
//		RelativeLayout splashLayout = ((RelativeLayout) view.findViewById(R.id.ad_ll));
//		splashLayout.setVisibility(View.GONE);
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
//		splashLayout.addView(splash, params);
//		SpotManager.getInstance(this).showSplashSpotAds(this, splashView,new SpotDialogListener() {
//			
//			@Override
//			public void onSpotClosed() {
//			}
//			
//			@Override
//			public void onSpotClick() {
//			}
//			
//			@Override
//			public void onShowSuccess() {
//			}
//			@Override
//			public void onShowFailed() {
//			}
//		});
		TextView cancel = (TextView)view.findViewById(R.id.tv_cancel);
		cancel.setText("取消");
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog != null && dialog.isShowing()){
					dialog.cancel();
				}
			}
		});

		TextView commit = (TextView)view.findViewById(R.id.tv_commit);
		commit.setText("退出");
		commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog != null && dialog.isShowing()){
					dialog.cancel();
				}
				finish();
	            System.exit(0);
			}
		});
		dialog.show();
		
//		builder.setMessage("待测版本"+mUpdate.getVersionCode()+"\n\n"+mUpdate.getUpdateLog());
	}
	
	private void setAlarm(){
		  Intent intent=new Intent(this,CheckUpdateReceiver.class);
		  //create the Intent between activity and broadcast
		  
		  this.alarm=(AlarmManager)super.getSystemService(Context.ALARM_SERVICE);
		   //get the alarm service
		  this.calendar.set(Calendar.HOUR_OF_DAY,HourOfDay);
		  //set the hour of the calendar to the value that we want 
		  this.calendar.set(Calendar.MINUTE, 0);
		  //set the minute of the calendar to 0
		  this.calendar.set(Calendar.SECOND, 0);
		  //set the minute of the calendar to 0
		  this.calendar.set(Calendar.MILLISECOND,0);
		  //set the millsecond of the calendar to 0
		 
		 
		  intent.setAction("com.sepcialfocus.android.check.article");//define the action of intent
		  PendingIntent sender=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		  //define the PendingIntent
		  this.alarm.setRepeating(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis()+1000*60*60*7,
		  this.TIME_INTERVAL,sender);//set the properties of alarm
		                                      //send broatcast at the same time
	}
	
	private final int HourOfDay=8;//定时更新的小时
    
    private final int TIME_INTERVAL=1000*60*60*8;//set the interval of the alarm repeating
	private AlarmManager alarm=null;
	private Calendar calendar=Calendar.getInstance();//Calendar是一类可以将时间转化成绝对时间
}

