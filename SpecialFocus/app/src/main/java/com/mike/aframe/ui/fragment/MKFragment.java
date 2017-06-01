/**
 * 工程名: MikeLibs
 * 文件名: MKFragment.java
 * 包名: com.mike.aframe.ui.fragment
 * 日期: 2015-3-16下午4:14:23
 * Mail: ammike@163.com.
 * QQ: 378640336
 * http://www.cnblogs.com/ammike/
 *
*/

package com.mike.aframe.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mike.aframe.ui.AnnotateUtil;

/**
 * 类名: MKFragment <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-3-16 下午4:14:23 <br/>
 *
 * @author   mike
 * @version  	 
 */
public abstract class MKFragment extends Fragment implements View.OnClickListener{
	/** 传递过来的参数 */
	protected Bundle mBundle;
	
	/**
	 * initData:(数据初始化). <br/>
	 */
	protected abstract void initData();

	/**
	 * initWidget:(控件初始化，在数据初始化之后). <br/>
	 */
	protected abstract void initWidget(View view);
	
	/**
	 * widgetClick:(点击事件的添加). <br/>
	 */
	protected abstract void widgetClick(View v);
	
	/**
	 * 
	 * inflaterView:(fragment 布局文件的设置). <br/>
	 *
	 */
	protected abstract View inflaterView(LayoutInflater inflater,
            ViewGroup container, Bundle bundle);
	
	 @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflaterView(inflater, container,
                savedInstanceState);
        AnnotateUtil.initBindView(this, view);
        if(this.getArguments()!=null){
        	mBundle = this.getArguments();
        }
        initData();
        initWidget(view);
        return view;
    }
	 
	 /**
     * showWaittingDialog:(显示通讯框). <br/>
     * 
     * fragment的 生命周期onCreateView（）中调用 .<br/>
     * 且在线程结束时会调用dismissWaittingDialog() .<br/>
     * @since 1.0
     */
    public abstract void showWaittingDialog();
    
    /** 通讯框消失 */
    public abstract void dismissWaittingDialog();

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}
    
    
}

