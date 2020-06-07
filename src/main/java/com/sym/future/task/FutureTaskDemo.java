package com.sym.future.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask类实现Runnable和Future接口，因此既可以作为Runnable被Thread执行，也可以作为
 * Future作为Callable的返回值，使用线程池的submit()方法来执行FutureTask
 * @author shenyanming
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("主线程开始运行... ...");
        FutureTask<Integer> result = new FutureTask<>(() -> {
            // 子线程执行逻辑...
            Thread.sleep(2000);
            return 12580;
        });
        //使用线程池执行线程
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(result);
        pool.shutdown();
        // 主线程把复杂任务交于子线程执行,继续干自己的事
        System.out.println("主线程干别的事...");
        Thread.sleep(5000);
        // 主线程获取子线程执行结果,如果此时子线程仍未执行完,则主线程会阻塞等待它执行
        System.out.println("获取子线程的返回值 result=" + result.get());
        System.out.println("主线程结束运行... ...");
    }
}
