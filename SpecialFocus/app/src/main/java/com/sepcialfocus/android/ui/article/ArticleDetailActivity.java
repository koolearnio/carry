/**
 * 工程名: MainActivity
 * 文件名: ArticleDetailActivity.java
 * 包名: com.sepcialfocus.android.ui.article
 * 日期: 2015-9-4下午8:10:44
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.article;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mike.aframe.MKLog;
import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.DensityUtils;
import com.mike.aframe.utils.PreferenceHelper;
import com.mike.aframe.utils.Regexp;
import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.bean.HistroyItemBean;
import com.sepcialfocus.android.configs.AppConfig;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.share.CustomShareBoard;
import com.sepcialfocus.android.widgets.MyWebView;
import com.upyun.UploadTask;
import com.upyun.bean.ReturnBean;
import com.upyun.block.api.listener.CompleteListener;
import com.upyun.block.api.listener.ProgressListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类名: ArticleDetailActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-4 下午8:10:44 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ArticleDetailActivity extends BaseFragmentActivity
	implements View.OnClickListener{
	
	
	private String urls = "";
	private ArticleItemBean mArticleBean;
	private TextView mArticleTitleTv;
	private TextView mArticlePostmetaTv;
	private TextView mArticleContentTv;
	private String mArticleContentStr = "";
	private String mArticlePostmetaStr = "";
	ImageView mBackImg;
	private LinearLayout mContentLL;
	private MyWebView mWebView;
	
	private ImageView mLoadImg;
	private ImageView mShareImg;
	private ImageView mFavorImg;
	private String md5="";
	private Boolean favorFlag = false;
	private KJDB kjDb = null;
	
	HistroyItemBean mHistoryBean;
	
	CustomShareBoard mCustomShareBoard = null;
	
	private View view;
	ReturnBean mReturnBean = null;
	File mFile = null;
	AnimationDrawable animationDrawable = null;
	 Document doc;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_article_detail);
		initView();
		kjDb = KJDB.create(this);
		if(getIntent().getSerializableExtra("key") instanceof ArticleItemBean){
			mArticleBean = (ArticleItemBean)getIntent().getSerializableExtra("key");
			mArticleTitleTv.setText(mArticleBean.getTitle());
			urls = URLs.HOST+mArticleBean.getUrl();
			md5 = mArticleBean.getMd5();
		}else{
			HistroyItemBean bean = (HistroyItemBean)getIntent().getSerializableExtra("key");
			mArticleBean = new ArticleItemBean();
			mArticleBean.setCategory(bean.getCategory());
			mArticleBean.setSummary(bean.getSummary());
			mArticleTitleTv.setText(bean.getTitle());
			urls = URLs.HOST+bean.getUrl();
			md5 = bean.getMd5();
		}
		setLoadingVisible(true);
		mContentLL.setVisibility(View.GONE);
		new Loadhtml(urls).execute("","","");
	}
	
	protected void initView(){
		super.initView();
		mBackImg = (ImageView)findViewById(R.id.bottom_back);
		mBackImg.setOnClickListener(this);
		mShareImg = (ImageView)findViewById(R.id.bottom_share);
		mLoadImg = (ImageView)findViewById(R.id.loading_bar);
		mShareImg.setOnClickListener(this);
		mFavorImg = (ImageView)findViewById(R.id.bottom_favor);
		mFavorImg.setOnClickListener(this);
		mFavorImg.setEnabled(false);
		mNoNetLayout = (RelativeLayout)findViewById(R.id.layout_refresh_onclick);
		mNoNetLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setNoNetVisible(false);
				setLoadingVisible(true);
				mContentLL.setVisibility(View.GONE);
				new Loadhtml(urls).execute("","","");
			}
		});
		mContentLL = (LinearLayout)findViewById(R.id.content_ll);
		mArticleTitleTv = (TextView)findViewById(R.id.article_title);
		mArticlePostmetaTv = (TextView)findViewById(R.id.article_postmeta);
		mArticleContentTv = (TextView)findViewById(R.id.article_content);
		mWebView = (MyWebView)findViewById(R.id.article_web);
		mWebView.setBackgroundColor(0);
		mWebView.getSettings().setTextSize(setTextSize(PreferenceHelper.readInt(ArticleDetailActivity.this, 
    			AppConstant.TEXTSIZE, AppConstant.TEXTSIZE,3)));
	}

	class Loadhtml extends AsyncTask<String, String, String>
    {
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
                CharSequence charSequence = null;
            	doc = Jsoup.connect(urls).timeout(5000).get();
                 Document content = Jsoup.parse(doc.toString());
                 mArticleContentStr = parseArticleContent(false,content);
                 mArticlePostmetaStr = parsePostMeta(content);
                 mHistoryBean = kjDb.findById(md5, HistroyItemBean.class);
                 
                 if(mHistoryBean!=null){
                	 favorFlag = mHistoryBean.isHasFavor();
                 }else{
                	 MKLog.d(ArticleDetailActivity.class.getSimpleName(), "严重逻辑错误");
                 }
                 return mArticleContentStr;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            Log.d("doc", doc.toString().trim());
            if(mFavorImg!=null){
            	if(favorFlag){
            		mFavorImg.setImageResource(R.drawable.bottom_collectioned_icon);
            	}else{
            		mFavorImg.setImageResource(R.drawable.bottom_collection_icon);
            	}
            	mFavorImg.setEnabled(true);
            }
            mArticleContentTv.setText(result);
//            mArticlePostmetaTv.setText(Html.fromHtml(mArticlePostmetaStr));
            if(!"".equals(mArticleContentStr)){
            	mWebView.getSettings().setJavaScriptEnabled(false);  
            	mWebView.getSettings().setLoadWithOverviewMode(true);
            	mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            	mWebView.setBackgroundColor(0);
            	mWebView.loadData(mArticleContentStr, "text/html; charset=UTF-8", "utf-8");
            	new Handler().postDelayed(new Runnable() {
            		@Override
            		public void run() {
            			mContentLL.setVisibility(View.VISIBLE);
            			setLoadingVisible(false);
            		}
            	}, 200);
            }else{
            	setLoadingVisible(false);
        		mContentLL.setVisibility(View.GONE);
        		setNoNetVisible(true);
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        
    }
	
	private String parsePostMeta(Document content){
		Element article = content.getElementById("text");
		Elements articles = content.getElementsByClass("postmeta");
		articles.select("script").remove();
        if(article!=null){
	        return articles.toString();
        }else{
        	Elements contents = content.getElementsByClass("info");
        	contents.select("script").remove();
        	return contents.toString();
        }
	}
	
	/*
	 * 解析内容
	 */
	private String parseArticleContent(boolean isShare,Document content){
		//　去掉广告
		if(content.getElementById("hr336")!=null){
			content.getElementById("hr336").remove();
		}
		// 批量处理img标签 链接地址、宽高设置
		Elements pngs = content.select("img[src]");  
        for (Element element : pngs) {  
            String imgUrl = element.attr("src");
            if (imgUrl.trim().startsWith("/")) {  
                imgUrl =URLs.HOST + imgUrl;  
                element.attr("src", imgUrl);
            }  
            if(!isShare){
            	String imgWidth = element.attr("style").trim();
            	int width = Regexp.getStringWidth(imgWidth);
            	int height = Regexp.getStringHeight(imgWidth);
            	if(width > AppConstant.WEBVIEW_WIDTH && height > 0){
            		height = height*AppConstant.WEBVIEW_WIDTH/width;
            		width = AppConstant.WEBVIEW_WIDTH;
            	}
            	if(width>0 && height>0){
            		element.attr("style", "width:"+width+"px; height:"+height+"px;");
            	}
            }
        }  
        
        Element article = content.getElementById("text");
        Elements contents = null;
        if(article!=null){
        	article.append("<div style=\"height:"+DensityUtils.dip2px(this, 15)+"px\"></div>");
        	return article.toString();
        }else{
        	contents = content.getElementsByClass("content");
        	contents.append("<div style=\"height:"+DensityUtils.dip2px(this, 15)+"px\"></div>");
        	return contents.toString();
        }
        
	}

	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		case R.id.bottom_back: 
			finish();
			break;
		case R.id.bottom_favor:
			if(mHistoryBean!=null){
				if(mHistoryBean.isHasFavor()){
					mHistoryBean.setHasFavor(false);
					mFavorImg.setImageResource(R.drawable.bottom_collection_icon);
				}else{
					mFavorImg.setImageResource(R.drawable.bottom_collectioned_icon);
					mHistoryBean.setHasFavor(true);
				}
				new Handler().post(new Runnable(){
					@Override
					public void run() {
						kjDb.update(mHistoryBean, " md5 = \'"+md5+"\'");
					}
				});
			}
			break;
		case R.id.bottom_share:
			view = arg0;
			mLoadImg.setVisibility(View.VISIBLE);
