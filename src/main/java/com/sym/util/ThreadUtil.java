package com.sym.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程的一些常用工具
 *
 * @author ym.shen
 * Created on 2020/4/16 11:46
 */
public class ThreadUtil {

    private final static Lock LOCK = new ReentrantLock();
    private final static Condition STOP = LOCK.newCondition();

    /**
     * 让程序保持运行, 在多线程环境下防止程序退出
     */
    public static void keepAlive() {
        keepAlive(null);
    }

    /**
     * 让程序保持运行, 在多线程环境下防止程序退出
     *
     * @param callbackWhenExist 钩子函数, 在程序退出时回调, 一般用来处理资源
     */
    public static void keepAlive(Runnable callbackWhenExist) {
        if (null != callbackWhenExist) {
            Runtime.getRuntime().addShutdownHook(new Thread(callbackWhenExist));
        }
        LOCK.lock();
        try {
            STOP.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 退出程序
     */
    public static void exist() {
        if (System.getSecurityManager() != null) {
            // 存在安全管理器, 则用 AccessController 来执行
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                STOP.signalAll();
                System.exit(0);
                return null;
            }, AccessController.getContext());
        } else {
            // 不存在安全管理, 直接执行业务逻辑即可
            STOP.signalAll();
            System.exit(0);
        }
    }

    /**
     * 创建一个默认的线程池
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getThreadPool() {
        return new ThreadPoolExecutor(
                10,
                20,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(64),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
