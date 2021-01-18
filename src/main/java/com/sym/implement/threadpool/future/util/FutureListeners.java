package com.sym.implement.threadpool.future.util;

import com.sym.implement.threadpool.future.IFutureListener;

import java.util.function.Consumer;

/**
 * {@link IFutureListener}的工厂类
 *
 * @author shenyanming
 * @date 2020/6/15 22:39.
 */
public class FutureListeners {

    /**
     * 返回只处理异步任务执行成功的监听器
     *
     * @param consumer 消费逻辑
     * @param <V>      异步执行结果
     * @return IFutureListener
     */
    public static <V> IFutureListener<V> completeListener(Consumer<V> consumer) {
        return new IFutureListener<V>() {
            @Override
            public void onComplete(V v) {
                consumer.accept(v);
            }
            @Override
            public void onError(Throwable t) {
                // do nothing
            }
        };
    }


    /**
     * 返回只处理异步任务执行失败的监听器
     *
     * @param consumer 消费逻辑
     * @param <V>      异步执行结果
     * @return IFutureListener
     */
    public static <V> IFutureListener<V> errorListener(Consumer<Throwable> consumer) {
        return new IFutureListener<V>() {
            @Override
            public void onComplete(V v) {
                // do nothing
            }
            @Override
            public void onError(Throwable t) {
                consumer.accept(t);
            }
        };
    }
}
