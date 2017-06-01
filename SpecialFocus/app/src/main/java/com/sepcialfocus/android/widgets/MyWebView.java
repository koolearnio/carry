/**
 * 工程名: MainActivity
 * 文件名: MyWebView.java
 * 包名: com.sepcialfocus.android.widgets
 * 日期: 2015年9月28日下午3:20:38
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;

/**
 * 类名: MyWebView <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015年9月28日 下午3:20:38 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MyWebView extends WebView{
	Context mContext;
	
	private float scale_temp;// 缩放比例

	private int mode = 0;// 操作标志：0-无操作 1-拖拽 20-等比例缩放 21-水平缩放 22-竖直缩放 
	
	private float beforeLenght, beforeLenght_X, beforeLenght_Y, afterLenght,
    afterLenght_X, afterLenght_Y;// 两触点距离
  
	private int firstX,firstY,secondX,secondY,leftInc=0,rightInc=0,topInc=0,bottomInc=0;

	public MyWebView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	public MyWebView(Context context,AttributeSet set){
		super(context,set);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {//多点触摸，获取下一个动作
	      case MotionEvent.ACTION_DOWN:
	        firstX=(int) event.getX();
	        firstY=(int) event.getY();
	        onTouchDown(event);
	        postInvalidate();
	        break;
	      case MotionEvent.ACTION_POINTER_DOWN:// 另一个触摸点按下
	        secondX=(int) event.getX(1);
	        secondY=(int) event.getY(1);
	        if((secondX>=0)&&(secondY>=0)){
	          onPointerDown(event);
	        }
	        break;

	      case MotionEvent.ACTION_MOVE:
	        onTouchMove(event);
	        break;
	      case MotionEvent.ACTION_UP:
	        mode = 0;
	        break;
	      case MotionEvent.ACTION_POINTER_UP:
	        mode = 0;
	        break;
	      }

	    return true;
	  }
	
	private void onTouchDown(MotionEvent event){
		mode = 1;

	}
	
	private void onTouchMove(MotionEvent event){
		if (mode == 20) {
		      afterLenght = getDistance(event);
		      float gapLenght = afterLenght - beforeLenght;// 变化的长度
		      if (Math.abs(gapLenght) > 5f) {
		        scale_temp = afterLenght / beforeLenght;// 求的缩放的比例
		        this.setScale(scale_temp);
		        beforeLenght = afterLenght;
		      }
		      /** 水平缩放 **/
		    } else if (mode == 21) {
		      afterLenght_X = getDistance_X(event);
		      float gapLenght_X = afterLenght_X - beforeLenght_X;// 变化的长度
		      if (Math.abs(gapLenght_X) > 3f) {
		        scale_temp = afterLenght_X / beforeLenght_X;// 求的缩放的比例
		        this.setScale(scale_temp);
		        beforeLenght_X = afterLenght_X;
		      }
		      /** 豎直缩放 **/
		    } else if (mode == 22) {
		      afterLenght_Y = getDistance_Y(event);
		      float gapLenght_Y = afterLenght_Y - beforeLenght_Y;// 变化的长度
		      if (Math.abs(gapLenght_Y) > 3f) {
		        scale_temp = afterLenght_Y / beforeLenght_Y;// 求的缩放的比例
		        this.setScale(scale_temp);
		        beforeLenght_Y = afterLenght_Y;
		      }
		   }
	}
	
	private void onPointerDown(MotionEvent event){
		 if (event.getPointerCount() == 2) {
		      int x_long = (int) Math.abs(event.getX(0) - event.getX(1));
		      int y_long = (int) Math.abs(event.getY(0) - event.getY(1));
		      if (y_long == 0)
		        y_long = 1;//分母不得为零
		      double xdy = x_long / y_long;
		      if (xdy >= 2f){
		        int smfX=(int) (event.getX(1)-event.getX(0));
		        leftInc=(smfX>0)?(0):(1);
		        rightInc=(smfX>0)?(1):(0);
		        mode = 21;
		        }
		      else if (xdy <= 0.5f){
		        int smfY=(int) (event.getY(1)-event.getY(0));
		        topInc=(smfY>0)?(0):(1);
		        bottomInc=(smfY>0)?(1):(0);
		        mode = 22;
		        }
		      else{
		        int smfX=(int) (event.getX(1)-event.getX(0));
		        leftInc=(smfX>0)?(0):(1);
		        rightInc=(smfX>0)?(1):(0);
		        int smfY=(int) (event.getY(1)-event.getY(0));
		        topInc=(smfY>0)?(0):(1);
		        bottomInc=(smfY>0)?(1):(0);
		        mode = 20;/*根据x/y的竖直判断缩放类型*/
		      }
		      beforeLenght = getDistance(event);// 获取两点的距离
		      beforeLenght_X = getDistance_X(event);// 获取两点的X距离
		      beforeLenght_Y = getDistance_Y(event);// 获取两点的Y距离
	    }
	}
	
	 /** 获取两点的距离 **/
	  float getDistance(MotionEvent event) {
	    float x = firstX - event.getX(1);
	    float y = firstY - event.getY(1);
	    return (float)Math.sqrt(x * x + y * y);
	  }

	  float getDistance_X(MotionEvent event) {
	    float x = firstX - event.getX(1);
	    return Math.abs(x);
	  }

	  float getDistance_Y(MotionEvent event) {
	    float y = firstY - event.getY(1);
	    return Math.abs(y);
	  }
	  
	  private void setScale(float scale){
		  int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
	      int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// 获取缩放垂直距离
	      
	      if(scale>1){
	    	  setLagger(1);
	      }else{
	    	  setLagger(-1);
	      }
	      
	  }
	  
	  
	  private void setLagger(int i){
		  TextSize size = this.getSettings().getTextSize();
		  if(size == TextSize.SMALLEST){
			  if(i>0){
				  this.getSettings().setTextSize(TextSize.SMALLER);
			  }
		  }else if(size == TextSize.SMALLER){
			  if(i<0){
				  this.getSettings().setTextSize(TextSize.SMALLEST);
			  }else{
				  this.getSettings().setTextSize(TextSize.NORMAL);
			  }
			  
		  }else if(size == TextSize.NORMAL){
			  if(i<0){
				  this.getSettings().setTextSize(TextSize.SMALLER);
			  }else{
				  this.getSettings().setTextSize(TextSize.LARGER);
			  }
		  }else if(size == TextSize.LARGER){
			  if(i<0){
				  this.getSettings().setTextSize(TextSize.NORMAL);
			  }else{
				  this.getSettings().setTextSize(TextSize.LARGEST);
			  }
		  }else if(size == TextSize.LARGEST){
			  if(i<0){
				  this.getSettings().setTextSize(TextSize.LARGER);
			  }else{
				  this.getSettings().setTextSize(TextSize.LARGEST);
			  }
		  }
		  
		  
	  }
	  
	  
	  
}

