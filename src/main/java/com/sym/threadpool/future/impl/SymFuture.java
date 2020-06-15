package com.sym.threadpool.future.impl;

import com.sym.threadpool.future.IFuture;
import com.sym.threadpool.future.IFutureListener;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.stream.Collectors;

/**
 * {@link IFuture}实现
 *
 * @author shenyanming
 * @date 2020/6/7 16:19.
 */
public class SymFuture<V> implements IFuture<V> {

    private final static AtomicReferenceFieldUpdater<SymFuture, Boolean> atomicReferenceFieldUpdater =
            AtomicReferenceFieldUpdater.newUpdater(SymFuture.class, Boolean.class, "doCallback");

    // 全局锁
    private final Object LOCK = new Object();
    // 异步执行结果
    private volatile V result;
    // 标志当前任务是否取消
    private boolean isCancel;
    // 当异步执行失败时的原因
    private volatile Throwable throwable;
    // 监听器集合
    private List<IFutureListener<V>> listeners;
    // 标识是否已经回调过监听器
    volatile Boolean doCallback;
    // 执行当前Future的线程
    private Thread thread;

    public SymFuture() {
    }

    /**
     * 取消当前任务的执行:
     * 1.若任务还未执行, 则返回true;
     * 2.若任务已经执行完, 则返回false;
     * 3.若任务正在执行, 尝试中断它执行.
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // TODO 取消逻辑
        return false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancel;
    }

    @Override
    public boolean isDone() {
        return Objects.nonNull(result) || isCancel || Objects.nonNull(throwable);
    }

    @Override
    public boolean isSuccess() {
        return Objects.nonNull(result);
    }

    /**
     * 获取执行结果, 若任务未执行完, 则阻塞等待
     *
     * @return 异步执行结果
     * @throws InterruptedException
     */
    @Override
    public V get() throws InterruptedException {
        if (isDone()) {
            return this.result;
        }
        // 若未执行成功, 则需要等待
        synchronized (LOCK) {
            while (Objects.isNull(result)) {
                LOCK.wait();
            }
        }
        return result;
    }

    /**
     * 获取执行结果, 若任务未执行完, 则阻塞等待并设置超时等待时间
     */
    @Override
    public V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        // TODO 超时等待逻辑
        return null;
    }


    @Override
    public void setResult(V v) {
        this.set(v, null);
    }

    @Override
    public void setError(Throwable throwable) {
        this.set(null, throwable);
    }

    @Override
    public void addListener(IFutureListener<V> listener) {
        this.addListener(Collections.singleton(Objects.requireNonNull(listener)));
    }

    @Override
    public void addListener(Collection<IFutureListener<V>> listeners) {
        Objects.requireNonNull(listeners);
        // 如果还未初始化
        initListenerIfRequire();
        // 检测是否可以添加监听器
        checkCanAddListener();
        // 添加集合
        this.listeners.addAll(listeners.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /**
     * 设置成功结果或者异常结果
     *
     * @param v 成功结果
     * @param t 异常结果
     */
    private void set(V v, Throwable t) {
        if (Objects.nonNull(result)) {
            throw new UnsupportedOperationException("已执行完毕, 不能再设置");
        }
        synchronized (LOCK) {
            if (Objects.nonNull(result)) {
                throw new UnsupportedOperationException("已执行完毕, 不能再设置");
            }
            boolean isSuccess = Objects.nonNull(v);
            if (isSuccess) {
                this.result = v;
            } else {
                this.throwable = t;
            }
            // 唤醒所有
            LOCK.notifyAll();
            // 执行监听器
            doCallback(isSuccess);
        }
    }

    /**
     * 初始化监听器集合
     */
    private void initListenerIfRequire() {
        if (Objects.nonNull(listeners)) {
            return;
        }
        synchronized (LOCK) {
            if (Objects.isNull(listeners)) {
                listeners = new CopyOnWriteArrayList<>();
            }
        }
    }

    /**
     * 判断当前条件下, 是否允许添加监听器
     */
    private void checkCanAddListener() {
        if (Objects.isNull(result) && Objects.isNull(throwable)) {
            // 说明任务还没执行, 或者正在执行
            return;
        }
        if (doCallback) {
            throw new UnsupportedOperationException("已回调完毕, 不能再添加监听器");
        }
        // TODO 如果代码执行到这, 任务执行完了的处理
    }

    /**
     * 回调监听器
     *
     * @param isSuccess 为true时执行成功回调, 反之执行异常回调
     */
    private void doCallback(boolean isSuccess) {
        if (atomicReferenceFieldUpdater.compareAndSet(this, null, true)) {
            int alreadyDone = 0;
            for (; ; ) {
                // 回调监听器
                int total = listeners.size();
                if (alreadyDone == total) {
                    // 避免在回调的过程中, 又有监听器添加进来
                    break;
                }
                if (isSuccess) {
                    for (IFutureListener<V> listener : listeners) {
                        listener.onComplete(result);
                        alreadyDone++;
                    }
                } else {
                    for (IFutureListener<V> listener : listeners) {
                        listener.onError(throwable);
                        alreadyDone++;
                    }
                }
            }
        }
    }

}
