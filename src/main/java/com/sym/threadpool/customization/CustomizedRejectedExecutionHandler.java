package com.sym.threadpool.customization;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定制化的线程池拒绝策略{@link RejectedExecutionHandler}
 *
 * @author shenyanming
 * Created on 2019/9/6.
 */
@Slf4j
public class CustomizedRejectedExecutionHandler implements RejectedExecutionHandler {

    /**
     * 自定义拒绝策略：可以日志记录或者持久化存储不能执行的任务
     *
     * @param r        当前任务
     * @param executor 当前线程池
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 活跃的线程数
        int activeCount = executor.getActiveCount();
        // 已完成的任务数
        long completedTaskCount = executor.getCompletedTaskCount();
        // 运行期间的最大线程数
        int largestPoolSize = executor.getLargestPoolSize();
        // 曾经完成的任务的总数
        long taskCount = executor.getTaskCount();

        log.error("线程池无法执行任务, 当前运行数据为：活跃线程数-{{}}, 已完成任务数-{{}}, 线程数最大峰值-{{}}",
                activeCount, completedTaskCount, largestPoolSize);
    }
}
