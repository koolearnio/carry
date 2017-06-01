/**
 * 工程名: MainActivity
 * 文件名: ArticleDbFragment.java
 * 包名: com.sepcialfocus.android.ui.article
 * 日期: 2015-9-27上午10:31:10
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.article;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.DensityUtils;
import com.sepcialfocus.android.BaseFragment;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.HistroyItemBean;
import com.sepcialfocus.android.bean.NavBean;
import com.sepcialfocus.android.ui.adapter.HistoryListAdapter;
import com.sepcialfocus.android.utils.SettingsManager;
import com.sepcialfocus.android.widgets.swiptlistview.SwipeListView;

/**
 * 类名: ArticleDbFragment <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-27 上午10:31:10 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ArticleDbFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
	private SwipeRefreshLayout mSwipeLayout;
	private ArrayList<HistroyItemBean> mArticleList;
	private SwipeListView mArticle_listview;
	private HistoryListAdapter mArticleAdapter;
	private View mView;
	private Context mContext;
	
	// 加载更多
	boolean isPullRrefreshFlag;
	// 下拉刷新
	boolean isRefresh = false;
	
	private KJDB kjDb = null;
	
	private boolean isHistory = true;
	
	public ArticleDbFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		kjDb = KJDB.create(mContext);
		Bundle args = getArguments();
        if (null !=  args) {
            if (args.containsKey("key")) {
                this.isHistory = args.getBoolean("key");
            }
        }
        readNativeData();
        
	}
	
	@Override
	protected void initView() {
		mSwipeLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipe_container);
		mArticle_listview = (SwipeListView)mView.findViewById(R.id.article_listview);
		initSwapLayout();
		mLoadingLayout = (RelativeLayout)mView.findViewById(R.id.layout_loading_bar);
		mArticle_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext,ArticleDetailActivity.class);
				intent.putExtra("key", mArticleList.get(position));
				startActivity(intent);
			}
		});
		
		mArticle_listview.setOnBottomListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPullRrefreshFlag){
//					new Loadhtml(urls+nextUrl).execute("","","");
				} else {
					mArticle_listview.onBottomComplete();
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_articles, null);
		initView();
		initData();
		return mView;
	}
	
	public void notifyData(NavBean bean){
		if(bean!=null){
//			this.urls = bean.getMenuUrl();
			readNativeData();
			if(mArticle_listview== null){
				return;
			}
			mArticleAdapter = new HistoryListAdapter(mContext, mArticleList);
			mArticle_listview.setAdapter(mArticleAdapter);
		}
	}
	
	private void initData(){
		if(null==mArticleList || mArticleList.size()==0){
			new Loadhtml().execute("","","");
		}
	}


	class Loadhtml extends AsyncTask<String, String, String>
    {
        Document doc;
        public Loadhtml(){
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
            	mArticleList.clear();
                if(isHistory){
                	mArticleList.addAll(kjDb.findAll(HistroyItemBean.class));
                }else{
                	mArticleList.addAll(kjDb.findAllByWhere(HistroyItemBean.class, " hasFavor = 1"));
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            Log.d("doc", doc.toString().trim());
            setLoadingVisible(false);
            mSwipeLayout.setVisibility(View.VISIBLE);
            mArticleAdapter = new HistoryListAdapter(mContext, mArticleList);
    		mArticle_listview.setAdapter(mArticleAdapter);
            mArticle_listview.onBottomComplete();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if(mArticleList!=null 
            		&& mArticleList.size()==0){
            	setLoadingVisible(true);
            	mSwipeLayout.setVisibility(View.GONE);
            }
            if(isRefresh){
            	isRefresh = false;
            }
            mSwipeLayout.setRefreshing(false);
        }
        
    }


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void readNativeData(){
		try{
        	if(mArticleList==null){
        		mArticleList = new ArrayList<HistroyItemBean>();
            }
        }catch(Exception e){
        	e.printStackTrace();
        	mArticleList = new ArrayList<HistroyItemBean>();
        }
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
		new Loadhtml().execute("","","");
	}
	
	private void initSwapLayout(){
		mSwipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		mSwipeLayout.setOnRefreshListener(this);
		
		 SettingsManager settings = SettingsManager.getInstance();
		 mArticle_listview.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
		 mArticle_listview.setSwipeActionLeft(settings.getSwipeActionLeft());
		 mArticle_listview.setSwipeActionRight(settings.getSwipeActionRight());
		 mArticle_listview.setOffsetLeft(DensityUtils.dip2px(mContext,
	                settings.getSwipeOffsetLeft()));
		 mArticle_listview.setOffsetRight(DensityUtils.dip2px(mContext,
	                settings.getSwipeOffsetRight()));
		 mArticle_listview.setAnimationTime(settings.getSwipeAnimationTime());
		 mArticle_listview.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
	}

}

