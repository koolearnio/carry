/**
 * 工程名: MainActivity
 * 文件名: ArticleItemListParse.java
 * 包名: com.sepcialfocus.android.parse.specialfocus
 * 日期: 2015-9-12上午10:07:16
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.parse.specialfocus;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.mike.aframe.MKLog;
import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.MD5Utils;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.ui.article.ArticleFragment;

/**
 * 类名: ArticleItemListParse <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-12 上午10:07:16 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ArticleItemListParse {
	
	public static ArrayList<ArticleItemBean> getArticleItemList(KJDB kjDb,Document content) throws Exception{
		return getArticleItemList(kjDb,content,false);
	}
	
	/**
	 * 
	 * getArticleItemList:(解析特别关注-》文章条目类). <br/>
	 *
	 * @author leixun
	 * 2015-9-12上午10:14:26
	 * @param kjDb
	 * @param content
	 * @return
	 * @throws Exception
	 * @since 1.0
	 */
	public static ArrayList<ArticleItemBean> getArticleItemList(KJDB kjDb,Document content,boolean isRefresh) throws Exception{
		 Element article = content.getElementById("article");
         MKLog.d("element", article.toString());
         Elements elements = article.children();
         ArrayList<ArticleItemBean> tempList = new ArrayList<ArticleItemBean>();
         for(Element linkss : elements)
         {	 
        	 if(!linkss.hasAttr("class") || !"post".equals(linkss.attr("class"))){
        		 continue;
        	 }
        	 String title = "";
         	 String imgUrl = "";
         	 String summary = "";
         	 String link = "";
        	 Elements titleImg = linkss.getElementsByTag("img");
        	 for(Element bean : titleImg){
        		 if(bean.hasAttr("alt")){	// 标题
        			 title = bean.attr("alt");
        		 }
        		 if(bean.hasAttr("src")){	// 图像地址
        			 imgUrl = bean.attr("src");
        		 }
        	 }
        	 
        	 Elements contentUrl = linkss.getElementsByClass("summary");
        	 for(Element bean:contentUrl){
        		 if(bean.hasAttr("class")){	// 内容摘要
        			 summary = bean.text();
        		 }
        		 Elements contentBean = bean.getElementsByTag("a");
        		 for(Element bean2:contentBean){
        			 if(bean2.hasAttr("href")){	// 跳转链接
        				 link = bean2.attr("href");
        			 }
        		 }
        	 }
        	 String time = "";
        	 ArrayList<String> tags = new ArrayList<String>();
        	 String tagUrl = "";
        	 Elements timeTag = linkss.getElementsByClass("postmeta");
        	 for(Element links: timeTag){
        		 Elements spans = links.getElementsByTag("span");
        		 for(Element bean:spans){
        			 if(bean.hasAttr("class")){
        				 if("left_author_span".equals(bean.attr("class"))){
        					 time = bean.text();
        					 continue;
        				 }
        				 if("left_tag_span".equals(bean.attr("class"))){
        					 Elements childen = bean.children();
        					 for(Element child:childen){
        						 if(child.hasAttr("href")){
        							 tagUrl = child.attr("href");
        							 tags.add(child.text());
        						 }
        					 }
        				 }
        			 }
        		 }
        		 
        		 
        	 }
        	 ArticleItemBean bean = new ArticleItemBean();
        	 bean.setTitle(title);
        	 bean.setDate(time);
        	 bean.setImgUrl(imgUrl);
        	 bean.setSummary(summary);
        	 bean.setUrl(link);
        	 bean.setTags(tags);
        	 bean.setMd5(MD5Utils.md5(link));
        	 ArticleItemBean selectBean = kjDb.findById(bean.getMd5(), ArticleItemBean.class);
        	 if(selectBean==null){
        		 tempList.add(bean);
        		 kjDb.save(bean);
        	 }else if(isRefresh && selectBean!=null){
        		 tempList.add(bean);
        	 }
         }
         return tempList;
	}
	
	/**
	 * 
	 * getArticleItemList:(解析特别关注-》文章条目类). <br/>
	 *
	 * @author leixun
	 * 2015-9-12上午10:14:26
	 * @param kjDb
	 * @param content
	 * @return
	 * @throws Exception
	 * @since 1.0
	 */
	public static ArrayList<ArticleItemBean> getArticleItemLists(KJDB kjDb,Document content) throws Exception{
		Element article = content.getElementById("article");
		MKLog.d("element", article.toString());
		Elements elements = article.children();
		ArrayList<ArticleItemBean> tempList = new ArrayList<ArticleItemBean>();
		for(Element linkss : elements)
		{	 
			if(!linkss.hasAttr("class") || !"post".equals(linkss.attr("class"))){
				continue;
			}
			String title = "";
			String imgUrl = "";
			String summary = "";
			String link = "";
			Elements titleImg = linkss.getElementsByTag("img");
			for(Element bean : titleImg){
				if(bean.hasAttr("alt")){	// 标题
					title = bean.attr("alt");
				}
				if(bean.hasAttr("src")){	// 图像地址
					imgUrl = bean.attr("src");
				}
			}
			
			Elements contentUrl = linkss.getElementsByClass("summary");
			for(Element bean:contentUrl){
				if(bean.hasAttr("class")){	// 内容摘要
					summary = bean.text();
				}
				Elements contentBean = bean.getElementsByTag("a");
				for(Element bean2:contentBean){
					if(bean2.hasAttr("href")){	// 跳转链接
						link = bean2.attr("href");
					}
				}
			}
			String time = "";
			ArrayList<String> tags = new ArrayList<String>();
			String tagUrl = "";
			Elements timeTag = linkss.getElementsByClass("postmeta");
			for(Element links: timeTag){
				Elements spans = links.getElementsByTag("span");
				for(Element bean:spans){
					if(bean.hasAttr("class")){
						if("left_author_span".equals(bean.attr("class"))){
							time = bean.text();
							continue;
						}
						if("left_tag_span".equals(bean.attr("class"))){
							Elements childen = bean.children();
							for(Element child:childen){
								if(child.hasAttr("href")){
									tagUrl = child.attr("href");
									tags.add(child.text());
								}
							}
						}
					}
				}
				
				
			}
			ArticleItemBean bean = new ArticleItemBean();
			bean.setTitle(title);
			bean.setDate(time);
			bean.setImgUrl(imgUrl);
			bean.setSummary(summary);
			bean.setUrl(link);
			bean.setTags(tags);
			bean.setMd5(MD5Utils.md5(link));
			ArticleItemBean selectBean = kjDb.findById(bean.getMd5(), ArticleItemBean.class);
			if(selectBean==null){
				tempList.add(bean);
			}
		}
		return tempList;
	}
}

