/**
 * ������: SpecialFocus
 * �ļ���: MainFragment.java
 * ����: com.sepcialfocus.android.ui.article
 * ����: 2015-9-1����9:42:39
 * Copyright (c) 2015, ����С����ӽ����Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.article;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.sepcialfocus.android.bean.RollImageBean;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemListParse;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemPagesParse;
import com.sepcialfocus.android.ui.adapter.ArticleListAdapter;
import com.sepcialfocus.android.utils.SettingsManager;
import com.sepcialfocus.android.widgets.swiptlistview.SwipeListView;
import com.sepcialfocus.android.widgets.viewimage.Animations.DescriptionAnimation;
import com.sepcialfocus.android.widgets.viewimage.Animations.SliderLayout;
import com.sepcialfocus.android.widgets.viewimage.SliderTypes.BaseSliderView;
import com.sepcialfocus.android.widgets.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.sepcialfocus.android.widgets.viewimage.SliderTypes.TextSliderView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

/**
 * 类名: MainFragment <br/>
 * 功能描述: TODO 添加功能描述. <br/>
 * 日期: 2015年9月2日 上午10:27:39 <br/>
 *
 * @author leixun
 * @version 
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
			,OnSliderClickListener{
	
	protected SliderLayout mSlider;
	private ArrayList<RollImageBean> images;
	int gallerypisition = 0;
	Timer autoGallery = new Timer();
	LinearLayout pointLinear;
	private int position = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;

	private ArrayList<ArticleItemBean> mArticleList;
	private SwipeRefreshLayout mSwipeLayout;
	private SwipeListView mArticle_listview;
	private ArticleListAdapter mArticleAdapter;
	private View mView;
	private View mHeadView;
	private Activity mActivity;
	private String urls = "";
	
	boolean isPullRrefreshFlag;
	String nextUrl;
	private KJDB kjDb = null;
	
	ArrayList<RollImageBean> rollList;
	private boolean isRefresh = false;
	private boolean isPullFlag = false;
	public MainFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mActivity = getActivity();
		kjDb = KJDB.create(mActivity);
		Bundle args = getArguments();
        if (null !=  args) {
            if (args.containsKey("key")) {
                this.urls = args.getString("key");
            }
        }
        try{
        	mArticleList = (ArrayList<ArticleItemBean>)
        			BaseApplication.globalContext.readObject(MD5Utils.md5(urls));
        	nextUrl = PreferenceHelper.readString(getActivity(), 
        			AppConstant.URL_NEXT_PAGE_FILE, MD5Utils.md5(urls));
        	if(nextUrl!=null && !"".equals(nextUrl)){
        		isPullRrefreshFlag = true;
        	}
        	images = (ArrayList<RollImageBean>)BaseApplication.globalContext.readObject("rollImgs");
        	if(mArticleList==null){
        		mArticleList = new ArrayList<ArticleItemBean>();
            }
        }catch(Exception e){
        	e.printStackTrace();
        	mArticleList = new ArrayList<ArticleItemBean>();
        }
        
	}
	
	@Override
	protected void initView() {
		mSlider = (SliderLayout)mHeadView.findViewById(R.id.slider);
		mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
		mSlider.setCustomAnimation(new DescriptionAnimation());
		mLoadingLayout = (RelativeLayout)mView.findViewById(R.id.layout_loading_bar);
		mArticle_listview = (SwipeListView)mView.findViewById(R.id.article_listview);
		mSwipeLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipe_container);
		mArticle_listview = (SwipeListView)mView.findViewById(R.id.article_listview);
		initSwapLayout();
		if(mArticle_listview.getHeaderViewsCount()<1){
			mArticle_listview.addHeaderView(mHeadView);
		}
		mArticle_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if(mArticleList!=null && mArticleList.size() >= position){
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							ArticleItemBean bean = mArticleList.get(position-1);
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
							mArticleList.set(position-1, tempBean);
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
								//Toast.makeText(mContext, "已经读过，不用继续保存",Toast.LENGTH_SHORT).show();
							}
							mArticleAdapter.notifyDataSetChanged();
						}
					});
					Intent intent = new Intent(mActivity,ArticleDetailActivity.class);
					intent.putExtra("key", mArticleList.get(position-1));
					startActivity(intent);
				}
			}
		});
		mArticle_listview.setOnBottomListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPullFlag && isPullRrefreshFlag && !isRefresh){
					isPullFlag = true;
					new Loadhtml(urls+nextUrl).execute("","","");
				} else {
					isPullFlag = false;
					mArticle_listview.onBottomComplete();
				}
			}
		});
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		mView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_main, null);
		mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
		initView();
		mArticleAdapter = new ArticleListAdapter(mActivity, mArticleList);
		mArticle_listview.setAdapter(mArticleAdapter);
		initData();
		return mView;
	}
	
	private void initData(){
		if(null==mArticleList || mArticleList.size()==0){
			isRefresh = true;
			new Loadhtml(URLs.HOST).execute("","","");
		}
		if(images!=null){
			getRollImages(images);
		}
	}
	


	@Override
	public void onPause() {
		
		if(mArticleList!=null && urls!=null && BaseApplication.globalContext!=null){
			BaseApplication.globalContext.saveObject(mArticleList, MD5Utils.md5(urls));
		}
		BaseApplication.globalContext.saveObject(images, "rollImgs");
		PreferenceHelper.write(getActivity(), 
				AppConstant.URL_NEXT_PAGE_FILE, MD5Utils.md5(urls),nextUrl);
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		timeFlag = false;
	}

	public void getRollImages(ArrayList<RollImageBean> json){
		images = json;
		if (images!=null && images.size() > 0){
			int size = images.size();
			for(int i = 0 ; i < size ; i++){
				TextSliderView textSliderView = new TextSliderView(getActivity());
				textSliderView.setOnSliderClickListener(this);
				textSliderView.description(images.get(i).getTitle()).image(URLs.HOST+images.get(i).getImgUrl());
				textSliderView.getBundle().putSerializable("key", images.get(i));
				mSlider.addSlider(textSliderView);
			}
		} else {
			MKLog.d("MainFragment", "get RollImageBean is null !!!");
		}
	}


	class Loadhtml extends AsyncTask<String, String, String>
    {
        Document doc;
        String urls = "";
        public Loadhtml(String urls){
        	this.urls = urls;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                if("".equals(urls)){
                	return null;
                }
            	doc = Jsoup.connect(urls).timeout(5000).get();
                 Document content = Jsoup.parse(doc.toString());
                 if(!isPullFlag){
            		 parseRoolImg(content);
            	 }
                 if(isRefresh){
                	 if(mArticleList.size() == 0){
                		 if(URLs.HOST.equals(urls)){
                    		 isPullRrefreshFlag = true;
                    		 nextUrl = "index_1.html";
                    	 }else{
                    		 PagesInfo info = ArticleItemPagesParse.getPagesInfo(urls, content);
                    		 isPullRrefreshFlag = info.getHasNextPage();
                    		 nextUrl = info.getNextPageUrl();
                    	 }
                		 mArticleList.addAll(0, ArticleItemListParse.getArticleItemList(kjDb, content,isRefresh));
                	 }else{
                		 if(URLs.HOST.equals(urls) && "".equals(nextUrl)){
                    		 isPullRrefreshFlag = true;
                    		 nextUrl = "index_1.html";
                    	 }
                		 mArticleList.addAll(0, ArticleItemListParse.getArticleItemList(kjDb, content));
                	 }
                 }else{
                	 if(URLs.HOST.equals(urls)){
                		 isPullRrefreshFlag = true;
                		 nextUrl = "index_1.html";
                	 }else{
                		 PagesInfo info = ArticleItemPagesParse.getPagesInfo(urls, content);
                		 isPullRrefreshFlag = info.getHasNextPage();
                		 nextUrl = info.getNextPageUrl();
                	 }
                	 if(mArticleList.size() == 0){
                		 mArticleList.addAll(ArticleItemListParse.getArticleItemList(kjDb, content,true));
                	 }else{
                		 mArticleList.addAll(ArticleItemListParse.getArticleItemList(kjDb, content));
                	 }
                 }
            } catch (IOException e) {
            	mHandler.sendEmptyMessage(0x02);
                isRefresh = false;
            	isPullFlag = false;
                e.printStackTrace();
            } catch(Exception e){
            	mArticle_listview.onBottomComplete();
                mSwipeLayout.setRefreshing(false);
                isRefresh = false;
            	isPullFlag = false;
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
            mArticleAdapter.notifyDataSetChanged();
            mArticle_listview.onBottomComplete();
            if(!isPullFlag){
            	if(mSlider!=null){
            		mSlider.removeAllSliders();
            	}
            	getRollImages(rollList);
            }
        	isRefresh = false;
        	isPullFlag = false;
            mSwipeLayout.setRefreshing(false);
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
        }
        
    }
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void initSwapLayout(){
		mSwipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		mSwipeLayout.setOnRefreshListener(this);
		
		 SettingsManager settings = SettingsManager.getInstance();
		 mArticle_listview.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
		 mArticle_listview.setSwipeActionLeft(settings.getSwipeActionLeft());
		 mArticle_listview.setSwipeActionRight(settings.getSwipeActionRight());
		 mArticle_listview.setOffsetLeft(DensityUtils.dip2px(getActivity(),
	                settings.getSwipeOffsetLeft()));
		 mArticle_listview.setOffsetRight(DensityUtils.dip2px(getActivity(),
	                settings.getSwipeOffsetRight()));
		 mArticle_listview.setAnimationTime(settings.getSwipeAnimationTime());
		 mArticle_listview.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
		new Loadhtml(URLs.HOST).execute("","","");
	}
	
	private void parseRoolImg(Document content){
		 Element rollImg = content.getElementById("pic");
         Elements rollImgChild= rollImg.children();
          rollList = new ArrayList<RollImageBean>();
         for(Element bean : rollImgChild){
        	 Elements ele = bean.getElementsByTag("a");
        	 for(Element bean_ele:ele){
        		 if(bean_ele.hasAttr("href") && bean_ele.hasAttr("title")){
        			 RollImageBean item = new RollImageBean();
        			 item.setImgLinkUrl(bean_ele.attr("href"));
        			 item.setTitle(bean_ele.attr("title"));
        			 Elements imgs = bean_ele.children();
        			 if(imgs!=null && imgs.size()>0){
        				 Element img = imgs.get(0);
        				 if(img!=null && img.hasAttr("src")){
        					 item.setImgUrl(img.attr("src"));
        					 rollList.add(item);
        				 }
        			 }
        		 }
        	 }
         }
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		Intent intent = new Intent(mActivity,ArticleDetailActivity.class);
		RollImageBean bean = (RollImageBean)slider.getBundle().getSerializable("key");
		ArticleItemBean item = new ArticleItemBean();
		item.setUrl(bean.getImgLinkUrl());
		item.setCategory(1);
		item.setMd5(MD5Utils.md5(bean.getImgLinkUrl()));
		item.setTitle(bean.getTitle());
		intent.putExtra("key", item);
		startActivity(intent);
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x02:
				mArticle_listview.onBottomComplete();
                mSwipeLayout.setRefreshing(false);
				break;
			}
		}
		
	};
}

