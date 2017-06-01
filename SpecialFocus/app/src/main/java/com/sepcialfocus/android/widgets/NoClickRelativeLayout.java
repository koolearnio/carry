/**
 * 工程名: MainActivity
 * 文件名: NoClickRelativeLayout.java
 * 包名: com.sepcialfocus.android.widgets
 * 日期: 2015年10月12日下午4:34:59
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 类名: NoClickRelativeLayout <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015年10月12日 下午4:34:59 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class NoClickRelativeLayout extends RelativeLayout{

	public NoClickRelativeLayout(Context context, AttributeSet attrs) {
		
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// TODO Auto-generated method stub
		return true;
	}

	
}

