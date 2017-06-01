/**
 * 工程名: MainActivity
 * 文件名: HistroyItemBean.java
 * 包名: com.sepcialfocus.android.bean
 * 日期: 2015-9-27上午9:17:39
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.mike.aframe.database.annotate.Id;

/**
 * 类名: HistroyItemBean <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-27 上午9:17:39 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class HistroyItemBean implements Serializable{
	@Id
	private String md5 = "";
	protected String title = "";
	protected String date = "";
	protected String imgUrl = "";
	protected String url = "";
	protected String tagUrl = "";
	protected ArrayList<String> tags = null;
	protected String summary = "";
	
	/** 该条目类型  
	 * 1：正常文章
	 * */
	protected int category = 1;
	
	protected boolean hasReadFlag = false;
	protected boolean hasFavor = false;
	
	public HistroyItemBean(){
	}
	
	public boolean isHasFavor() {
		return hasFavor;
	}
	public void setHasFavor(boolean hasFavor) {
		this.hasFavor = hasFavor;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTagUrl() {
		return tagUrl;
	}
	public void setTagUrl(String tagUrl) {
		this.tagUrl = tagUrl;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public boolean isHasReadFlag() {
		return hasReadFlag;
	}
	public void setHasReadFlag(boolean hasReadFlag) {
		this.hasReadFlag = hasReadFlag;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
}

