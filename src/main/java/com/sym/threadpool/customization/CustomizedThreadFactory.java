package com.sym.threadpool.customization;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定制化的线程工厂{@link ThreadFactory},
 * 默认的线程池用的是{@link java.util.concurrent.Executors.DefaultThreadFactory}
 *
 * @author shenyanming
 * Created on 2019/9/6.
 */
public class CustomizedThreadFactory implements ThreadFactory {

    private static AtomicInteger number = new AtomicInteger(1);

    private static String prefix = "sym-customized-theadPool:";

    private ThreadGroup group;

    public CustomizedThreadFactory() {
        SecurityManager securityManager = System.getSecurityManager();
        group = Objects.nonNull(securityManager) ? securityManager.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, prefix + number.getAndIncrement(), 0);
        //设置线程优先级
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
