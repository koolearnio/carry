/**
 * 工程名: MikeLibs
 * 文件名: MKConfig.java
 * 包名: com.mike.aframe
 * 日期: 2015-3-16下午2:43:55
 * Mail: ammike@163.com.
 * QQ: 378640336
 * http://www.cnblogs.com/ammike/
 *
*/

package com.mike.aframe;

import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.util.Log;

/**
 * 类名: MKConfig <br/>
 * 功能: 配置文件. <br/>
 * 日期: 2015-3-16 下午2:43:55 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class MKConfig {
	/**
     * 是否调试
     */
    public static final boolean isDebug = false;
    /**
     * 与isDebug结合使用
	 *	verbose|debug|info|warn|error
     */
    public static final String debugLevel = "verbose"; 
    
    /** preference键值对 */
    public static final String SETTING_FILE = "xmcorrect_preference";
    public static final String ONLY_WIFI = "only_wifi";
    public static final String DBNAME = "xmcorrect_db_";
    
    /** 图片缓存路径、http数据缓存路径 */
    public static final String cachePath = "/XM_Library/";
    /** 图片本地缓存文件名前缀 */
    public static final String IMAGE_PREFIX = "XM_";
    /** http数据通信中 */
    public static final String HttpParamsKey = "audioUrl";
    
    public static final String sdcardRootPath = Environment.getExternalStorageDirectory().getPath();
    public static final String dataRootPath = Environment.getDataDirectory()+"/data/com.xiaoma.correct";
    public static final String AUDIO_PATH = "audio";
}

