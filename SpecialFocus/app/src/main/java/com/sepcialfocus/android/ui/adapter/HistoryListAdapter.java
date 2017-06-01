/**
 * 工程名: MainActivity
 * 文件名: HistoryListAdapter.java
 * 包名: com.sepcialfocus.android.ui.adapter
 * 日期: 2015-9-27上午10:39:58
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mike.aframe.bitmap.KJBitmap;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.bean.HistroyItemBean;
import com.sepcialfocus.android.configs.AppConfig;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.ui.adapter.ArticleListAdapter.ViewHolder;

/**
 * 类名: HistoryListAdapter <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-27 上午10:39:58 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class HistoryListAdapter extends BaseAdapter{
	ArrayList<HistroyItemBean> mList;
	Context mContext;
	KJBitmap kjBitMap = null;
	
	public HistoryListAdapter(Context context, ArrayList<HistroyItemBean> list){
		this.mContext = context;
		this.mList = list;
		kjBitMap = KJBitmap.create();
	}

	@Override
	public int getCount() {
		return mList!=null ? mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null ? mList.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_article, null);
			holder = new ViewHolder();
			holder.mArticleImg = (ImageView)convertView.findViewById(R.id.article_img);
			holder.mArticleTitleTv = (TextView)convertView.findViewById(R.id.article_title);
			holder.mArticleContentTv = (TextView)convertView.findViewById(R.id.article_content);
			holder.mArticleTagOneTv = (TextView)convertView.findViewById(R.id.article_from1);
			holder.mArticleTagTwoTv = (TextView)convertView.findViewById(R.id.article_from2);
			holder.mArticleTagThrTv = (TextView)convertView.findViewById(R.id.article_from3);
			holder.mArticleDateTv = (TextView)convertView.findViewById(R.id.article_date);
			convertView.setTag(holder);
		}
		holder = (ViewHolder)convertView.getTag();
		HistroyItemBean bean  = mList.get(position);
		holder.mArticleTitleTv.setText(bean.getTitle()+"");
		holder.mArticleContentTv.setText(bean.getSummary()+"");
		holder.mArticleDateTv.setText(bean.getDate()+"");
		ArrayList<String> tags = bean.getTags();
		holder.mArticleTagOneTv.setVisibility(View.INVISIBLE);
		holder.mArticleTagTwoTv.setVisibility(View.INVISIBLE);
		holder.mArticleTagThrTv.setVisibility(View.INVISIBLE);
		holder.mArticleImg.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.default_img));
		if(tags!=null && tags.size()>0){
			int length = tags.size();
			if(length > 3){
				length = 3;
			}
			switch(length){
			case 3:
				holder.mArticleTagThrTv.setVisibility(View.VISIBLE);
				holder.mArticleTagThrTv.setText(tags.get(2));
			case 2:
				holder.mArticleTagTwoTv.setVisibility(View.VISIBLE);
				holder.mArticleTagTwoTv.setText(tags.get(1));
			case 1:
				holder.mArticleTagOneTv.setVisibility(View.VISIBLE);
				holder.mArticleTagOneTv.setText(tags.get(0));
				break;
			}
		}
		if(AppConfig.imgFlag){
			if(!"".equals(bean.getImgUrl()+"") && !(bean.getImgUrl()+"").contains("defaultpic.gif")){
				kjBitMap.display(holder.mArticleImg,URLs.HOST+bean.getImgUrl(),
						holder.mArticleImg.getWidth(),holder.mArticleImg.getHeight());
			}
		}else{
			holder.mArticleImg.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.default_img));
		}
		return convertView;
	}
	
	static class ViewHolder{
		ImageView mArticleImg;
		TextView mArticleTitleTv;
		TextView mArticleContentTv;
		TextView mArticleDateTv;
		TextView mArticleTagOneTv;
		TextView mArticleTagTwoTv;
		TextView mArticleTagThrTv;
		TextView mArticleLabelTv;
	}
}

