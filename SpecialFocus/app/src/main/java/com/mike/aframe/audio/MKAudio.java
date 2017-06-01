/**
 * 工程名: MikeLibs
 * 文件名: MKAudio.java
 * 包名: com.mike.aframe.audio
 * 日期: 2015-5-11下午12:46:26
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.mike.aframe.audio;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;
import android.view.View;

import com.mike.aframe.http.utils.KJAsyncTask;

/**
 * 类名: MKAudio <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-5-11 下午12:46:26 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MKAudio {

	private static MKAudio instance = null;
	public static MKFileConfig config;
	private I_FileLoader downloader = null;
	
	private Set<AudioWorkerTask> taskCollection = null;
	
	/**
	 * 音频加载 时触发的回调
	 */
	private AudioCallBack audioLoadCallback;
	
	public static MKAudio getInstance(){
		if (instance == null){
			synchronized (MKAudio.class) {
				if(instance == null){
					instance = new MKAudio();
				}
			}
		}
		return instance;
	}
	
	private MKAudio(){
		config = new MKFileConfig();
		downloader = new DownloadFileWithLruCache(config);
		taskCollection = new HashSet<AudioWorkerTask>();
	}
	
	/*
	 * 开始下载
	 */
	public void startDownload(View view,String url){
		startDownload(view,url,config.openProgress);
	}
	
	
	public void startDownload(View view,String url,boolean progressFlag){
		if(progressFlag){
			
		} else {
			doDownload(view, url);
		}
	}
	
	private class AudioWorkerTask extends KJAsyncTask<String>{
		private View relativeLayout;
		private String url;
		
		public AudioWorkerTask(View rl , String url){
			this.relativeLayout = rl;
			this.url = url;
			relativeLayout.setTag(url);
		}
		
		public View getView(){
			return relativeLayout;
		}
		
		public String getUrl(){
			return url;
		}
		
		 // 取消当前正在进行的任务
        public boolean cancelTask() {
            Log.v("MKAudio","task->" + this.url + "has been canceled");
            return this.cancel(true);
        }
        
		@Override
		protected String doInBackground() {
			return getFileFromNet(url);
		}
		
		@Override
        protected void onPostExecute(String filePath){
			super.onPostExecute(filePath);
			if(filePath!=null && config.openMemoryCache){
				if(config.callBack!=null){
					config.callBack.audioLoadSuccess(filePath);
				}
				taskCollection.remove(this);
			}
		}
		
	}
	
	public String getFileFromNet(String url){
		return downloader.getFilePathFromDisk(url);
	}
	
	public void doDownload(View view,String url){
		String file = getFileFromCache(url);
		if(file !=null ){
			config.callBack.audioLoadSuccess(file);
		} else {
			doGetFileFromNet(view,url);
		}
	}
	
	public String getFileFromCache(String url){
		
		return downloader.getFileFromDiskCache(url);
	}
	
	private void doGetFileFromNet(View view,String url){
		 // 开启task的时候先检查传进来的这个view是否已经有一个task是为它执行
		for(AudioWorkerTask task:taskCollection){
			if(task.getView().equals(view)){	
				 // 是同一个url的话就不用开新的task，不一样就取消掉之前开新的
                if (task.getUrl().equals(url)) {
                    return;
                } else {
                    task.cancelTask();
                    taskCollection.remove(task);
                    break;
                }
			}else{	// 只允许有一个task 所以全部删除
				task.cancelTask();
                taskCollection.remove(task);
			}
		}
		AudioWorkerTask task = new AudioWorkerTask(view,url);
		taskCollection.add(task);
		task.execute();
	}
	
	public void setAudioCallBack(AudioCallBack callBack){
		config.callBack = callBack;
	}
}

