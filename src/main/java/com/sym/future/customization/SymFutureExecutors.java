package com.sym.future.customization;

import com.sym.future.customization.component.SymFuture;

import java.util.concurrent.*;

/**
 * 异步执行器
 *
 * @author shenyanming
 * @date 2020/6/7 17:01.
 */

public class SymFutureExecutors {

    // 暂时不用线程池
    private static ThreadPoolExecutor executor;

    static{
        executor = new ThreadPoolExecutor(10, 20, 2, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(200),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static <V> SymFuture<V> submit(Callable<V> callable){
        SymFuture<V> future = new SymFuture<>();
        // 暂时不用线程池实现
        new Thread(()->{
            try {
                V call = callable.call();
                future.setResult(call);
            } catch (Exception e) {
                // TODO 异常处理
                e.printStackTrace();
            }
        }).start();
        return future;
    }
}
