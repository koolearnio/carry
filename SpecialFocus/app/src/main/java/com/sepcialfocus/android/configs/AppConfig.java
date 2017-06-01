/**
 * 工程名: MainActivity
 * 文件名: AppConfig.java
 * 包名: com.sepcialfocus.android.config
 * 日期: 2015-9-2下午10:30:05
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.configs;

/**
 * 类名: AppConfig <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-2 下午10:30:05 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class AppConfig {
	
	public static boolean imgFlag = true;
	
	public static boolean windowFlag = true;

	/** sdcard根目录 */
	public static final String sdcardRootPath = android.os.Environment
			.getExternalStorageDirectory().getPath();
	/** 机身根目录 */
	public static final String dataRootPath = android.os.Environment
			.getDataDirectory() + "/data/" + "com.specialfocus.android";
	
	/** 正式版客户端文件根目录 */
	public static final String officialRoot = "/specialfocus";
	/**json文件保存地址*/
	public static final String jsonPath = "/json/";
	/** 图片文件地址 */
	public static final String imgPath = "/image/";
	public static final String htmlPath = "/html/";
	
	public static String getDownloadImgPath(){
		if(ExistSDCard()){
			return AppConfig.sdcardRootPath + AppConfig.officialRoot + AppConfig.imgPath;
		}else{
			return AppConfig.dataRootPath + AppConfig.officialRoot + AppConfig.imgPath;
		}
	}
	public static String getUploadHtmlPath(){
		if(ExistSDCard()){
			return AppConfig.sdcardRootPath + AppConfig.officialRoot + AppConfig.htmlPath;
		}else{
			return AppConfig.dataRootPath + AppConfig.officialRoot + AppConfig.htmlPath;
		}
	}
	
	public static boolean ExistSDCard() {  
		if (android.os.Environment.getExternalStorageState().equals(  
				android.os.Environment.MEDIA_MOUNTED)) {  
			return true;  
		} else {
			return false;    
		} 
	}  
}

