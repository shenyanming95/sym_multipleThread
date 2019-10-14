package com.sym;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 沈燕明 on 2018/12/22.
 */
public class MainTest {


    @Test
    public void testOne() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 1, TimeUnit.MINUTES, new SynchronousQueue<>());
        // 创建10个线程，放入到线程池中执行，每个线程相当于一个任务
        for (int i = 0; i < 10; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int index = i;
            threadPoolExecutor.execute(() -> {
                String name = Thread.currentThread().getName();
                System.out.println(name + " == " + (index + 1));
            });
        }
        //关闭线程池，不在接收新的任务，处于关闭状态，等所有执行的线程都执行完以后，终止线程池
        threadPoolExecutor.shutdown();
    }


    @Test
    public void testTwo(){
        // ReentrantLock,可重入锁,是Lock的一个实现类
        Lock lock = new ReentrantLock();
        lock.lock(); // 线程加锁
        try {
            System.out.println("线程执行...");
        }finally {
            lock.unlock(); // 线程解锁
        }
    }


    @Test
    public void testThree(){

    }

    @Test
    public void testFour(){
        Lock lock = new ReentrantLock();
        for( int i=1;i<=10;i++ ){
            new Thread(()->{
                if( lock.tryLock() ){
                    try{
                        System.out.println(Thread.currentThread().getName()+"获取到锁");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }finally {
                        lock.unlock();
                    }
                }else{
                    Thread thread = Thread.currentThread();
                    thread.interrupt();
                    System.out.println(thread.getName()+"是否终止："+thread.isInterrupted());
                }
            },"线程"+i).start();
        }
    }

    @Test
    public void testFive(){
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.wait(1000,TimeUnit.SECONDS.ordinal());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }finally {
            lock.unlock();
        }
    }



    @Test
    public void testSeven(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
            System.out.println("11");
        });
    }

    @Test
    public void testEight(){


    }
}
