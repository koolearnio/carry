/**
 * 工程名: TOEFLCorrect
 * 文件名: ReturnBean.java
 * 包名: com.upyun.bean
 * 日期: 2015-4-24下午4:01:45
 * Mail: ammike@163.com.
 * QQ: 378640336
 *
*/

package com.upyun.bean;

import java.io.Serializable;

/**
 * 类名: ReturnBean <br/>
 * 功能: 又盘云返回参数. <br/>
 * 日期: 2015-4-24 下午4:01:45 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class ReturnBean implements Serializable{
	String path = "";
	long last_modified = 0l;
	String mimetype = "";
	int code = 0;
	String file_size = "";
	String signature = "";
	String bucket_name = "";
	
	public ReturnBean(){
	}
	
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