//			animationDrawable = (AnimationDrawable)mLoadImg.getBackground();
//			if(animationDrawable!=null&&animationDrawable.isRunning()){
//				animationDrawable.stop();
//			}
//			startAnimationDrawable();
			mShareImg.setVisibility(View.INVISIBLE);
			String path = makeHtml(mArticleBean);
			if(!"".equals(path)){
				uploadHtml(path);
			}else{
//				Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x01:
//				stopAnimationDrawable();
				mLoadImg.setVisibility(View.GONE);
				mShareImg.setVisibility(View.VISIBLE);
				if(view!=null && mReturnBean!=null && mArticleBean!=null){
//					Toast.makeText(ArticleDetailActivity.this, "200", Toast.LENGTH_SHORT).show();
					openSharewindow(view,mArticleBean.getSummary(),
							mArticleBean.getTitle(),
							AppConstant.UPYUN+mReturnBean.getPath(),AppConstant.SHARE_ICON);
				}else{
//					Toast.makeText(ArticleDetailActivity.this, "400", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
		
	};

	@Override
	protected void onPause() {
		super.onPause();
		TextSize size = this.mWebView.getSettings().getTextSize();
		PreferenceHelper.write(this, 
    			AppConstant.TEXTSIZE, AppConstant.TEXTSIZE,getTextSize(size));
	}
	
	private int getTextSize(TextSize size){
		if(size == TextSize.LARGER){
			return 4;
		}else if(size == TextSize.LARGEST){
			return 5;
		}else if(size == TextSize.SMALLER){
			return 2;
		}else if(size == TextSize.SMALLEST){
			return 1;
		}else{
			return 3;
		}
	}
	
	private TextSize setTextSize(int size){
		switch(size){
		case 1:
			return TextSize.SMALLEST;
		case 2:
			return TextSize.SMALLER;
		case 3:
			return TextSize.NORMAL;
		case 4:
			return TextSize.LARGER;
		case 5:
			return TextSize.LARGEST;
		default:
				return TextSize.NORMAL;
		}
	}
	
	/**
	 * TODO 自定义分享界面.<br/>
	 * 
	 * @author wenpeng 2015-6-15下午6:21:58
	 * @param view
	 * @since 1.0
	 */
	private void openSharewindow(View view,String content,String title,
			String targetUrl,String iconUrl) {
		mCustomShareBoard = new CustomShareBoard(this);
		mCustomShareBoard.setShareContent(content,title,targetUrl,iconUrl);
		mCustomShareBoard.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				setBackground(1.0f);
			}
		});
		setBackground(0.3f);
		mCustomShareBoard.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private void setBackground(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}
	
	private String makeHtml(ArticleItemBean bean){
		FileWriter fw = null;
		try {
			InputStream in = getResources().openRawResource(R.raw.footer);
			Document document = Jsoup.parse(in, "utf-8", URLs.HOST); 
			document.getElementById("title").append("<h1>"+mArticleBean.getTitle());
			document.getElementById("span").append(mArticlePostmetaStr);
			document.getElementById("article").append(mArticleContentStr);
			File file = new File(AppConfig.getUploadHtmlPath());
			if(!file.exists()){
				file.mkdirs();
			}
			File files = new File(AppConfig.getUploadHtmlPath()+bean.getMd5()+".html");
			if(files.exists()){
				return AppConfig.getUploadHtmlPath()+bean.getMd5()+".html";
			}else{
				fw = new FileWriter(AppConfig.getUploadHtmlPath()+bean.getMd5()+".html");
				fw.write(document.toString());
				return AppConfig.getUploadHtmlPath()+bean.getMd5()+".html";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}finally{
			try {
				if(fw!=null){
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void uploadHtml(String path){
		new UploadTask(new File(path), "/test/html/share/"+mArticleBean.getMd5()+".html", new ProgressListener() {
			@Override
			public void transferred(long transferedBytes, long totalBytes) {
			}
		}, new CompleteListener() {
			@Override
			public void result(boolean isComplete, String result, String error) {
				mReturnBean = (ReturnBean)JSON.parseObject(result, ReturnBean.class);
				if(mReturnBean.getCode()==200){
					mHandler.sendEmptyMessage(0x01);
				}else{
//					Toast.makeText(ArticleDetailActivity.this, result+"\nreturnbean:"+mReturnBean.getCode()+"\nerror:"+error, Toast.LENGTH_LONG).show();
				}
			}
		}).execute();
	}
	
	/**
	 * 停止播放动画
	 */
	private void stopAnimationDrawable(){
		if (animationDrawable!=null){
			animationDrawable.setOneShot(true);
		}
	}
	
	/**
	 * 播放动画
	 */
	private void startAnimationDrawable(){
		if (animationDrawable!=null){
			animationDrawable.setOneShot(false);
			animationDrawable.stop();
			animationDrawable.start();
		}
	}
}

