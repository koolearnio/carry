/**
 * 工程名: MainActivity
 * 文件名: PagesInfo.java
 * 包名: com.sepcialfocus.android.bean
 * 日期: 2015-9-12上午10:18:25
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.bean;
/**
 * 类名: PagesInfo <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-12 上午10:18:25 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class PagesInfo {
	String nextPageUrl = "";
	Boolean hasNextPage = false;
	
	
	public PagesInfo(){
	}
	public String getNextPageUrl() {
		return nextPageUrl;
	}
	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}
	public Boolean getHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
}

