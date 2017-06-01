package com.sepcialfocus.android.receiver;

import com.sepcialfocus.android.services.NotificationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author 检测文章更新
 *
 */
public class CheckUpdateReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent jintent = new Intent(context,NotificationService.class);
		context.startService(jintent);
	}

}
