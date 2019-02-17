package com.sym.threadLocal;
import java.util.Random;
/**
 * 当多线程访问同一个数据时没有作"线程范围内数据共享"，每个线程访问到的数据都是一样的;
 * 什么是"线程范围内数据共享"===》线程与线程间的数据是独立的，但线程自己上的程序数据时共享的,例如：数据库的事务管理器
 * 不使用线程范围内数据共享功能，线程A变量的赋值会被线程B覆盖掉，导致线程A在后面执行过程中用的都是线程B的设的值
 *
 * @Auther: shenym
 * @Date: 2018-12-21 13:53
 */
public class NoneThreadLocal {

    private volatile static int value = 0;

    public static void main(String[] args) {

        new Thread(() -> {
            value = new Random().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + "设值：" + value);
            new PrintA().print();
            new PrintB().print();

        }, "线程1").start();

        new Thread(() -> {
            value = new Random().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + "设值：" + value);
            new PrintA().print();
            new PrintB().print();

        }, "线程2").start();

    }

    static class PrintA {
        public void print() {
            System.out.println(Thread.currentThread().getName() + "-> A模块 ->" + value);
        }
    }

    static class PrintB {
        public void print() {
            System.out.println(Thread.currentThread().getName() + "-> B模块 ->" + value);
        }
    }

}
