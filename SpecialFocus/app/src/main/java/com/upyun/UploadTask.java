/**
 * 工程名: TOEFLCorrect
 * 文件名: UploadTask.java
 * 包名: com.upyun
 * 日期: 2015-4-24下午2:16:39
 * Mail: ammike@163.com.
 * QQ: 378640336
 *
*/

package com.upyun;

import android.os.AsyncTask;

import com.sepcialfocus.android.AppConstant;
import com.upyun.block.api.listener.CompleteListener;
import com.upyun.block.api.listener.ProgressListener;
import com.upyun.block.api.main.UploaderManager;
import com.upyun.block.api.utils.UpYunUtils;

import java.io.File;
import java.util.Map;

/**
 * 类名: UploadTask <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-4-24 下午2:16:39 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class UploadTask extends AsyncTask<Void, Void, String> {
	// 空间名
	private static final String bucket = AppConstant.bucket;
	// 表单密钥
	private static final String formApiSecret = AppConstant.formApiSecret;
	File localFile;
	ProgressListener progressListener;
	CompleteListener completeListener;
	private String savePath;
	
	public UploadTask(File localFile,String savePath,ProgressListener progressListener,
			CompleteListener completeListener){
		this.localFile = localFile;
		this.progressListener = progressListener;
		this.completeListener = completeListener;
		this.savePath = savePath;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			UploaderManager uploaderManager = UploaderManager.getInstance(bucket);
			uploaderManager.setConnectTimeout(60);
			uploaderManager.setResponseTimeout(60);
			Map<String, Object> paramsMap = uploaderManager.fetchFileInfoDictionaryWith(localFile, savePath);
			//还可以加上其他的额外处理参数...
//			paramsMap.put("return_url", "http://httpbin.org/get");
			// signature & policy 建议从服务端获取
			String policyForInitial = UpYunUtils.getPolicy(paramsMap);
			String signatureForInitial = UpYunUtils.getSignature(paramsMap, formApiSecret);
			uploaderManager.upload(policyForInitial, signatureForInitial, localFile, progressListener, completeListener);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "result";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
//			Toast.makeText(getApplicationContext(), "成功\n"+result, Toast.LENGTH_LONG).show();
		} else {
//			Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_LONG).show();
		}
	}
}
