/**
 * 工程名: YybAndroid
 * 文件名: UpdateManager.java
 * 包名: com.yyb.android.globle
 * 日期: 2014-12-11下午9:38:33
 * Copyright (c) 2014, 北京巨翔科技有限公司 All Rights Reserved.
 * 官网：http://www.wjuxiang.com/
 *
*/

package com.sepcialfocus.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mike.aframe.MKLog;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.UpdateBean;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.services.UpgradeService;
import com.sepcialfocus.android.volley.FastJSONRequest;
import com.sepcialfocus.android.volley.FastResponse.Listener;
import com.sepcialfocus.android.volley.VolleyManager;
import com.sepcialfocus.android.widgets.CustomDialog;

/**
 * 类名: UpdateManager <br/>
 * 功能: 应用程序更新工具包. <br/>
 * 日期: 2014-12-11 下午9:38:33 <br/>
 *
 * @author   leixun
 * @email leixun33@163.com
 * @version  	 
 */
public class UpdateManager {
	private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
	
    private static final int DIALOG_TYPE_LATEST = 0;
    private static final int DIALOG_TYPE_FAIL   = 1;
    
	private static UpdateManager updateManager;
	
	private Context mContext;
	//通知对话框
	private Dialog noticeDialog;
	//下载对话框
	private Dialog downloadDialog;
	//'已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
    //进度条
    private ProgressBar mProgress;
    //显示下载数值
    private TextView mProgressText;
    //查询动画
    private ProgressDialog mProDialog;
    //进度值
    private int progress;
    //下载线程
    private Thread downLoadThread;
    //终止标记
    private boolean interceptFlag;
	//提示语
	private String updateMsg = "";
	//返回的安装包url
	private String apkUrl = "";
	//下载包保存路径
    private String savePath = "";
	//apk保存完整路径
	private String apkFilePath = "";
	//临时下载文件路径
	private String tmpFilePath = "";
	//下载文件大小
	private String apkFileSize;
	//已下载文件大小
	private String tmpFileSize;
	
	private String curVersionName = "";
	private int curVersionCode;
	private UpdateBean mUpdate;
    
	CustomDialog dialog = null;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, 
						mContext.getString(R.string.version_download_sd_str)
						, 3000).show();
				break;
			}
    	};
    };
    
	public static UpdateManager getUpdateManager() {
		if(updateManager == null){
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}
	
	/**
	 * 检查App更新
	 * @param context
	 * @param isShowMsg 是否显示提示消息
	 */
	public void checkAppUpdate(Context context, final boolean isShowMsg){
		RequestQueue mQueue = Volley.newRequestQueue(context);
		mQueue.start();
		this.mContext = context;
		getCurrentVersion();
		if(isShowMsg){
			if(mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, 
						mContext.getString(R.string.version_checking_str), true, true);
			else if(mProDialog.isShowing() || (latestOrFailDialog!=null && latestOrFailDialog.isShowing()))
				return;
		}
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				//进度条对话框不显示 - 检测结果也不显示
				if(mProDialog != null && !mProDialog.isShowing()){
					return;
				}
				//关闭并释放释放进度条对话框
				if(isShowMsg && mProDialog != null){
					mProDialog.dismiss();
					mProDialog = null;
				}
				//显示检测结果
				if(msg.what == 1){
					mUpdate = (UpdateBean)msg.obj;
					if(mUpdate != null){
						if(curVersionCode < mUpdate.getVersionCode()){
							apkUrl = mUpdate.getDownloadUrl();
							updateMsg = mUpdate.getUpdateLog();
							showNoticeDialog();
						}else if(isShowMsg){
							showLatestOrFailDialog(DIALOG_TYPE_LATEST);
						}
					}
				}else if(isShowMsg){
					showLatestOrFailDialog(DIALOG_TYPE_FAIL);
				}
			}
		};
		
		VolleyManager.getInstance().beginSubmitRequest(
				mQueue, 
				new FastJSONRequest(URLs.URL_GET_CHECK_VERSION, "", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject obj,
							String executeMethod, String flag,
							boolean dialogFlag) {
						if ("0".equals(flag)){	// 取出results
							if (obj!=null){
								if("1".equals(obj.getString("status"))){	// 分析服务端返回数据
									String resulss = obj.getString("results");
									Message msg = new Message();
									msg.what = 1;
									msg.obj = JSONObject.parseObject(resulss, UpdateBean.class);
									handler.sendMessage(msg);
								}else{
									MKLog.e(getClass().getName(), "can't get data"); 
								}
							} else {
								MKLog.e(getClass().getName(), "can't get data"); 
							}
						} else {
							MKLog.e(getClass().getName(), "The Response flag is not right");
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MKLog.d("", error.toString());
					}
				}));
		
	}	
	
	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			//关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(getResString(R.string.version_tips_str));
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage(getResString(R.string.version_neednot_str));
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage(getResString(R.string.version_cannot_str));
		}
		builder.setPositiveButton("确定", null);
		latestOrFailDialog = builder.create();
		latestOrFailDialog.show();
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion(){
        try { 
        	PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        	curVersionName = info.versionName;
        	curVersionCode = info.versionCode;
        } catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
	}
	
	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog(){
		dialog = new CustomDialog(mContext,R.style.custom_dialog);
		View view = View.inflate(mContext, R.layout.custom_dialog, null);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setView(view);
		((TextView)view.findViewById(R.id.pop_title)).setText("温馨提示");
		((TextView)view.findViewById(R.id.tv_pop_win_text)).setText(getResString(R.string.version_dialog_title_str));


		TextView cancel = (TextView)view.findViewById(R.id.tv_cancel);
		cancel.setText("取消");
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog != null && dialog.isShowing()){
					dialog.cancel();
				}
			}
		});

		TextView commit = (TextView)view.findViewById(R.id.tv_commit);
		commit.setText("更新");
		commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				Intent intent = new Intent(mContext,UpgradeService.class);
				Bundle bundle = new Bundle();
				bundle.putString("downloadurl", mUpdate.getDownloadUrl());
				intent.putExtras(bundle);
				mContext.startService(intent);
			}
		});
		dialog.show();
		
//		builder.setMessage("待测版本"+mUpdate.getVersionCode()+"\n\n"+mUpdate.getUpdateLog());
	}
	
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				String apkName = "YybAndroid_"+mUpdate.getVersionCode()+".apk";
				String tmpApk = "YybAndroid_"+mUpdate.getVersionCode()+".tmp";
				//判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();		
				if(storageState.equals(Environment.MEDIA_MOUNTED)){
					savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YybAndroid/Update/";
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}
				
				//没有挂载SD卡，无法下载文件
				if(apkFilePath == null || apkFilePath == ""){
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				
				File ApkFile = new File(apkFilePath);
				
				//是否已下载更新文件
				if(ApkFile.exists()){
					downloadDialog.dismiss();
					installApk();
					return;
				}
				
				//输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//显示文件大小格式：2个小数点显示
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//进度条下面显示的总文件大小
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    		//进度条下面显示的当前下载文件大小
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//当前进度值
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//下载完成 - 将临时下载文件转成APK文件
						if(tmpFile.renameTo(ApkFile)){
							//通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	
	/**
	* 下载apk
	* @param url
	*/	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
    * 安装apk
    * @param url
    */
	private void installApk(){
		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
	
	private String getResString(int id){
		if (mContext!=null){
			return mContext.getResources().getString(id);
		}else{
			MKLog.d("UpdateManager", "mContext is null when call getResString()");
			return "";
		}
	}
}

