package com.sym.future.customization.component;

/**
 * @author shenyanming
 * @date 2020/6/7 16:27.
 */

public interface FutureListener<V> {

    /**
     * {@link java.util.concurrent.Future}执行成功的回调
     * @param v 异步执行结果
     */
    void onComplete(V v);

}
