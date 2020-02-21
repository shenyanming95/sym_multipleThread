package com.sym.threadPool.customization;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义的拒绝策略
 *
 * Created by shenym on 2019/9/6.
 */
public class CustomizedRejectedExecutionHandler implements RejectedExecutionHandler {

    /**
     * 自定义拒绝策略：可以日志记录或者持久化存储不能执行的任务
     * @param r 当前任务
     * @param executor 当前线程池
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        //当前正在执行的任务数
        int activeCount = executor.getActiveCount();
        //已完成的任务数
        long completedTaskCount = executor.getCompletedTaskCount();
        int largestPoolSize = executor.getLargestPoolSize();
        long taskCount = executor.getTaskCount();
        System.out.println("线程池运行数："+activeCount+",无法执行任务："+r);
    }
}
