package com.sepcialfocus.android.widgets;

import com.sepcialfocus.android.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * 自定义对话框
 * 
 */
public class CustomDialog extends Dialog {

	protected CustomDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		
	}

	/** 显示的视图 */
	private View view = null;

	/***
	 * 屏幕变暗
	 */
	public CustomDialog(Context context) {
		super(getParent(context));
		getWindow().setBackgroundDrawableResource(R.color.blank);
		setCanceledOnTouchOutside(false);
	}

	/***
	 * 屏幕不变暗
	 * 
	 * @param context
	 * @param style
	 */
	public CustomDialog(Context context, int style) {
		super(getParent(context), style);
		getWindow().setBackgroundDrawableResource(R.color.blank);
		setCanceledOnTouchOutside(true);
	}
	
	/**
	 * 
	 * Creates a new instance of CustomDialog.
	 *	(用户引导页面调用此构造)
	 * @param context
	 * @param style
	 * @param gravity
	 * @param x
	 * @param y
	 */
	public CustomDialog(Context context, int style,int gravity,int x,int y) {
		super(getParent(context), style);
		Window win = getWindow();
		win.setBackgroundDrawableResource(R.drawable.blank);
		WindowManager.LayoutParams layout = win.getAttributes();
		win.setGravity(gravity);
		layout.x = x;
		layout.y = y;
		win.setAttributes(layout);
		setCanceledOnTouchOutside(true);
	}

	private static Context getParent(Context context) {
		Context re = context;
		if (context instanceof Activity) {
			Activity a = ((Activity) context);
			if (a.isChild()) {
				a = (Activity) getParent(a.getParent());
			}
			re = a;
		}
		return re;
	}

	/**
	 * 设置显示的VIew
	 * 
	 * @param view
	 */
	public void setView(View view) {
		this.view = view;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (view != null) {
			setContentView(view);
		}
	}
}
