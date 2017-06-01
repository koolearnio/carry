/**
 * 工程名: YybLibrary
 * 文件名: TimeCount.java
 * 包名: org.yyb.aframe.utils
 * 日期: 2015-2-5上午11:41:44
 * Copyright (c) 2015, 北京巨翔科技有限公司 All Rights Reserved.
 * 官网：http://www.wjuxiang.com/
 *
*/

package com.mike.aframe.utils;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 类名: TimeCount <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-2-5 上午11:41:44 <br/>
 *
 * @author   leixun
 * @email leixun33@163.com
 * @version  	 
 */
public class TimeCount extends CountDownTimer{

	/**计时按钮*/
	private TextView mClickView = null;
	/**当前时间*/
	private long countTime;
	/**计时时间*/
	private long time;
	
	private Context mContext;
	
	private FinishListener finish;
	
	public interface FinishListener{
		public void onFinish(int time);
	}
	
	public TimeCount(Context context,long millisInFuture, long countDownInterval,FinishListener finishListener) {
		
		super(millisInFuture, countDownInterval);
		time = millisInFuture;
		this.mContext = context;
		this.finish = finishListener;
	}

	private void setView(TextView view){
		this.mClickView = view;
	}
	/**
	 * 
	 * TODO 计时完成.
	 * @see android.os.CountDownTimer#onFinish()
	 */
	@Override
	public void onFinish() {
		if (finish!=null){
			finish.onFinish((int)((time-countTime)/1000)+1);
		}
	}
	/**
	 * 计时中
	 * @see android.os.CountDownTimer#onTick(long)
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		countTime = millisUntilFinished;
		mClickView.setText(millisUntilFinished/1000+"\"");
		mClickView.setClickable(false);
	}
	/**
	 * 
	 * count:(开始计时). <br/>
	 */
	public void count(TextView view){
		this.setView(view);
		this.start();
	}
	
	public int stop(){
		cancel();
		return (int)((time-countTime)/1000)+1;
	}
}

