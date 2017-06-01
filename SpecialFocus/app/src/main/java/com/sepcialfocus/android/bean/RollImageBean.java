/**
 * 工程名: MainActivity
 * 文件名: RollImageBean.java
 * 包名: com.sepcialfocus.android.bean
 * 日期: 2015-9-5上午10:17:30
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.bean;

import java.io.Serializable;

/**
 * 类名: RollImageBean <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-5 上午10:17:30 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class RollImageBean implements Serializable{
	private String id = "";
	private String title="";
	private String imgUrl = "";
	private String imgLinkUrl = "";
	
	
	public RollImageBean(){
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgLinkUrl() {
		return imgLinkUrl;
	}
	public void setImgLinkUrl(String imgLinkUrl) {
		this.imgLinkUrl = imgLinkUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}

