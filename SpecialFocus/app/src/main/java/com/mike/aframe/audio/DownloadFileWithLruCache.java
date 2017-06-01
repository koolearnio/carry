/**
 * 工程名: MikeLibs
 * 文件名: DownloadFileWithLruCache.java
 * 包名: com.mike.aframe.audio
 * 日期: 2015-5-11下午1:02:51
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.mike.aframe.audio;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.mike.aframe.core.DiskCache;
import com.mike.aframe.utils.CipherUtils;
import com.mike.aframe.utils.FileUtils;

/**
 * 类名: DownloadFileWithLruCache <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-5-11 下午1:02:51 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class DownloadFileWithLruCache implements I_FileLoader{
	private DiskAudioCache diskCache;
	private MKFileConfig config;
	
	public DownloadFileWithLruCache(MKFileConfig config){
		this.config = config;
		diskCache = new DiskAudioCache(config.cachePath,
                config.diskCacheSize, config.isDEBUG);
	}
	
	@Override
	public File loadFile(String url) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilePathFromDisk(String path) {
		 byte[] data = null;
	        HttpURLConnection con = null;
	        try {
	            URL url = new URL(path);
	            Log.i("path",path);
	            con = (HttpURLConnection) url.openConnection();
	            con.setConnectTimeout(config.timeOut);
	            con.setReadTimeout(config.timeOut * 2);
	            con.setRequestMethod("GET");
	            con.setDoInput(true);
	            con.connect();
	            data=FileUtils.input2byte(con.getInputStream());
	            Log.i("getFileFromdisk",path
	            		+"\ndownload success, from be net");
	            return putBmpToDC(path, data); // 建立diskLru缓存
	        } catch (Exception e) {
	            doFailureCallBack(path, e);
	        } finally {
	            if (con != null) {
	                con.disconnect();
	            }
	        }
	        return null;
	}
	
	/**
     * 如果设置了回调，调用加载失败回调
     * 
     * @param imagePath
     *            加载失败的图片路径
     * @param e
     *            失败原因
     */
    private void doFailureCallBack(String imagePath, Exception e) {
        if (config.callBack != null) {
            config.callBack.audioLoadFailure(imagePath, e.getMessage());
        }
    }

	 /**
     * 加入磁盘缓存
     * 
     * @param path
     *            图片路径
     * @param bmpByteArray
     *            图片二进制数组数据
     */
    private String putBmpToDC(String path, byte[] bmpByteArray) {
        if (config.openDiskCache) {
            return diskCache.put(CipherUtils.md5(path), bmpByteArray);
        } else {
        	return diskCache.put(CipherUtils.md5(path), bmpByteArray);
        }
    }
    
    public String getFileFromDiskCache(String path){
    	return diskCache.getFilePathFromCache(CipherUtils.md5(path));
    }
}

