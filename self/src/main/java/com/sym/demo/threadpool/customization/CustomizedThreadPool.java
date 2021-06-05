package com.sym.demo.threadpool.customization;

import java.util.concurrent.*;

/**
 * 定制化的线程池{@link ThreadPoolExecutor}
 *
 * @author shenyanming
 * Created on 2019/9/6.
 */
public class CustomizedThreadPool {

    public static void main(String[] args) {
        CustomizedThreadPool customizedThreadPool = new CustomizedThreadPool();
        ThreadPoolExecutor threadPool = customizedThreadPool.threadPoolExecutor();
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                // 线程都睡眠5s
                try {
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName() + ",执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    /**
     * 创建一个线程池
     */
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(1, 2, 20, TimeUnit.SECONDS, blockingQueue(), threadFactory(), rejectedExecutionHandler());
    }


    /**
     * 创建自定义的线程工厂
     */
    private ThreadFactory threadFactory() {
        return new CustomizedThreadFactory();
    }

    /**
     * 创建自定义的拒绝策略
     */
    private RejectedExecutionHandler rejectedExecutionHandler() {
        return new CustomizedRejectedExecutionHandler();
    }

    /**
     * 创建阻塞队列
     */
    private BlockingQueue<Runnable> blockingQueue() {
        return new ArrayBlockingQueue<>(2);
    }
}
