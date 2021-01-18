package com.sym.demo.sync.tradition;

/**
 * synchronized修饰普通方法
 *
 * @auther: shenyanming
 * Created on 2018-12-07 15:20
 */
public class SynchronizedForMethod implements Runnable {

    private int ticket = 77;

    // run()代表线程要做的事
    @Override
    public void run() {
        /**
         * 当修饰普通方法时,是对某个实例对象加锁,不同的实例对象之间互不影响
         * 加锁的范围仅限于synchronized的方法之内,意味着方法之外的代码可以被其它未抢到锁的线程执行
         */
        System.out.println(Thread.currentThread().getName() + "开始执行");
        print();
        System.out.println(Thread.currentThread().getName() + "结束执行");
    }

    /**
     * synchronized修饰普通方法,关键字放在方法访问权限和返回值之间,表示整个方法处于互斥状态
     */
    public synchronized void print() {
        while (ticket > 0) {
            System.out.println(Thread.currentThread().getName() + ":" + ticket--);
        }
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
