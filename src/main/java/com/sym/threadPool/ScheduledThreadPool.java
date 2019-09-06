package com.sym.threadPool;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * 创建调度型线程池，可以定时或者周期性的执行任务
 * 只有当任务的执行时间到来时，ScheduedExecutor 才会真正启动一个线程，
 * 其余时间 ScheduledExecutor 都是在轮询任务的状态
 * 通过ScheduledThreadPool创建，通过execute()方法加入任务
 *
 */
public class ScheduledThreadPool {

	public static void main(String[] args) {
		// 创建一个定时执行线程池,注意返回值为 ScheduledExecutorService
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
		// 创建10个线程，放入到线程池中执行，每个线程相当于一个任务
		for (int i = 0; i < 10; i++) {
			final int idx = i;
			// 任务加入线程池开始算起,5s后才执行,注意调用的是schedule()方法
			pool.schedule(()->{
				String name = Thread.currentThread().getName();
				System.out.println(name+" -> "+(idx+1));
			},5,TimeUnit.SECONDS);
		}
		//关闭线程池，不在接收新的任务，处于关闭状态，等所有执行的线程都执行完以后，终止线程池
		pool.shutdown();

	}

}
