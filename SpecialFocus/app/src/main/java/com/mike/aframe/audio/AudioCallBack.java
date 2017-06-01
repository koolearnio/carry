/**
 * 工程名: MikeLibs
 * 文件名: AudioCallBack.java
 * 包名: com.mike.aframe.audio
 * 日期: 2015-5-11下午2:33:05
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.mike.aframe.audio;

import android.view.View;

/**
 * 类名: AudioCallBack <br/>
 * 功能: 音频加载时回调. <br/>
 * 日期: 2015-5-11 下午2:33:05 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public interface AudioCallBack {
	 /** 音频载入时将回调 */
    public void audioLoading(final View view);

    /** 音频载入完成将回调 */
    public void audioLoadSuccess(final String filePath);

    /** 音频载入失败将回调 */
    public void audioLoadFailure(final String url, String msg);
}

