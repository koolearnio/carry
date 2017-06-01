/**
 * 工程名: MainActivity
 * 文件名: NavBean.java
 * 包名: com.sepcialfocus.android.bean
 * 日期: 2015-9-4下午9:58:03
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.bean;

import java.io.Serializable;

import com.mike.aframe.database.annotate.Id;

/**
 * 类名: NavBean <br/>
 * 功能: 顶部菜单. <br/>
 * 日期: 2015-9-4 下午9:58:03 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class NavBean implements Serializable{
	@Id
	String md5 = "";
	String menu = "";
	String menuUrl = "";
	int orderId = 0;
	
	/** 1 ：特别关注 
	 *  2：青年文摘
	 *  3：读者
	 *  4：故事会
	 * */
	int category = 1;
	
	String show = "1";
	
	public NavBean(){
	}
	
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
}

