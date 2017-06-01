/**
 * 工程名: MainActivity
 * 文件名: ArticleItemPagesParse.java
 * 包名: com.sepcialfocus.android.parse.specialfocus
 * 日期: 2015-9-12上午10:15:49
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.parse.specialfocus;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.mike.aframe.MKLog;
import com.sepcialfocus.android.bean.PagesInfo;
import com.sepcialfocus.android.ui.article.ArticleFragment;

/**
 * 类名: ArticleItemPagesParse <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-12 上午10:15:49 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ArticleItemPagesParse {
	
	public static PagesInfo getPagesInfo(String urls,Document content){
		PagesInfo pages = new PagesInfo();
		 Element nextPage = content.getElementById("pages");
         Elements nextelement = nextPage.getElementsByTag("a");
         for(Element element:nextelement){
        	 if("下一页".equals(element.text())){
        		 pages.setHasNextPage(true);
        		 pages.setNextPageUrl(element.attr("href").trim());
        		 break;
        	 }
        	 MKLog.d("element",element.toString());
         }
        if(!pages.getHasNextPage()){
        	pages.setNextPageUrl("");
        }
		return pages;
	}

}

