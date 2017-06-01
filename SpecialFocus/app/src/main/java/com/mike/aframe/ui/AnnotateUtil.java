/**
 * 工程名: MikeLibs
 * 文件名: AnnotateUtil.java
 * 包名: com.mike.aframe.ui
 * 日期: 2015-3-16下午2:52:48
 * Mail: ammike@163.com.
 * QQ: 378640336
 * http://www.cnblogs.com/ammike/
 *
*/

package com.mike.aframe.ui;

import java.lang.reflect.Field;

import com.mike.aframe.MKException;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 类名: AnnotateUtil <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-3-16 下午2:52:48 <br/>
 *
 * @author   mike
 * @version  	 
 */
public class AnnotateUtil {
	/**
     * @param currentClass
     *            当前类，一般为Activity或Fragment
     * @param sourceView
     *            待绑定控件的直接或间接父控件
     */
    public static void initBindView(Object currentClass,
            View sourceView) {
        // 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
        Field[] fields = currentClass.getClass().getDeclaredFields();
        bindProcess(currentClass,sourceView,fields);
        Field[] superFields = currentClass.getClass().getSuperclass().getDeclaredFields();
        bindProcess(currentClass,sourceView,superFields);
    }
    
    private static void bindProcess(Object currentClass,
            View sourceView,Field[] fields){
    	if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                // 返回BindView类型的注解内容
                BindView bindView = field
                        .getAnnotation(BindView.class);
                if (bindView != null) {
                    int viewId = bindView.id();
                    boolean clickLis = bindView.click();
                    try {
                        field.setAccessible(true);
                        if (clickLis) {
                            sourceView
                                    .findViewById(viewId)
                                    .setOnClickListener(
                                            (OnClickListener) currentClass);
                        }
                        // 将currentClass的field赋值为sourceView.findViewById(viewId)
                        field.set(currentClass,
                                sourceView.findViewById(viewId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 必须在setContentView之后调用
     * 
     * @param aty
     */
    public static void initBindView(Activity aty) {
        initBindView(aty, aty.getWindow().getDecorView());
    }

    /**
     * 必须在setContentView之后调用
     * 
     * @param view
     *            侵入式的view，例如使用inflater载入的view
     */
    public static void initBindView(View view) {
        Context cxt = view.getContext();
        if (cxt instanceof Activity) {
            initBindView((Activity) cxt);
        } else {
            throw new MKException("the view don't have root view");
        }
    }

    /**
     * 必须在setContentView之后调用
     * 
     * @param frag
     */
    public static void initBindView(Fragment frag) {
        initBindView(frag, frag.getActivity().getWindow()
                .getDecorView());
    }
}

