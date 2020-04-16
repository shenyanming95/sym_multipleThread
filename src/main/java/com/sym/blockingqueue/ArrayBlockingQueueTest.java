package com.sym.blockingqueue;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ym.shen
 * @date 2018/12/31
 */
public class ArrayBlockingQueueTest {

	private Random random = new Random();

	@Test
	public void test0() throws IOException {
		// 线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		SymArrayBlockingQueue demo = new SymArrayBlockingQueue();
		// 开启2个生产者线程
		for (int i = 0; i < 2; i++) {
			final int j = i + 1;
			threadPool.execute(() -> {
				Thread.currentThread().setName("生产者" + j + "号");
				// 每个线程添加5个
				for (int z = 0; z < 5; z++) {
					demo.put(random.nextInt(5000));
				}
			});
		}
		// 开启3个消费者线程
		for (int i = 5; i < 8; i++) {
			final int j = i + 1;
			threadPool.execute(() -> {
				Thread.currentThread().setName("消费者" + j + "号");
				for (; ; ) {
					demo.take();
				}
			});
		}
		// junit 多线程比较麻烦
		System.in.read();
	}
}
