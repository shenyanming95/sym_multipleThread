package com.sym.implement.threadpool.future.pool;

import com.sym.implement.threadpool.future.IFuture;
import com.sym.implement.threadpool.future.impl.SymFuture;
import com.sym.implement.threadpool.future.impl.SymFutureTask;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 扩展JDK自带的线程池{@link ThreadPoolExecutor}, 重写它的监控方法,
 * 以满足{@link IFuture}的实现逻辑
 *
 * @author shenyanming
 * @date 2020/6/15 21:52.
 */
public class FutureThreadPool extends ThreadPoolExecutor {

    public FutureThreadPool() {
        this(10, 20, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
    }

    public FutureThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public FutureThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public FutureThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public FutureThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 重写掉原生线程池{@link ThreadPoolExecutor#submit(Callable)}方法,
     * 将自己封装的{@link SymFutureTask}给原生线程池去实现
     *
     * @param task 任务
     * @param <T>  结果
     * @return 异步结果
     */
    @Override
    public <T> IFuture<T> submit(Callable<T> task) {
        Objects.requireNonNull(task);
        IFuture<T> future = new SymFuture<>();
        SymFutureTask<T> futureTask = new SymFutureTask<>(future, task);
        super.execute(futureTask);
        return future;
    }

    /**
     * 重写原生线程池的{@link ThreadPoolExecutor#afterExecute(Runnable, Throwable)},
     * 在任务执行后, 操作异步结果{@link IFuture}
     *
     * @param r 任务类
     * @param t 异常信息
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if(r instanceof SymFutureTask){
            // 强转成 SymFutureTask 对象, 取出它的 IFuture 对象
            SymFutureTask<?> futureTask = (SymFutureTask<?>) r;
            IFuture future = futureTask.getFuture();

            // 若异常信息为null, 说明没有抛出异常, 异步执行成功
            if (Objects.isNull(t)) {
                future.setResult(futureTask.getResult());
            } else {
                // 反之说明异步执行失败
                future.setError(t);
            }
        }
    }
}
