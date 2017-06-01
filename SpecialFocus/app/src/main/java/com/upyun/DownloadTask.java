/**
 * 工程名: TOEFLCorrect
 * 文件名: DownloadTask.java
 * 包名: com.upyun
 * 日期: 2015-4-24下午4:32:15
 * Mail: ammike@163.com.
 * QQ: 378640336
 *
*/

package com.upyun;

import android.os.AsyncTask;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.upyun.block.api.http.HttpManager;
import com.upyun.block.api.listener.CompleteListener;

/**
 * 类名: DownloadTask <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-4-24 下午4:32:15 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class DownloadTask extends AsyncTask<Void, Void, String>{
	String saveFile = "";
	FileAsyncHttpResponseHandler responseHandler = null;
	String path = "";
	public DownloadTask(String path,FileAsyncHttpResponseHandler
			responseHandler){
		this.responseHandler = responseHandler;
		this.path = path;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		HttpManager manager = new HttpManager();
		manager.doGet(path, null, responseHandler);
		return null;
	}
}

