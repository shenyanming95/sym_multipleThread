package com.sym.syncTool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * java同步工具之一：semaphore，可以很轻松地完成信号量控制，跟synchronized类似
 * 不过synchronized是只允许一个线程访问资源，而semaphore可以允许多个线程访问资源
 * 通过acquire()获取许可，通过release()释放许可。Semaphore的作用就是：让获取到许可
 * 信号的n个线程同时执行。
 *
 * 假设游戏室一次只能容纳3个人，总共有7个人要玩，每次满了以后其他人就要等待游戏室的人出来。
 */
public class SemaphoreDemo {

    // 指定只有3个许可信号,意味着同一时间只有3个线程可以执行
    private Semaphore semaphore = new Semaphore(3);

    /**
     *
     */
    public void play(){
        try {
            try {
                // 申请信号量许可。跟Lock一样，也需要放在try-finally块里面
                semaphore.acquire();
                // 抢到信号的线程执行
                String name = Thread.currentThread().getName();
                System.out.println(name+"开始玩游戏...信号量剩余："+semaphore.availablePermits());
                Thread.sleep(1500);
                System.out.println(name+"不玩了...");
            }finally {
                // 释放信号量许可
                semaphore.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SemaphoreDemo demo = new SemaphoreDemo();
        ExecutorService threadPool = Executors.newFixedThreadPool(6);
        for( int i=0;i<6;i++ ){
            final int j = i+1;
            threadPool.execute(()->{
                Thread.currentThread().setName("线程"+j);
                demo.play();
            });
        }
        threadPool.shutdown();
    }

}
