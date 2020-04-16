package com.sym.other.question;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程练习题目：线程依次执行：T1执行完执行T2，T2执行完执行T3
 * @author ym.shen
 * create one 2018-07-13 16:28
 */
public class MultiThreadQuestion0 {

    Lock lock = new ReentrantLock();
    int i = 0;
    /**
     * 线程1的唤醒条件
     */
    Condition c1 = lock.newCondition();
    /**
     * 线程2的唤醒条件
     */
    Condition c2 = lock.newCondition();
    /**
     * 线程3的唤醒条件
     */
    Condition c3 = lock.newCondition();


    public static void main(String[] args) {

        useJoin();
        //useCondition();
    }

    /**
     * 使用join方法很简单地就可以实现了
     */
    public static void useJoin() {

        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程1执行完");
            }
        });
       final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2执行完");
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程3执行完");
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 使用condition来唤醒相应的线程
     */
    public static void useCondition() {
        try {

            final MultiThreadQuestion0 obj = new MultiThreadQuestion0();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        obj.runTwo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        obj.runThree();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        obj.runFour();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runTwo() throws Exception {
        lock.lock();
        try {

            while (i != 0) {
                c1.await();
            }
            i = 1;
            System.out.println("线程1执行完毕");
            c2.signal();

        } finally {
            lock.unlock();
        }
    }

    public void runThree() throws Exception {
        lock.lock();
        try {

            while (i != 1) {
                c2.await();
            }
            i = 2;
            System.out.println("线程2执行完毕");
            c3.signal();

        } finally {
            lock.unlock();
        }
    }

    public void runFour() throws Exception {
        lock.lock();
        try {

            while (i != 2) {
                c3.await();
            }
            i = 0;
            System.out.println("线程3执行完毕");
            c1.signal();

        } finally {
            lock.unlock();
        }
    }

}
