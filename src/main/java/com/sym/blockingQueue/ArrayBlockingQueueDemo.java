package com.sym.blockingQueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 沈燕明 on 2018/12/31.
 */
public class ArrayBlockingQueueDemo {

    // 创建指定容量的阻塞队列,因为它是数组实现的,所以必须指定容量
    private ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(5);

    /**
     * 生产者线程往阻塞队列中添加数据
     *
     * @param obj 数据
     */
    public void put(Object obj) {
        String name = Thread.currentThread().getName();
        try {
            blockingQueue.put(obj);
            System.out.println(name + "添加：" + obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费者线程往阻塞队列中取走数据
     */
    public void take() {
        String name = Thread.currentThread().getName();
        try {
            Object obj = blockingQueue.take();
            System.out.println(name + "取走：" + obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Random random = new Random();// 随机数
        ExecutorService threadPool = Executors.newFixedThreadPool(10); // 线程池
        ArrayBlockingQueueDemo demo = new ArrayBlockingQueueDemo();
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
        threadPool.shutdown();
    }
}
