/**
 * 工程名: MikeLibs
 * 文件名: I_FileLoader.java
 * 包名: com.mike.aframe.audio
 * 日期: 2015-5-11下午1:01:37
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.mike.aframe.audio;

import java.io.File;

/**
 * 类名: I_FileLoader <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-5-11 下午1:01:37 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public interface I_FileLoader {
	public File loadFile(String url);
	public String getFilePathFromDisk(String key);
	public String getFileFromDiskCache(String url);
}

