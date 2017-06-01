/**
 * 工程名: MainActivity
 * 文件名: UpdateBean.java
 * 包名: com.sepcialfocus.android.bean
 * 日期: 2015-9-19上午9:51:24
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.bean;
/**
 * 类名: UpdateBean <br/>
 * 功能: 版本更新bean. <br/>
 * 日期: 2015-9-19 上午9:51:24 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class UpdateBean {
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	
	public UpdateBean(){
	}
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		if (!"".equals(versionCode)){
			this.versionCode = Integer.parseInt(versionCode);
		} else{
			this.versionCode = 0;
		}
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	
}

