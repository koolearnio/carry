/**
 * 工程名: MainActivity
 * 文件名: FontSizeActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-10-5上午9:29:32
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.mike.aframe.utils.PreferenceHelper;
import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;

/**
 * 类名: FontSizeActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-10-5 上午9:29:32 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class FontSizeActivity extends BaseFragmentActivity implements View.OnClickListener{

	ImageView mBackImg;
	TextView mTitleTv;
	int textSize;
	RadioGroup rg;
	RadioButton rb;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main_text_size);
		textSize = PreferenceHelper.readInt(this, 
    			AppConstant.TEXTSIZE, AppConstant.TEXTSIZE,3);
		initView();
	}
	
	protected void initView(){
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		mTitleTv.setText(getResources().getString(R.string.setting_font_size_str));
		rg = (RadioGroup)findViewById(R.id.content_text_size_rg);
		rg.check(getId());
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.text_smallest_rb:
					textSize = 1;
					break;
				case R.id.text_smaller_rb:
					textSize = 2;
					break;
				case R.id.text_normal_rb:
					textSize = 3;
					break;
				case R.id.text_larger_rb:
					textSize = 4;
					break;
				case R.id.text_largest_rb:
					textSize = 5;
					break;
				}
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_title_back:
			finish();
			break;
		}
	}


	public void onPause(){
		super.onPause();
		PreferenceHelper.write(this, 
    			AppConstant.TEXTSIZE, AppConstant.TEXTSIZE,textSize);
	}
	
	private int getId(){
		switch(textSize){
		case 1:
			return R.id.text_smallest_rb;
		case 2:
			return R.id.text_smaller_rb;
		case 3:
			return R.id.text_normal_rb;
		case 4:
			return R.id.text_larger_rb;
		case 5:
			return R.id.text_largest_rb;
			default: return R.id.text_normal_rb;
		}
	}
	
}

