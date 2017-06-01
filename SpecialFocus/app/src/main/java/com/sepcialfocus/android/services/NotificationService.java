package com.sepcialfocus.android.services;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mike.aframe.database.KJDB;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemListParse;
import com.sepcialfocus.android.ui.WelcomeActivity;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class NotificationService extends Service{
	private MyHandler myHandler;

	private KJDB kjDb;
	
	@Override  
	public void onCreate() {
		super.onCreate();
	}  

	@Override  
	public void onDestroy() {
		super.onDestroy();
	} 
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		String downloadUrl = ToolsPreferences.getPreferences(ToolsPreferences.DOWNLOAD_URL);
		//将下载任务添加到任务栏中
		myHandler=new MyHandler(Looper.myLooper(),this);
		
		kjDb = KJDB.create(this);
		getNewestArticle();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void getNewestArticle(){
		new Thread(){
			public void run(){
				try{
					Document doc = null;
					doc = Jsoup.connect(URLs.HOST).timeout(5000).get();
					Document content = Jsoup.parse(doc.toString());
					ArrayList<ArticleItemBean> list = 
							ArticleItemListParse.getArticleItemLists(kjDb, content);
					if(list!=null && list.size()>0){
						Message msg = new Message();
						msg.obj = list.get(0);
						msg.what = 0x01;
						myHandler.sendMessage(msg);
					}else{
						myHandler.sendEmptyMessage(0x02);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}

	class MyHandler extends Handler{
		private Context context;
		public MyHandler(Looper looper,Context c){
			super(looper);
			this.context=c;
		}
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if(msg!=null){
				switch(msg.what){
				case 0x01:
					ArticleItemBean bean = (ArticleItemBean)msg.obj;
					Intent intent = new Intent();
					intent.setAction("com.sepcialfocus.android.receive");
					Bundle bundle = new Bundle();
					bundle.putSerializable("key", bean);
					intent.putExtras(bundle);
					sendBroadcast(intent);
					break;
				case 0x02:
					//停止掉当前的服务
					stopSelf();
					break;
				}
			}
		}
	}
	
}
