/** 
 * @ClassName: TaskProcesser.java
 * @author: jiajie_liang
 * @date: 2015年7月2日 上午11:09:23
 * @copyright:  ©2011-2014 youpo.net
 * @Description:
 *
 */
package com.dh.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jiajie_liang
 *
 */
public class TaskProcesser {

	private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 执行一次延迟任务
	 * @param runnable
	 * @param delay 延迟（秒）
	 */
	public static void executeTaskDelay(Runnable runnable,int delay){
		
		scheduler.schedule(runnable, delay, TimeUnit.SECONDS);
	}
	
	/**
	 * 执行一次延迟任务，并以指定的周期循环执行
	 * @param runnable
	 * @param initialDelay 延迟（秒）
	 * @param period 周期（秒）
	 */
	public static void executeTaskAtFixedRate(Runnable runnable,int initialDelay,int period ){
		
		scheduler.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS);
	}
	
	
	/**
	 * 执行一次延迟任务，并在指定的延迟执行下一次
	 * @param runnable
	 * @param initialDelay 首次延迟（秒）
	 * @param delay 第二次延迟（秒）
	 */
	public static void executeTaskWithFixedDelay(Runnable runnable, int initialDelay, int delay ){
		
		scheduler.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.SECONDS);
	}
	
	
	public static void main(String[] args) {
		TaskProcesser.executeTaskWithFixedDelay(new Runnable(){

			@Override
			public void run() {
				System.out.println("TaskProcesser.main(...).new Runnable() {...}.run()");
				
			}
			
		}, 2, 10);
	}
	
}
