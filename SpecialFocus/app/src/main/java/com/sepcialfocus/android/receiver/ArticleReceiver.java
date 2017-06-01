package com.sepcialfocus.android.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;

import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.ui.article.ArticleDetailActivity;

public class ArticleReceiver extends BroadcastReceiver{
	private NotificationManager nm;
	private Notification notification;
	private MyHandler myHandler;
	private RemoteViews views;
	private int notificationId=1235;
	private String title = "";
	private String summary = "";
	@Override
	public void onReceive(Context context, Intent intent) {
		nm=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		notification=new Notification();
		notification.icon=R.drawable.icon;
		notification.when=System.currentTimeMillis();
		notification.defaults=Notification.DEFAULT_LIGHTS;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		//设置任务栏中下载进程显示的views
		views=new RemoteViews(context.getPackageName(),R.layout.article_notificate);
		notification.contentView=views;
		Intent jumpIntent = new Intent(context,ArticleDetailActivity.class);
		
		myHandler=new MyHandler(Looper.myLooper(),context);
		ArticleItemBean bean = (ArticleItemBean)intent.getSerializableExtra("key");
		if(bean!=null){
			title = bean.getTitle();
			summary = bean.getSummary();
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("key", bean);
		notification.tickerText=title;
		jumpIntent.putExtras(bundle);
		//初始化下载任务内容views
		PendingIntent contentIntent=PendingIntent.getActivity(context,0,jumpIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(context,"","",contentIntent);
		//将下载任务添加到任务栏中
		nm.notify(notificationId,notification);
		Message message=myHandler.obtainMessage(3,0);
		myHandler.sendMessage(message);
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
				case 3:
					views.setTextViewText(R.id.tvTitle,title);
					views.setTextViewText(R.id.tvContent,summary);
					notification.contentView=views;
					nm.notify(notificationId,notification);
					break;
				case 4:
					nm.cancel(notificationId);
					break;
				}
			}
		}
	}
}
