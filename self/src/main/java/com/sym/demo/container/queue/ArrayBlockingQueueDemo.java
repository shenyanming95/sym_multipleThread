package com.sym.demo.container.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link ArrayBlockingQueue}
 *
 * @author shenyanming
 * Created on 2020/8/19 10:36
 */
@Slf4j
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) {
        ArrayBlockingQueueDemo queueDemo = new ArrayBlockingQueueDemo();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Random random = new Random();

        // 开启2个生产者线程
        for (int i = 0; i < 2; i++) {
            final int j = i + 1;
            threadPool.execute(() -> {
                Thread.currentThread().setName("生产者" + j + "号");
                // 每个线程添加5个
                for (int z = 0; z < 5; z++) {
                    queueDemo.put(random.nextInt(5000));
                }
            });
        }

        // 开启3个消费者线程
        for (int i = 5; i < 8; i++) {
            final int j = i + 1;
            threadPool.execute(() -> {
                Thread.currentThread().setName("消费者" + j + "号");
                while (!Thread.currentThread().isInterrupted()) {
                    queueDemo.take();
                }
            });
        }
    }

    /**
     * 创建指定容量的阻塞队列, 因为它是数组实现的, 所以必须指定容量
     */
    private ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(16);

    /**
     * 往阻塞队列中添加数据
     */
    public void put(Object obj) {
        try {
            // 往队列添加数据, 如果队列已满, 线程在此阻塞
            blockingQueue.put(obj);
            log.info("添加数据, thread：{}, value：{}", Thread.currentThread().getName(), obj);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 往阻塞队列中取走数据
     */
    public void take() {
        try {
            Object obj = blockingQueue.take();
            log.info("消费数据, thread:{}, value:{}", Thread.currentThread().getName(), obj);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
