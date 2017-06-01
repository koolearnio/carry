/**
 * 工程名: MainActivity
 * 文件名: ReturnBean.java
 * 包名: com.sepcialfocus.android.bean
 * 日期: 2015-10-2上午10:53:23
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.bean;
/**
 * 类名: ReturnBean <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-10-2 上午10:53:23 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ReturnBean {
	String path = "";
	long last_modified = 0l;
	String mimetype = "";
	int code = 0;
	String file_size = "";
	String signature = "";
	String bucket_name = "";
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(long last_modified) {
		this.last_modified = last_modified;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getBucket_name() {
		return bucket_name;
	}
	public void setBucket_name(String bucket_name) {
		this.bucket_name = bucket_name;
	}
}

