/**
 * ������: SpecialFocus
 * �ļ���: BaseFragment.java
 * ����: com.sepcialfocus.android
 * ����: 2015-9-1����8:48:02
 * Copyright (c) 2015, ����С����ӽ����Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android;


import com.mike.aframe.MKLog;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 类名: BaseFragment <br/>
 * 功能描述: TODO 添加功能描述. <br/>
 * 日期: 2015年9月2日 下午5:25:20 <br/>
 *
 * @author leixun
 * @version 
 */
public abstract class BaseFragment extends Fragment{
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
	protected abstract void initView();
	
    /** 
     * 在这里实现Fragment数据的缓加载. 
     * @param isVisibleToUser 
     */  
    @Override  
    public void setUserVisibleHint(boolean isVisibleToUser) {  
        super.setUserVisibleHint(isVisibleToUser);  
//        if(getUserVisibleHint()) {  
//            isVisible = true;  
//            onVisible();  
//        } else {  
//            isVisible = false;  
//            onInvisible();  
//        }  
    }  
  
//    protected void onVisible(){  
//        lazyLoad();  
//    }  
//  
//    protected abstract void lazyLoad();  
  
    protected void onInvisible(){}  
	
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

}

