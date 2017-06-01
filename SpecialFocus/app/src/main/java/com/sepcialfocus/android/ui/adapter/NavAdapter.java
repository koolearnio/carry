/**
 * 工程名: MainActivity
 * 文件名: NavAdapter.java
 * 包名: com.sepcialfocus.android.ui.adapter
 * 日期: 2015-9-4下午9:54:35
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
import android.widget.TextView;

import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.NavBean;

/**
 * 类名: NavAdapter <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-4 下午9:54:35 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class NavAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<NavBean> mMenuList;
	
	public NavAdapter(Context context,ArrayList<NavBean> list){
		this.mContext = context;
		this.mMenuList = list;
	}
	
	@Override
	public int getCount() {
		
		// TODO Auto-generated method stub
		return mMenuList!=null ? mMenuList.size():0;
	}

	@Override
	public Object getItem(int position) {
		
		// TODO Auto-generated method stub
		return mMenuList!=null ? mMenuList.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu,null);
			holder= new ViewHolder();
			holder.menuTv = (TextView)convertView.findViewById(R.id.menu);
			convertView.setTag(holder);
		}
		holder = (ViewHolder)convertView.getTag();
		NavBean bean = mMenuList.get(position);
		holder.menuTv.setText(bean.getMenu());
		
		return convertView;
	}

	static class ViewHolder{
		TextView menuTv;
		TextView selectedTag;
	}
}

