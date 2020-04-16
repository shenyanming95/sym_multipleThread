package com.sym.threadpool.executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 创建根据需要new新线程的缓存线程池
 *
 * 通过newCacheThreadPool创建，通过execute()方法加入任务
 *
 */
public class CacheThreadPool {

	public static void main(String[] args) {
		// 创建缓存线程池
		ExecutorService pool = Executors.newCachedThreadPool();
		// 创建10个线程，放入到线程池中执行，每个线程相当于一个任务
		for (int i = 0; i < 10; i++) {
			try {
				// 主线程休眠1s,保证下一个任务执行时上一个任务已经执行完
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			final int idx = i;
			pool.execute(()->{
				String name = Thread.currentThread().getName();
				System.out.println(name+" -> " + (idx+1));
			});
		}
		//关闭线程池，不在接收新的任务，处于关闭状态，等所有执行的线程都执行完以后，终止线程池
		pool.shutdown();

	}

}
