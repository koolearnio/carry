package com.sepcialfocus.android.ui.adapter;

import java.util.ArrayList;

import com.sepcialfocus.android.bean.NavBean;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.ui.article.ArticleFragment;
import com.sepcialfocus.android.ui.article.MainFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ArticleFragmentAdapter extends FragmentStatePagerAdapter{

	private ArrayList<NavBean> mUrlsList = null;
	public ArticleFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void setUrlsList(ArrayList<NavBean> mUrlList){
		this.mUrlsList = mUrlList;
	}

	@Override
	public Fragment getItem(int i) {
		Bundle bundle = new Bundle();
		bundle.putString("key", URLs.HOST+mUrlsList.get(i).getMenuUrl());
		if(i == 0){
			Fragment fragment = new MainFragment();
			fragment.setArguments(bundle);
			return fragment;
		}else{
			Fragment fragment = new ArticleFragment();
			fragment.setArguments(bundle);
			return fragment;
		}
	}

	@Override
	public int getCount() {
		return mUrlsList!=null ? mUrlsList.size():0;
	}

}
