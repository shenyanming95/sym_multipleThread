package com.sym.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建单线程化的线程池，在同一时刻，只有一个线程处于运行状态
 * 通过SingleThreadExecutor创建，通过execute()方法加入任务
 */
public class SingleThreadPool {
    public static void main(String[] args) {
        // 创建单线程的线程池,同一时间内只有一个线程在执行
        ExecutorService pool = Executors.newSingleThreadExecutor();
        // 创建10个线程，放入到线程池中执行，每个线程相当于一个任务
        for (int i = 0; i < 10; i++) {
            final int index = i;
            pool.execute(() -> {
                String name = Thread.currentThread().getName();
                System.out.println(name + " == " + (index + 1));
            });
        }
        //关闭线程池，不在接收新的任务，处于关闭状态，等所有执行的线程都执行完以后，终止线程池
        pool.shutdown();

    }

}
