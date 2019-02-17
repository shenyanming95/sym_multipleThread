package com.sym.callableAndFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 默认一个需要进行复杂运算的过程，主线程将这个复杂运算交于子线程执行，
 * 主线程继续干自己的活儿，等需要获取复杂运算结果时，再去异步获取子线程执行的返回值
 */
public class MainThread {
    public static void main(String[] args) throws Exception {
        System.out.println("主程序开始运行...");
        ExecutorService pool = Executors.newSingleThreadExecutor();
        //开启子线程去执行复杂的运算
        Future<Integer> result = pool.submit(new SunThread());
        //执行shutdown()方法，等当前线程执行完，关闭线程池
        pool.shutdown();

        System.out.println("让子线程去运算...主线程继续干自己的事");
        // 模拟主线程干自己的事
        Thread.sleep(5000);

        // 在需要返回值的时候，用get()方法获取返回值,此时如果子线程仍未计算完毕
        // 主线程会在这边阻塞，等待执行结果
        System.out.println("主程序取到子线程运算完的值result=" + result.get());
        System.out.println("主程序结束运行...");
    }
}
