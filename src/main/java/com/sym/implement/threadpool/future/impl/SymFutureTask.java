package com.sym.implement.threadpool.future.impl;

import com.sym.implement.threadpool.future.IFuture;
import lombok.Data;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * {@link Runnable}的适配类
 *
 * @author shenyanming
 * @date 2020/6/15 22:18.
 */
@Data
public class SymFutureTask<V> implements Runnable {

    private V result;
    private IFuture<V> future;
    private Callable<V> delegate;

    public SymFutureTask(IFuture<V> future, Callable<V> callable) {
        this.future = Objects.requireNonNull(future);
        this.delegate = Objects.requireNonNull(callable);
    }

    @Override
    public void run() {
        try {
            result = delegate.call();
        } catch (Exception e) {
            // TODO 暂时抛出运行时异常
            throw new RuntimeException(e);
        }
    }

}
