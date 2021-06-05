package com.sym.demo.threadmethod;

import com.sym.util.ThreadUtil;

/**
 * 三个中断方法：
 * {@link Thread#interrupt()}、@link Thread#isInterrupted()}、{@link Thread#interrupted()}
 *
 * @author shenyanming
 * Created on 2020/8/19 11:04
 */
public class ThreadInterruptApiDemo {

    public static void main(String[] args) {
        interruptTest();
        isInterruptedTest();
        interruptedTest();
    }

    /**
     * {@link Thread#interrupt()}可以中断某个线程，这里的中断是将线程的中断标志设置为true, 分为两种情况：
     * 1、对于阻塞的线程，调用interrupt()方法会立即抛出InterruptedException异常
     * 2、对于正在运行的线程，调用interrupt()方法只是标志它为中断状态，并不会停止线程的运行
     */
    static void interruptTest() {
        // 对于阻塞的线程，调用interrupt()方法会抛出InterruptedException异常
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 对于正在运行的线程，只会标志线程的中断状态，而不会停止线程的运行
        Thread t2 = new Thread(() -> {
            for (; ; ) {
                // 当t2线程被中断后，并不会立即退出，仍在继续运行
                try {
                    Thread.sleep(500);
                    System.out.println("t2 thread running...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 启动线程
        t1.start();
        t2.start();
        // 主线程分别中断t1和t2线程
        try {
            Thread.sleep(1000);
            t1.interrupt();
            t2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 主线程阻塞同步
        ThreadUtil.keepAlive();
    }


    /**
     * {@link Thread#isInterrupted()}是用来获取指定线程的中断状态, 若线程已被中断则返回true，
     * 若线程未被中断则返回false. 注意：此方法不会清除线程的中断状态，仅仅只是作为一个判断而已
     */
    static void isInterruptedTest() {
        Thread t1 = new Thread(() -> {
            for (; ; ) {
                // 线程t1在此一直运行
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
        t1.start();

        System.out.println("线程t1是否中断?" + t1.isInterrupted());
        t1.interrupt();
        System.out.println("线程t1是否中断?" + t1.isInterrupted());
        System.out.println("线程t1是否中断?" + t1.isInterrupted());
        ThreadUtil.keepAlive();
    }


    /**
     * {@link Thread#interrupted()}用来获取当前线程的中断状态，当前线程已中断返回true，反之返回false
     * 注意：此方法会清除当前线程的中断状态，即连续紧接两次调用interrupted()，第二次必定返回false
     */
    static void interruptedTest() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    // 因为t1线程未被中断，所以Thread.interrupted()返回false
                    System.out.println("t1线程是否中断?" + Thread.interrupted());
                    // 开始中断t1线程
                    Thread.currentThread().interrupt();
                    // 紧连两次调用Thread.interrupted()，第二次必定返回false，因为interrupted()方法会清除当前线程的中断状态
                    System.out.println("t1线程是否中断?" + Thread.interrupted());
                    System.out.println("t1线程是否中断?" + Thread.interrupted());
                }
            }
        });
        t1.start();
        ThreadUtil.keepAlive();
    }
}
