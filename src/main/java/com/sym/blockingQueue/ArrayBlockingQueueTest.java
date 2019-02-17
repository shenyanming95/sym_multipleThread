package com.sym.blockingQueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArrayBlockingQueueTest {
	
	private Random random = new Random();
	
	private int len = 10;
	
	

	public static void main(String[] args) {
		ArrayBlockingQueueTest test = new ArrayBlockingQueueTest();
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 4; i++) {
			final int temp = i+1;
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					Thread.currentThread().setName("生产者"+temp+"号");
					test.produce();
				}
			});
		}
		for (int i = 0; i < 4; i++) {
			final int temp = i+1;
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					Thread.currentThread().setName("消费者"+temp+"号");
					test.consume();
				}
			});
		}
		pool.shutdown();
  
	}
	//构造数组阻塞队列ArrayBlockingQueue，并指明数组的初始大小(不可变)
	private BlockingQueue<Integer> bq = new ArrayBlockingQueue<Integer>(len);
	public void produce(){//生产者。往数组加数据的
		try {
			int data = random.nextInt(1000);
			bq.put(data);//要实现阻塞，需要用put(),add()方法只会报异常
			System.out.println(Thread.currentThread().getName()+" put '"+data+"',队列长度为："+bq.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void consume(){//消费者。往数组取数据的
		try {
			int data = bq.take();//要实现阻塞，需要用take()
			System.out.println(Thread.currentThread().getName()+" take '"+data+"',队列长度为："+bq.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
