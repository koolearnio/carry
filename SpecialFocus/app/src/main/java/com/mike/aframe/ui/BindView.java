/**
 * 工程名: MikeLibs
 * 文件名: BindView.java
 * 包名: com.mike.aframe.ui
 * 日期: 2015-3-16下午2:50:59
 * Mail: ammike@163.com.
 * QQ: 378640336
 * http://www.cnblogs.com/ammike/
 *
*/

package com.mike.aframe.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类名: BindView <br/>
 * 功能: 注解式绑定控件. <br/>
 * 日期: 2015-3-16 下午2:50:59 <br/>
 *
 * @author   mike
 * @version  	 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
	public int id();

    public boolean click() default false;
}

