
package com.sepcialfocus.android.ui.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mike.aframe.MKLog;
import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.DensityUtils;
import com.mike.aframe.utils.MD5Utils;
import com.mike.aframe.utils.PreferenceHelper;
import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.BaseApplication;
import com.sepcialfocus.android.BaseFragment;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.bean.HistroyItemBean;
import com.sepcialfocus.android.bean.PagesInfo;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemListParse;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemPagesParse;
import com.sepcialfocus.android.ui.adapter.ArticleListAdapter;
import com.sepcialfocus.android.utils.ExcutorServiceUtils;
import com.sepcialfocus.android.utils.SettingsManager;
import com.sepcialfocus.android.widgets.swiptlistview.SwipeListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * 类名: ArticleFragment <br/>
 * 功能描述: TODO 添加功能描述. <br/>
 * 日期: 2015年9月2日 上午10:27:26 <br/>
 *
 * @author leixun
 * @version 
 */
public class ArticleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
	private SwipeRefreshLayout mSwipeLayout;
	private ArrayList<ArticleItemBean> mArticleList;
	private SwipeListView mArticle_listview;
	private ArticleListAdapter mArticleAdapter;
	private View mView;
	private Context mContext;
	private String urls = "";
	
	// 加载更多
	boolean isPullRrefreshFlag;
	// 下拉刷新
	boolean isRefresh = false;
	boolean isPullFlag = false;
	boolean isPrepared = false;
	String nextUrl;
	private KJDB kjDb = null;
	
	
	private boolean isNeedWrite = false;
	
	public ArticleFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MKLog.d("state", "onCreate:"+this.urls);
		this.mContext = getActivity();
		kjDb = KJDB.create(mContext);
		Bundle args = getArguments();
        if (null !=  args) {
            if (args.containsKey("key")) {
                this.urls = args.getString("key");
            }
        }
	}
	
	@Override
	protected void initView() {
		mSwipeLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipe_container);
		mArticle_listview = (SwipeListView)mView.findViewById(R.id.article_listview);
		initSwapLayout();
		mLoadingLayout = (RelativeLayout)mView.findViewById(R.id.layout_loading_bar);
		mNoNetLayout = (RelativeLayout)mView.findViewById(R.id.layout_refresh_onclick);
		mNoNetLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isRefresh = true;
				myHandler.sendEmptyMessage(0x112);
			}
		});
		mArticle_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						ArticleItemBean bean = mArticleList.get(position);
						ArticleItemBean tempBean = new ArticleItemBean();
						tempBean.setCategory(bean.getCategory());
						tempBean.setImgUrl(bean.getImgUrl());
						tempBean.setTitle(bean.getTitle());
						tempBean.setTags(bean.getTags());
						tempBean.setTagUrl(bean.getTagUrl());
						tempBean.setDate(bean.getDate());
						tempBean.setHasFavor(bean.isHasFavor());
						tempBean.setHasReadFlag(true);
						tempBean.setMd5(bean.getMd5());
						tempBean.setUrl(bean.getUrl());
						tempBean.setSummary(bean.getSummary());
						mArticleList.set(position, tempBean);
						kjDb.update(tempBean, " md5 = \'"+bean.getMd5()+"\'");
						if(kjDb.findById(bean.getMd5(), HistroyItemBean.class)==null){
							HistroyItemBean historyBean = new HistroyItemBean();
							historyBean.setCategory(bean.getCategory());
							historyBean.setImgUrl(bean.getImgUrl());
							historyBean.setTitle(bean.getTitle());
							historyBean.setTags(bean.getTags());
							historyBean.setTagUrl(bean.getTagUrl());
							historyBean.setDate(bean.getDate());
							historyBean.setHasFavor(bean.isHasFavor());
							historyBean.setHasReadFlag(true);
							historyBean.setMd5(bean.getMd5());
							historyBean.setUrl(bean.getUrl());
							historyBean.setSummary(bean.getSummary());
							kjDb.save(historyBean);
						}else{ 
//							Toast.makeText(mContext, "已经读过，不用继续保存",Toast.LENGTH_SHORT).show();
						}
						mArticleAdapter.notifyDataSetChanged();
					}
				});
				Intent intent = new Intent(mContext,ArticleDetailActivity.class);
				intent.putExtra("key", mArticleList.get(position));
				startActivity(intent);
			}
		});
		mArticle_listview.setOnBottomListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPullFlag && isPullRrefreshFlag && !isRefresh){
					isPullFlag = true;
					Loadhtml(urls+nextUrl);
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		MKLog.d("state", "onCreateView:"+this.urls);
		mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_articles, null);
		initView();
		if(this.urls.contains("/1/")){
			MKLog.d("","");
		}
		isPrepared = true;
		isRefresh = false;
    	isPullFlag = false;
		return mView;
	}
	

	
	
	private void initData(){
		if(isPrepared){
			if(null==mArticleList || mArticleList.size()==0){
				MKLog.d("urls","开始读服务端-》"+this.urls);
				isRefresh = true;
				Loadhtml(urls);
			}
		}
	}

	private void readNative(){
		setLoadingVisible(true);
    	mSwipeLayout.setVisibility(View.GONE);
		ExcutorServiceUtils.getInstance().getThreadPool().submit(
				new Runnable(){

					@Override
					public void run() {
						readNativeData();
						if(isPrepared){
							myHandler.sendEmptyMessage(0x110);
						}
					}
					
		});
	}
	
	public void onResume(){
		super.onResume();
		MKLog.d("state", "onResume:"+this.urls);
		if(mArticleAdapter!=null && mArticleAdapter.getCount()>0){
			mArticle_listview.setAdapter(mArticleAdapter);
			isPrepared = true;
		}else{
			lazyLoad();
		}
	}
	
	private void Loadhtml(final String urls)
    {
		ExcutorServiceUtils.getInstance().getThreadPool().submit(
				new Runnable(){
					@Override
					public void run() {
						try {
							 Document doc;
			                if(!"".equals(urls)){
				                	doc = Jsoup.connect(urls).timeout(5000).get();
				                	if(!isPrepared){
				                		return;
				                	}
				                	Document content = Jsoup.parse(doc.toString());
				                	if(!isPrepared){
				                		return;
				                	}
				                	if(isRefresh){
				                		if(mArticleList.size() == 0){
				                			mArticleList.addAll(0, ArticleItemListParse.getArticleItemList(kjDb, content,isRefresh));
				                			PagesInfo info = ArticleItemPagesParse.getPagesInfo(urls, content);
				                			nextUrl = info.getNextPageUrl();
					                		isPullRrefreshFlag = info.getHasNextPage();
					                		if(mArticleList!= null && mArticleList.size()>0){
					                			isNeedWrite = true;
					                		}
				                		}else{
				                			ArrayList<ArticleItemBean> list = ArticleItemListParse.getArticleItemList(kjDb, content);
				                			if(list!= null && list.size()>0){
				                				isNeedWrite = true;
				                			}
				                			MKLog.d("urls", "刷新-》"+urls+"-----list.size:"+list.size());
				                			mArticleList.addAll(0, list);
				                		}
				                	}else{
				                		PagesInfo info = ArticleItemPagesParse.getPagesInfo(urls, content);
				                		isPullRrefreshFlag = info.getHasNextPage();
				                		nextUrl = info.getNextPageUrl();
				                		ArrayList<ArticleItemBean> list = ArticleItemListParse.getArticleItemList(kjDb, content);
				                		if(list!= null && list.size()>0){
				                			isNeedWrite = true;
				                		}
				                		MKLog.d("urls", "非刷新-》"+urls+"-----list.size:"+list.size());
				                		mArticleList.addAll(0, list);
				                	}
				                }
			                } catch (Exception e) {
			                	mArticle_listview.onBottomComplete();
			                	mSwipeLayout.setRefreshing(false);
			                	isRefresh = false;
			                	isPullFlag = false;
			                	e.printStackTrace();
			                }
						if(isPrepared){
							myHandler.sendEmptyMessage(0x111);
						}
						}
					
				});
    }

	public void onPause(){
		super.onPause();
		MKLog.d("state", "onPause:"+this.urls);
		
		isPrepared = false;
		
		if(isNeedWrite && mArticleList!=null && mArticleList.size()>0){
			ExcutorServiceUtils.getInstance().getThreadPool().submit((new Runnable() {
				
				@Override
				public void run() {
					
					BaseApplication.globalContext.saveObject(mArticleList, MD5Utils.md5(urls));
					PreferenceHelper.write(mContext, 
							AppConstant.URL_NEXT_PAGE_FILE, MD5Utils.md5(urls),nextUrl);
					isNeedWrite = false;
					myHandler.sendEmptyMessage(0x113);
				}
			}));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MKLog.d("state", "onDestory:"+this.urls);
	}

	private void readNativeData(){
		try{
        	mArticleList = (ArrayList<ArticleItemBean>)
        			BaseApplication.globalContext.readObject(MD5Utils.md5(urls));
        	nextUrl = PreferenceHelper.readString(mContext, 
        			AppConstant.URL_NEXT_PAGE_FILE, MD5Utils.md5(urls));
        	if(nextUrl!=null && !"".equals(nextUrl)){
        		isPullRrefreshFlag = true;
        	}
        	if(mArticleList==null){
        		mArticleList = new ArrayList<ArticleItemBean>();
            }
        }catch(Exception e){
        	e.printStackTrace();
        	mArticleList = new ArrayList<ArticleItemBean>();
        }
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
		Loadhtml(urls);
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

	protected void lazyLoad() {
		if(mArticleAdapter!=null && mArticleAdapter.getCount()>0){
			mArticle_listview.setAdapter(mArticleAdapter);
			return;
		}
	   if(!isPrepared) {  
            return;  
        }  
		readNative();
	}
	
	private Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x110:	// 读取本地完毕
				if(mArticleAdapter == null){
					mArticleAdapter = new ArticleListAdapter(mContext, mArticleList);
					if(mArticle_listview!=null){
						mArticle_listview.setAdapter(mArticleAdapter);
					}
				}
				if(isPrepared){
					myHandler.sendEmptyMessage(0x112);
				}
				break;
			case 0x111:	// 服务端下载完毕
				setLoadingVisible(false);
	            mSwipeLayout.setVisibility(View.VISIBLE);
	            if(mArticleAdapter!=null){
	            	mArticleAdapter.notifyDataSetChanged();
	            }
	            mArticle_listview.onBottomComplete();
	        	isRefresh = false;
	        	isPullFlag = false;
	            mSwipeLayout.setRefreshing(false);
	            
	            if(mArticleList==null 
	            		|| mArticleList.size()==0){
	            	setLoadingVisible(false);
	            	mSwipeLayout.setVisibility(View.GONE);
	            	setNoNetVisible(true);
	            }else{
	            	setNoNetVisible(false);
	            }
				break;
			case 0x112:	// 度服务端之前
				if(mArticleList!=null 
		        		&& mArticleList.size()==0){
		        	setLoadingVisible(true);
		        	mSwipeLayout.setVisibility(View.GONE);
		        }
		        setNoNetVisible(false);
		        if(mArticleList == null || mArticleList.size() == 0){
					initData();
				}else{
					MKLog.d("urls","读本地成功-》"+urls);
					setLoadingVisible(false);
					mSwipeLayout.setVisibility(View.VISIBLE);
					mArticleAdapter.notifyDataSetChanged();
				}
				break;
			case 0x113:
				Toast.makeText(getActivity(), "写入成功", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};

}

