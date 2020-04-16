package com.sym.threadlocal;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 线程本地变量ThreadLocal例子
 *
 * @Auther: shenym
 * @Date: 2019-02-12 14:16
 */
public class ThreadLocalDemo {

    // 线程共享数据, 会存在多线程安全问题
    private volatile static int value = 0;

    // 定义一个ThreadLocal用来保存Data对象, 它不需要做同步处理
    private static ThreadLocal<ThreadData> threadDataLocal = new ThreadLocal<>();

    // 无法解决父子线程的数据继承问题
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    // 可以解决父子线程的数据继承问题
    private InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    /**
     * 当多线程访问同一个数据时没有作"线程范围内数据共享"，每个线程访问到的数据都是一样的;
     * 什么是"线程范围内数据共享"===》线程与线程间的数据是独立的，但线程自己上的程序数据时共享的,例如：数据库的事务管理器;
     * 不使用线程范围内数据共享功能，线程A变量的赋值会被线程B覆盖掉，导致线程A在后面执行过程中用的都是线程B的设的值
     */
    @Test
    public void commonTest() {
        for (int i = 1; i < 3; i++) {
            String name = "线程" + i;
            new Thread(() -> {
                value = new Random().nextInt(1000);
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + "设值：" + value);
                System.out.println(threadName + "-> A模块 ->" + value);
                System.out.println(threadName + "-> B模块 ->" + value);
            }, name).start();
        }
        sync();
    }

    /**
     * JDK为我们提供了ThreadLocal来实现"线程范围内数据共享",一个ThreadLocal只能保存一个变量;
     * 如果需要保存多个对象，可以把这些变量保存到一个对象中，再将这个对象保存到ThreadLocal中，推荐使用这种方式
     */
    @Test
    public void threadLocalTest() {
        SecureRandom random = new SecureRandom();
        for (int i = 1; i < 3; i++) {
            String name = "线程" + i;
            new Thread(() -> {
                ThreadData data = new ThreadData(random.nextInt(120), name + "的变量");
                System.out.println(name + "赋值：" + data);
                // 将线程的变量放到threadLocal中
                threadDataLocal.set(data);
                // 通过threadLocal获取线程本地变量
                ThreadData threadData = threadDataLocal.get();
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + "-> A模块 ->" + threadData);
                System.out.println(threadName + "-> B模块 ->" + threadData);
                // 用完就要删除，不然易造成内存泄露
                threadDataLocal.remove();
            }, name).start();
        }
        sync();
    }

    /**
     * 父子线程的意思：在一个线程的run()方法内再创建一个线程.
     * ThreadLocal不能满足父子线程的数据读取，即子线程不能获取父线程的ThreadLocal数据
     */
    @Test
    public void inheritThreadLocalTest() {
        // threadLocal
        new Thread(() -> {
            threadLocal.set("测试数据");
            System.out.println(Thread.currentThread().getName() + "-" + threadLocal.get());
            // 线程内部创建子线程.
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "-" + threadLocal.get());
            }, "threadLocal-子线程").start();
        }, "threadLocal-父线程").start();

        // inheritThreadLocal
        new Thread(() -> {
            inheritableThreadLocal.set("测试数据");
            System.out.println(Thread.currentThread().getName() + "-" + inheritableThreadLocal.get());
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "-" + inheritableThreadLocal.get());
            }, "inheritThreadLocal-子线程").start();
        }, "inheritThreadLocal-父线程").start();

        sync();
    }


    private void sync() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
