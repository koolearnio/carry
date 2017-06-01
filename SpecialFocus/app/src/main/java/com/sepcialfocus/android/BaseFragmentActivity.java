/**
 * ������: SpecialFocus
 * �ļ���: BaseFragmentActivity.java
 * ����: com.sepcialfocus.android.ui
 * ����: 2015-9-1����9:39:16
 * Copyright (c) 2015, ����С����ӽ����Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.mike.aframe.MKLog;
import com.umeng.analytics.MobclickAgent;

/**
 * ����: BaseFragmentActivity <br/>
 * ����: TODO ��ӹ�������. <br/>
 * ����: 2015-9-1 ����9:39:16 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class BaseFragmentActivity extends FragmentActivity{
	protected RelativeLayout mLoadingLayout;
	protected RelativeLayout mNoNetLayout;
	
	/**
	 * 
	 * initView:(这里用一句话描述这个方法的功能). <br/>
	 *
	 * @author leixun
	 * 2015年9月2日下午5:36:02
	 * @since 1.0
	 */
	protected void initView(){
		mLoadingLayout = (RelativeLayout)findViewById(R.id.layout_loading_bar);
	}
	
	/**
	 * 
	 * setLoadingVisible:(这里用一句话描述这个方法的功能). <br/>
	 *
	 * @author leixun
	 * 2015年9月2日下午5:25:05
	 * @param isVisible
	 * @since 1.0
	 */
	protected void setLoadingVisible(boolean isVisible){
		if(mLoadingLayout!=null){
			if(isVisible){
				mLoadingLayout.setVisibility(View.VISIBLE);
			}else{
				mLoadingLayout.setVisibility(View.GONE);
			}
		} else {
			MKLog.d(BaseFragment.class.getSimpleName(), "mLoadingLayout is null");
		}
	}
	
	protected void setNoNetVisible(boolean isVisible){
		if(mLoadingLayout!=null){
			if(isVisible){
				mNoNetLayout.setVisibility(View.VISIBLE);
			}else{
				mNoNetLayout.setVisibility(View.GONE);
			}
		} else {
			MKLog.d(BaseFragment.class.getSimpleName(), "mNoNetLayout is null");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	

}

