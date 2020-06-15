package com.sym.threadpool.future;

/**
 * {@link IFuture}的监听器
 *
 * @author shenyanming
 * @date 2020/6/15 21:12.
 */

public interface IFutureListener<V> {

    /**
     * {@link IFuture}执行成功的回调
     *
     * @param v 异步执行结果
     */
    void onComplete(V v);


    /**
     * {@link IFuture}执行失败的回调
     *
     * @param t 失败原因
     */
    void onError(Throwable t);

}
