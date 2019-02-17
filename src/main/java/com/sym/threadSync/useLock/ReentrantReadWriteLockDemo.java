package com.sym.threadSync.useLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 之前的synchronized和Lock都是排它锁，啥是排它锁 ？即当一个线程在占用共享资源，其他线程都只能等待
 * jdk1.5以后，我们可以使用读写锁：当一个线程用读锁锁定资源时，其他线程也可以使用读锁读取资源，但是不能修改;
 * 当一个线程用写锁锁定资源时，其他线程都不能进入该资源区，等待该线程修改完数据
 * <p>
 * Created by 沈燕明 on 2018/12/27.
 */
public class ReentrantReadWriteLockDemo {

    // 共享变量
    private static String data = "10k";

    // 定义为成员变量，是给对象加锁
    private ReadWriteLock memberLock = new ReentrantReadWriteLock();

    // 定义为静态变量，是给类加锁
    private static ReadWriteLock staticLock = new ReentrantReadWriteLock();

    /**
     * 读取数据,获取的是读锁,读锁之间不互斥
     */
    public void readData() {
        memberLock.readLock().lock();
        try {
            String name = Thread.currentThread().getName();
            System.out.println(name + "开始读取数据...");
            try {
                // 便于观察
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(name + "结束读取数据...");
        } finally {
            memberLock.readLock().unlock();
        }
    }

    /**
     * 写入数据,获取的是写锁,写锁与写锁之间互斥,写锁与读锁之间也互斥
     */
    public void writeData(String s) {
        memberLock.writeLock().lock();
        try {
            String name = Thread.currentThread().getName();
            System.out.println(name + "开始修改data_");
            try {
                // 便于观察
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            data = s;
            System.out.println(name + "修改完成data=" + data + "");
        } finally {
            memberLock.writeLock().unlock();
        }
    }
    
    public static void main(String[] args) {
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        // 开启两个线程读数据
        for (int i = 1; i < 3; i++) {
            threadPool.execute(() -> {
                demo.readData();
            });
        }

        // 开启两个线程写数据
        for (int i = 3; i < 5; i++) {
            int j = i * 10;
            threadPool.execute(() -> {
                demo.writeData(j + "k");
            });
        }

        threadPool.shutdown();
    }
}
