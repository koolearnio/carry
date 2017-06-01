/**
 * 工程名: MikeLibs
 * 文件名: MD5Utils.java
 * 包名: com.mike.aframe.utils
 * 日期: 2015-4-24上午9:21:54
 * Mail: ammike@163.com.
 * QQ: 378640336
 *
*/

package com.mike.aframe.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类名: MD5Utils <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-4-24 上午9:21:54 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class MD5Utils {
	public static String md5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}

}

