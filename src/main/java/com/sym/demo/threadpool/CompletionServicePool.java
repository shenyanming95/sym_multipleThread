package com.sym.demo.threadpool;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * JDK的{@link CompletionService}, 可以向里面添加异步任务,
 * 然后哪个任务先执行完, 那个任务就会先返回. 它内部其实维护了
 * 一个阻塞队列, 然后重写了{@link FutureTask}, 当实际线程池
 * 执行完任务以后, 将该{@link FutureTask}添加到队列中, 这样就可以实现
 * 先执行完的任务先返回.
 *
 * @author shenyanming
 * Created on 2020/8/19 08:57
 */
public class CompletionServicePool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // CompleteService需要一个线程池作为实际执行任务的组件
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        // 提交任务, 任务的完成时间各有差异
        Future<String> f1 = completionService.submit(() -> {
            Thread.sleep(2000);
            return "2秒~";
        });
        Future<String> f2 = completionService.submit(() -> {
            Thread.sleep(4000);
            return "4秒~";
        });
        Future<String> f3 = completionService.submit(() -> {
            Thread.sleep(6000);
            return "6秒~";
        });

        // 通过CompletionService的take()方法可以获取内部阻塞队列的已完成的Future实例
        for (int i = 0; i < 3; i++) {
            String s = completionService.take().get();
            System.out.println(s);
        }
        executorService.shutdown();
    }
}
