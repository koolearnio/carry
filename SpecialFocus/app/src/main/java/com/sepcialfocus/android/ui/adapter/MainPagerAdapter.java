/**
 * ������: SpecialFocus
 * �ļ���: MainPagerAdapter.java
 * ����: com.sepcialfocus.android.ui.adapter
 * ����: 2015-9-1����9:38:19
 * Copyright (c) 2015, ����С���ӽ���Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.adapter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.sepcialfocus.android.bean.NavBean;
import com.sepcialfocus.android.configs.URLs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ����: MainPagerAdapter <br/>
 * ����: TODO ��ӹ�������. <br/>
 * ����: 2015-9-1 ����9:38:19 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MainPagerAdapter extends FragmentPagerAdapter{

	private ArrayList<String> fragmentNameList;
	private ArrayList<Fragment> fragmentList;
	private ArrayList<NavBean> urlsList;
	
	public MainPagerAdapter(FragmentManager fm,ArrayList<String> fragmentNameList,ArrayList<NavBean> urlList) {
		super(fm);
		this.fragmentNameList = fragmentNameList;
		fragmentList = new ArrayList<Fragment>();
		this.urlsList = urlList;
	}

	@Override
	public Fragment getItem(int position) {
		if(fragmentList.size() <= position){
			if (fragmentList.size() > position && fragmentList.get(position)!=null){
				return fragmentList.get(position);
			}
			for(int i = 0 ; i < getCount() ; i++){
				String className = fragmentNameList.get(i);
				Class clazz;
				try{
					clazz = Class.forName(className);
					Constructor constructor = clazz.getConstructor();
					Fragment fragment = (Fragment)constructor.newInstance();
					Bundle bundle = new Bundle();
					bundle.putString("key", URLs.HOST+urlsList.get(i).getMenuUrl());
					fragment.setArguments(bundle);
					fragmentList.add(i,fragment);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return fragmentList!=null ? fragmentList.get(position):null;
	}

	
	
	@Override
	public int getCount() {
		return fragmentNameList!=null ? fragmentNameList.size():0;
	}

}
