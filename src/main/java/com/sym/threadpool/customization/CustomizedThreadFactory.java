package com.sym.threadpool.customization;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂{@Link ThreadFactory},默认的线程池用的是{@link java.util.concurrent.Executors.DefaultThreadFactory}
 *
 * Created by shenym on 2019/9/6.
 */
public class CustomizedThreadFactory implements ThreadFactory {

    private static AtomicInteger number = new AtomicInteger(1);

    private String prefix = "sym-customized-theadPool:";

    private SecurityManager s = System.getSecurityManager();

    private ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group,r,prefix+number.getAndIncrement(),0);
        t.setPriority(Thread.NORM_PRIORITY);//设置线程优先级
        return t;
    }
}
