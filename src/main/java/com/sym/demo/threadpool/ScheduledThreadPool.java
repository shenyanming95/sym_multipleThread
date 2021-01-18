package com.sym.demo.threadpool;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * 创建调度型线程池，可以定时或者周期性的执行任务
 * 只有当任务的执行时间到来时，ScheduedExecutor 才会真正启动一个线程，
 * 其余时间 ScheduledExecutor 都是在轮询任务的状态
 * 通过ScheduledThreadPool创建，通过execute()方法加入任务
 *
 * @author shenyanming
 */
public class ScheduledThreadPool {

	public static void main(String[] args) {
		ScheduledThreadPool scheduledThreadPool = new ScheduledThreadPool();
		//scheduledThreadPool.scheduleOnce();
		scheduledThreadPool.schedulePeriodic();
	}

	/**
	 * 创建调度线程池,注意返回值为 ScheduledExecutorService
	 */
	ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

	/**
	 * 从放入线程池的时间开始算起，过了指定时间后，才执行线程，只会执行一次
	 */
	public void scheduleOnce(){
		// 创建10个线程，放入到线程池中执行，每个线程相当于一个任务 周期性的
		for (int i = 0; i < 10; i++) {
			final int idx = i;
			// 任务加入线程池开始算起,5s后才执行,注意调用的是schedule()方法
			pool.schedule(()->{
				String name = Thread.currentThread().getName();
				System.out.println(name+" -> "+(idx+1));
			},5,TimeUnit.SECONDS);
		}
	}

	/**
	 * 周期性地执行
	 */
	public void schedulePeriodic(){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		/*
		 * 使用的API是：scheduleAtFixedRate()，4个参数的含义依次为：
		 * 		1.要执行的任务
		 * 		2.第一次执行的时间，置为0时，表示立即执行
		 * 		3.间隔值，表示每隔一段时间执行，与第4个参数搭配使用
		 * 		4.时间单位，与第3个参数搭配使用
		 *	例子表示：任务立即执行，并且以后，每隔10s执行一次
		 */
		pool.scheduleAtFixedRate(()->{
			System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
		},0,10,TimeUnit.SECONDS);
	}

	/**
	 * 关闭线程池
	 */
	public void close(){
		//关闭线程池，不在接收新的任务，处于关闭状态，等所有执行的线程都执行完以后，终止线程池
		pool.shutdown();
	}

}
