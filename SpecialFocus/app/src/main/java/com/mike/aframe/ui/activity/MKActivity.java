/**
 * 工程名: MikeLibs
 * 文件名: MKActivity.java
 * 包名: com.mike.aframe.ui.activity
 * 日期: 2015-3-16下午4:12:14
 * Mail: ammike@163.com.
 * QQ: 378640336
 * http://www.cnblogs.com/ammike/
 *
*/

package com.mike.aframe.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.mike.aframe.MKLog;
import com.mike.aframe.ui.AnnotateUtil;

/**
 * 类名: MKActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-3-16 下午4:12:14 <br/>
 *
 * @author   mike
 * @version  	 
 */
public abstract class MKActivity extends FragmentActivity implements 
	I_KJActivity,OnClickListener{
		
	
	protected abstract void initData();
	
	protected abstract void initWidget();
	
	protected abstract void init();
	@Override
	public void initialize() {
		init();
		initData();
		initWidget();
	}
	
	@Override
	public abstract void setRootView();
	
	@Override
	public abstract void widgetClick(View v);
	
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRootView();
		MKLog.state(this.getClass().getName(), "---------onCreat ");
		AnnotateUtil.initBindView(this);
		initialize();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		MKLog.state(this.getClass().getName(), "---------onRestart");
	}
	
	@Override
	protected void onDestroy() {
		MKLog.state(this.getClass().getName(), "---------onDestory");
		super.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		MKLog.state(this.getClass().getName(), "---------onLowMemory");
		super.onLowMemory();
	}
	
	@Override
	protected void onPause() {
		MKLog.state(this.getClass().getName(), "---------onPause");
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MKLog.state(this.getClass().getName(), "---------onResume");
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		MKLog.state(this.getClass().getName(),"---------onStart");
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		MKLog.state(this.getClass().getName(),"---------onStop");
		super.onStop();
	}
	
	@Override
	public void finish() {
		MKLog.state(this.getClass().getName(),"---------onFinish");
		super.finish();
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	
}

