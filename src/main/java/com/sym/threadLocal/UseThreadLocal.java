package com.sym.threadLocal;
/**
 * JDK为我们提供了ThreadLocal来实现"线程范围内数据共享",一个ThreadLocal只能保存一个变量，
 * 如果需要保存多个对象，可以把这些变量保存到一个对象中，再将这个对象保存到ThreadLocal中，推荐使用这种方式
 *
 * Created by 沈燕明 on 2018/12/21.
 */
public class UseThreadLocal {

    // 定义一个ThreadLocal用来保存Data对象，它不需要做同步处理
    private static ThreadLocal<ThreadData> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {

        // 开启两个线程
        new Thread(() -> {
            ThreadData data = new ThreadData(1, "线程1的变量");
            System.out.println("线程1赋值："+data);
            // 将线程1的变量放到threadLocal中
            threadLocal.set(data);
            // 打印信息
            new PrintA().print();
            new PrintB().print();
            // 用完就要删除，不然会造成内存泄露
            threadLocal.remove();
        }, "线程1").start();

        new Thread(() -> {
            ThreadData data = new ThreadData(2, "线程2的变量");
            System.out.println("线程2赋值："+data);
            // 将线程2的变量放到threadLocal中
            threadLocal.set(data);
            // 打印信息
            new PrintA().print();
            new PrintB().print();
            // 用完就要删除，不然会造成内存泄露
            threadLocal.remove();
        }, "线程2").start();


    }

    static class PrintA {
        public void print() {
            // 通过threadLocal获取线程本地变量
            ThreadData threadData = threadLocal.get();
            System.out.println(Thread.currentThread().getName() + "-> A模块 ->" + threadData);
        }
    }

    static class PrintB {
        // 通过threadLocal获取线程本地变量
        ThreadData threadData = threadLocal.get();
        public void print() {
            System.out.println(Thread.currentThread().getName() + "-> B模块 ->" + threadData);
        }
    }
}
