package com.sym.demo.sync.tradition;

/**
 * synchronized修饰类,即修饰类的类类型
 *
 * @auther: shenyanming
 * Created on 2018-12-07 15:21
 */
public class SynchronizedForClass implements Runnable {

    // 如果ticket改为静态类型,private static int ticket = 77 会有不一样的效果
    private int ticket = 77;

    // run()代表线程要做的事
    @Override
    public void run() {
        /*
         * synchronized修饰类,即对类的类类型加锁,由于一个类只有一个类类型,所以该类的所有实例对象都会互斥
         * 加锁的范围仅限于synchronized包裹起来的代码块，未被包裹的代码可以被其它未抢到锁的线程执行
         */
        System.out.println(Thread.currentThread().getName() + "开始执行");
        print();
        System.out.println(Thread.currentThread().getName() + "结束执行");
    }

    /**
     * synchronized修饰类，就是对类的类类型加锁,该类所有对象都会互斥,效果与修改静态方法一样.
     * 但是,要注意方法逻辑操作的是局部变量?还是成员变量?还是静态变量,不同的变量打印效果不一样
     */
    public void print() {
        synchronized (SynchronizedForClass.class) {
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + ":" + ticket--);

            }
        }
    }

    public static void main(String[] args) {
        // synchronized修饰类时,是对类的类类型加锁,由于一个类只有一个类类型,所以无论
        // 创建多少个实例对象,它们都是同一把锁，该类所有实例对象都会产生互斥效果。
        SynchronizedForClass myThreadOne = new SynchronizedForClass();
        SynchronizedForClass myThreadTwo = new SynchronizedForClass();
        SynchronizedForClass myThreadThree = new SynchronizedForClass();
        // 开启3个线程,谁抢到锁,谁就去执行
        new Thread(myThreadOne, "线程1").start();
        new Thread(myThreadTwo, "线程2").start();
        new Thread(myThreadThree, "线程3").start();
    }
}
