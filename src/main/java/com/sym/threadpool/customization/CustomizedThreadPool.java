package com.sym.threadpool.customization;

import java.util.concurrent.*;

/**
 * 自定义线程池
 *
 * Created by shenym on 2019/9/6.
 */
public class CustomizedThreadPool {

    public static void main(String[] args) throws InterruptedException {
        CustomizedThreadPool customizedThreadPool = new CustomizedThreadPool();
        ThreadPoolExecutor threadPool = customizedThreadPool.threadPoolExecutor();
        for( int i = 0 ; i < 10 ; i++ ){
            threadPool.execute(()->{
                // 线程都睡眠5s
                try {
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName()+",执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    /**
     * 获取一个线程池
     * @return
     */
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(
                        1,
                    2,
                       20 , TimeUnit.SECONDS,
                     blockingQueue(),
                     threadFactory(),
                     rejectedExecutionHandler()
        );
    }


    /**
     * 获取自定义的线程工厂
     * @return
     */
    private ThreadFactory threadFactory(){
        return new CustomizedThreadFactory();
    }

    /**
     * 获取自定义的拒绝策略
     * @return
     */
    private RejectedExecutionHandler rejectedExecutionHandler(){
        return new CustomizedRejectedExecutionHandler();
    }

    /**
     * 获取阻塞队列
     * @return
     */
    private BlockingQueue<Runnable> blockingQueue(){
        return new ArrayBlockingQueue<>(2);
    }
}
