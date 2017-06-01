/**
 * 工程名: MikeLibs
 * 文件名: MKLog.java
 * 包名: com.mike.aframe
 * 日期: 2015-3-16下午2:40:45
 * Mail: ammike@163.com.
 * QQ: 378640336
 * http://www.cnblogs.com/ammike/
 *
*/

package com.mike.aframe;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/**
 * 类名: MKLog <br/>
 * 功能: 日志类. <br/>
 * 日期: 2015-3-16 下午2:40:45 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class MKLog {
	private static boolean IS_DEBUG = MKConfig.isDebug;
    // activity 的状态标志调试
    private static boolean activityState = MKConfig.isDebug;
    // fragment 的状态标志调试 
    private static boolean fragmentState = MKConfig.isDebug;
    
    /** 当前日志记录级别 */
	private static final String LOG_LEVER = MKConfig.debugLevel; // verbose|debug|info|warn|error
	
    public static final Map<String, Integer> LOG_LEVER_MAP = new HashMap<String, Integer>() {
		{
			put("verbose", Integer.valueOf(Log.VERBOSE));
			put("debug", Integer.valueOf(Log.DEBUG));
			put("info", Integer.valueOf(Log.INFO));
			put("warn", Integer.valueOf(Log.WARN));
			put("error", Integer.valueOf(Log.ERROR));
		}
	};

	public static final boolean LOG_FLAG_VERBOSE = Log.VERBOSE >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public static final boolean LOG_FLAG_DEBUG = Log.DEBUG >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public static final boolean LOG_FLAG_INFO = Log.INFO >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public static final boolean LOG_FLAG_WARN = Log.WARN >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public static final boolean LOG_FLAG_ERROR = Log.ERROR >= LOG_LEVER_MAP
			.get(LOG_LEVER);

    public static final void openDebutLog(boolean enable) {
        IS_DEBUG = enable;
        activityState = enable;
    }
    
    public static final void state(String tag, String msg){
    	if (activityState){
    		Log.d(tag, msg);
    	}
    }
    
    public static final void fragState(String tag,String msg){
    	if (fragmentState){
    		Log.d(tag,msg);
    	}
    }


    /**
	 * 根据打印日志标识，判断是否打印日志 </p>
	 * 
	 * 级别：verbose
	 * 
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void v(String tag, String msg, Throwable t) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_VERBOSE) {
				Log.v(tag, msg, t);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：verbose
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, String msg) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_VERBOSE) {
				Log.v(tag, msg);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志 </p>
	 * 
	 * 级别：debug
	 * 
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void d(String tag, String msg, Throwable t) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_DEBUG) {
				Log.d(tag, msg, t);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：debug
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_DEBUG) {
				Log.d(tag, msg);
			}
		}
	}
	
	public static void d(boolean flag ,String tag,String msg){
		if(flag){
			d(tag,msg);
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：info
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_INFO) {
				Log.i(tag, msg);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：info
	 * 
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void i(String tag, String msg, Throwable t) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_INFO) {
				Log.i(tag, msg, t);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：warn
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_WARN) {
				Log.w(tag, msg);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：warn
	 * 
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void w(String tag, String msg, Throwable t) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_WARN) {
				Log.w(tag, msg, t);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：error
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_ERROR) {
				Log.w(tag, msg);
			}
		}
	}

	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：error
	 * 
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void e(String tag, String msg, Throwable t) {
		if (msg != null) {
			if (IS_DEBUG && LOG_FLAG_ERROR) {
				Log.w(tag, msg, t);
			}
		}
	}
	/**
	 * 根据打印日志标识，判断是否打印日志</p>
	 * 
	 * 级别：error
	 * 
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void e(String tag, Throwable t) {
		if (tag != null) {
			if (IS_DEBUG && LOG_FLAG_ERROR) {
				Log.w(tag,t);
			}
		}
	}
}

