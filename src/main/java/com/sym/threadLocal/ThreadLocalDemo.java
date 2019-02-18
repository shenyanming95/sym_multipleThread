package com.sym.threadLocal;

/**
 * 线程本地变量ThreadLocal例子
 *
 * @Auther: shenym
 * @Date: 2019-02-12 14:16
 */
public class ThreadLocalDemo {

    // 无法解决父子线程的数据继承问题
    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    // 可以解决父子线程的数据继承问题
    InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    
    public static void main(String[] args){

        ThreadLocalDemo demo = new ThreadLocalDemo();
        demo.testOne();
        //demo.testTwo();

    }

    /**
     * 父子线程的意思：在一个线程的run()方法内再创建一个线程
     * ThreadLocal不能满足父子线程的数据读取，即子线程不能获取父线程的ThreadLocal数据
     */
    public void testOne(){

        new Thread(()->{

            threadLocal.set("测试数据");
            System.out.println(Thread.currentThread().getName()+"-"+threadLocal.get());

            new Thread(()->{

                System.out.println(Thread.currentThread().getName()+"-"+threadLocal.get());

            },"子线程").start();

        },"父线程").start();

    }

    public void testTwo(){
        new Thread(()->{

            inheritableThreadLocal.set("测试数据");
            System.out.println(Thread.currentThread().getName()+"-"+inheritableThreadLocal.get());

            new Thread(()->{

                System.out.println(Thread.currentThread().getName()+"-"+inheritableThreadLocal.get());

            },"子线程").start();

        },"父线程").start();
    }

}
