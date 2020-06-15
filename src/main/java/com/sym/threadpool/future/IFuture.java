package com.sym.threadpool.future;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * 简易版的{@link Future}扩展, 引入了监听器的概念, 可以避免了
 * {@link Future#get()}方法的阻塞.
 *
 * @author shenyanming
 * @date 2020/6/15 21:10.
 */
public interface IFuture<V> extends Future<V> {

    /**
     * 设置结果
     *
     * @param result 结果
     */
    void setResult(V result);

    /**
     * 设置异常
     *
     * @param throwable 异常出错的原因
     */
    void setError(Throwable throwable);

    /**
     * JDK的{@link Future}只有判断{@link Future#isDone()},
     * 这里多添加一个方法, 判断是否异步执行成功
     *
     * @return true-执行成功
     */
    boolean isSuccess();

    /**
     * 添加监听器
     *
     * @param listener 监听器
     */
    void addListener(IFutureListener<V> listener);

    /**
     * 批量添加监听器
     *
     * @param listeners 监听器集合
     */
    void addListener(Collection<IFutureListener<V>> listeners);
}
