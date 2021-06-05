package com.sym.demo.sync.tradition;

/**
 * synchronized修饰静态方法
 *
 * @auther: shenyanming
 * Created on 2018-12-07 15:20
 */
public class SynchronizedForStaticMethod implements Runnable {

    private static int ticket = 77;

    // run()代表线程要做的事
    @Override
    public void run() {
        /**
         * 当修饰static方法时,是在类级别上加锁,该类所有的实例对象都会存在互斥效果
         * 加锁的范围仅限于synchronized的方法之内,意味着方法之外的代码可以被其它未抢到锁的线程执行
         */
        System.out.println(Thread.currentThread().getName() + "开始执行");
        print();
        System.out.println(Thread.currentThread().getName() + "结束执行");
    }

    /**
     * synchronized修饰静态方法,关键字放在public和static之间,表示整个方法处于互斥状态
     */
    public synchronized static void print() {
        while (ticket > 0) {
            System.out.println(Thread.currentThread().getName() + ":" + ticket--);
        }
    }

    public static void main(String[] args) {
        // synchronized修饰类的静态方法,是在类级别上加锁(静态方法不属于任何一个实例对象)
        // 所以无论创建多少个实例对象,它们都是同一把锁,该类所有实例对象都会产生互斥效果
        SynchronizedForStaticMethod myThreadOne = new SynchronizedForStaticMethod();
        SynchronizedForStaticMethod myThreadTwo = new SynchronizedForStaticMethod();
        SynchronizedForStaticMethod myThreadThree = new SynchronizedForStaticMethod();
        // 开启3个线程,谁抢到锁,谁就去执行
        new Thread(myThreadOne, "线程1").start();
        new Thread(myThreadTwo, "线程2").start();
        new Thread(myThreadThree, "线程3").start();
    }
}
