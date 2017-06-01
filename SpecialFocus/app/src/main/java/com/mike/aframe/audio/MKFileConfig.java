/**
 * 工程名: MikeLibs
 * 文件名: MKFileConfig.java
 * 包名: com.mike.aframe.audio
 * 日期: 2015-5-11下午1:06:12
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.mike.aframe.audio;

import android.graphics.Bitmap;

import com.mike.aframe.MKConfig;

/**
 * 类名: MKFileConfig <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-5-11 下午1:06:12 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MKFileConfig {
	public static final int DEFAULT = Integer.MAX_VALUE;
    public boolean isDEBUG = MKConfig.isDebug;

    /** 网络连接等待时间 */
    public int timeOut = 5000;

    /** 载入时的图片 */
    public Bitmap loadingBitmap;
    /** 音频载入状态将会回调相应的方法 */
    public AudioCallBack callBack;
    /** 是否开启载入图片时显示环形progressBar效果 */
    public boolean openProgress = false;

    /** 是否开启内存缓存功能 */
    public boolean openMemoryCache = true;
    /** 内存缓存大小 */
    public int memoryCacheSize;

    /** 本地图片缓存路径 */
    public String cachePath = MKConfig.cachePath;
    /** 是否开启本地图片缓存功能 */
    public boolean openDiskCache = true;
    /** 本地缓存大小 */
    public int diskCacheSize = 10 * 1024 * 1024;
}

