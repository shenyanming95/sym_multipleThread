package com.sym.future.customization.component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 简易版的{@link Future}实现, 并使用监听器, 避免了原先get()方法的阻塞
 *
 * @author shenyanming
 * @date 2020/6/7 16:19.
 */

public class SymFuture<V> implements Future<V> {

    private final static AtomicReferenceFieldUpdater<SymFuture, Boolean> atomicReferenceFieldUpdater =
            AtomicReferenceFieldUpdater.newUpdater(SymFuture.class, Boolean.class, "doCallback");

    private final Object LOCK = new Object();

    private volatile V result;
    private boolean isCancel;
    // TODO 异常处理
    private boolean isFail;
    private List<FutureListener<V>> listeners;
    volatile Boolean doCallback;

    public SymFuture() {
        this.result = null;
        this.isCancel = false;
    }


    /**
     * 暂不实现
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return isCancel;
    }

    @Override
    public boolean isDone() {
        return Objects.nonNull(result);
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (isDone()) {
            return this.result;
        }
        // 若未执行成功, 则需要等待
        synchronized (LOCK){
            while(result == null){
                LOCK.wait();
            }
        }
        return result;
    }

    /**
     * 暂不实现
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public void setResult(V v){
        if(Objects.nonNull(result)){
            throw new UnsupportedOperationException("已执行完毕, 不能再设置");
        }
        synchronized (LOCK){
            this.result = v;
            // 唤醒所有
            LOCK.notifyAll();
            // 执行监听器
            doCallback();
        }
    }

    public SymFuture<V> addListener(FutureListener<V> listener) {
        initListenerIfRequire();
        Objects.requireNonNull(listener);
        checkCanAddListener();
        this.listeners.add(listener);
        return this;
    }

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

    private void checkCanAddListener() {
        if (Objects.isNull(result)) {
            return;
        }
        if (doCallback) {
            throw new UnsupportedOperationException("已执行完毕, 不能再添加监听器");
        }
        // 说明结果执行完, 但是刚刚添加监听器
        doCallback();
    }

    private void doCallback(){
        if (atomicReferenceFieldUpdater.compareAndSet(this, null, true)) {
            int alreadyDone = 0;
            for (; ; ) {
                // 回调监听器
                int total = listeners.size();
                if (alreadyDone == total) {
                    break;
                }
                for (FutureListener<V> listener : listeners) {
                    listener.onComplete(result);
                    alreadyDone++;
                }
            }
        }
    }

}
