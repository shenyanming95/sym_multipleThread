package com.sym.sync.juc;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdk1.5以后，可以用Lock替代传统的synchronized线程锁，它具有更强悍的功能
 * 使用lock需要我们自己开锁，JVM不会再像用synchronized一样自动帮我们解锁
 * <p>
 * Created by 沈燕明 on 2018/12/27.
 */
public class ReentrantLockDemo {

    // 共享数据
    private static int data = 10;

    // 如果将Lock定义为成员变量,相当于给对象加锁
    private Lock memberLock = new ReentrantLock();

    // 如果将Lock定义为静态变量,相当于给整个类加锁
    private static Lock staticLock = new ReentrantLock();

    // 切记：把Lock定义成局部变量,是没办法起到同步效果的

    /**
     * 当Lock定义为成员变量,每创建一个对象,就拥有一把锁,Lock此时作用于实例对象
     */
    public void printWithLock() {
        memberLock.lock();
        try {
            if (data > -1) {
                System.out.println(Thread.currentThread().getName() + "：" + data--);
            }
        } finally {
            memberLock.unlock();
        }
    }

    @Test
    public void printWithLockTest() {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        // 多个线程共同操作同一个对象,保证共用一把锁
        for (int i = 1; i < 4; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    demo.printWithLock();
                }
            }, "线程" + i).start();
        }
    }

    /**
     * 当Lock定义为静态变量时,Lock此时作用于类,无论类创建多少个实例对象,都共有一把锁
     */
    public void printWithStaticLock() {
        staticLock.lock();
        try {
            if (data > -1) {
                System.out.println(Thread.currentThread().getName() + "：" + data--);
            }
        } finally {
            staticLock.unlock();
        }
    }

    @Test
    public void printWithStaticLockTest() {
        ReentrantLockDemo demo1 = new ReentrantLockDemo();
        ReentrantLockDemo demo2 = new ReentrantLockDemo();
        ReentrantLockDemo demo3 = new ReentrantLockDemo();
        // 当Lock作为静态变量,无论创建多少个实例对象,都共有同一把锁
        new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                demo1.printWithStaticLock();
            }
        }, "线程1").start();
        new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                demo2.printWithStaticLock();
            }
        }, "线程2").start();
        new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                demo3.printWithStaticLock();
            }
        }, "线程3").start();
    }

    /**
     * 通过 tryLock() 方法来获取锁
     */
    public void getLockByTryLock() {
        if (memberLock.tryLock()) {
            try {
                if (data > -1) {
                    System.out.println(Thread.currentThread().getName() + "：" + data--);
                }
            } finally {
                memberLock.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + "未获取到锁");
        }
    }

    @Test
    public void getLockByTryLockTest() {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        for (int i = 1; i < 4; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    demo.getLockByTryLock();
                }
            }, "线程" + i).start();
        }
    }

}
