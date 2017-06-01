/**
 * 工程名: MainActivity
 * 文件名: HistoryActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-9-27上午9:18:39
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;

import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.ui.article.ArticleDbFragment;

/**
 * 类名: HistoryActivity <br/>
 * 功能: 阅读历史. <br/>
 * 日期: 2015-9-27 上午9:18:39 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class HistoryActivity extends BaseFragmentActivity implements View.OnClickListener{
	ArticleDbFragment fragment = null;
	Boolean isHistory = true;

	TextView mTitleTv;
	ImageView mBackImg;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_fragment_container);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		isHistory = getIntent().getBooleanExtra("key", true);
		
		fragment = new ArticleDbFragment();
		Bundle bundle = new Bundle();
		if(isHistory){
			mTitleTv.setText(getString(R.string.mine_history_str));
			bundle.putBoolean("key", true);
		} else{
			mTitleTv.setText(getString(R.string.mine_favorite_str));
			bundle.putBoolean("key", false);
		}
		fragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_title_back:
			finish();
			break;
		}
		
	}

	
	
	
}

