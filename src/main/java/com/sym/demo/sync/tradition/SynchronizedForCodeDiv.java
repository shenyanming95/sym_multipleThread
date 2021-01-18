package com.sym.demo.sync.tradition;

/**
 * synchronized修饰代码块
 *
 * @auther: shenyanming
 * Created on 2018-12-07 15:19
 */
public class SynchronizedForCodeDiv implements Runnable {

    // 模拟：线程的共享变量
    private int ticket = 77;

    // run()代表线程要做的事
    @Override
    public void run() {
        /**
         * 如果去掉 synchronized，3个线程竞争cpu资源，执行结果时线程交叉打印 ticket 递减的值
         * 如果加上 synchronized，3个线程只有一个可以拿到 MyThread 对象的锁，执行结果时一个线程从77打印到1
         */
        System.out.println(Thread.currentThread().getName() + "开始执行");
        synchronized (this) {
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + ":" + ticket--);
            }
        }
        System.out.println(Thread.currentThread().getName() + "结束执行");
    }

    public static void main(String[] args) {
        // 实例化 SynchronizedForStaticMethod 对象，该对象就有一个同步锁，用此对象创建的线程相互之间才会互斥
        // 如果创建多个 SynchronizedForStaticMethod 对象，这些对象就各自拥有自己的同步锁，由这些对象创建的线程就不会互斥
        SynchronizedForStaticMethod myThread = new SynchronizedForStaticMethod();
        // 开启3个线程,谁抢到锁,谁就去执行
        new Thread(myThread, "线程1").start();
        new Thread(myThread, "线程2").start();
        new Thread(myThread, "线程3").start();
    }
}
