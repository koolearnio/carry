package com.sepcialfocus.android.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author app 中的线程池
 *
 */
public class ExcutorServiceUtils {
	
	private static ExcutorServiceUtils instance;
	
	private ExecutorService executorService;
	
	private ExcutorServiceUtils(){
		executorService = Executors.newFixedThreadPool(10);
	}
	
	public static ExcutorServiceUtils getInstance(){
		if(instance == null){
			synchronized (ExcutorServiceUtils.class) {
				if(instance == null){
					instance = new ExcutorServiceUtils();
				}
			}
		}
		return instance;
	}
	
	public ExecutorService getThreadPool(){
		return executorService;
	}

}
