package com.sym.demo.threadpool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 创建固定数量线程的线程池，在同一时刻，只有规定数量的线程处于运行状态
 * 多出来的线程只能在队列中等待
 * 通过newFixedThreadPool创建，通过execute()方法加入任务
 *
 * @author shenyanming
 */
public class FixedThreadPool {

	// 用于计数
	private static int count = 1;

	public static void main(String[] args) {
		// 创建一个固定数量为5的线程池
		ExecutorService pool = Executors.newFixedThreadPool(5);
		// 创建10个线程，放入到线程池中执行，每个线程相当于一个任务
		for (int i = 0; i < 10; i++) {
			final int count = i;
			pool.execute(()->{
				String name = Thread.currentThread().getName();
				System.out.println(name+" -> "+(count+1));
			});
		}
		//关闭线程池，不再接收新的任务，处于关闭状态，等所有执行的线程都执行完以后，终止线程池
		pool.shutdown();
	}

}
