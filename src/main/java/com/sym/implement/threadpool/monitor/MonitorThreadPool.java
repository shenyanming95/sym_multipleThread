package com.sym.implement.threadpool.monitor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 继承线程池{@link ThreadPoolExecutor}来实现对线程池的一些监控功能.
 * 为了更好地记录任务，我们不能仅仅使用{@link Runnable}对象，这样获取
 * 不到任何信息，更多的是实现它加入系统需要的一些属性，如{@link AbstractMonitorRunnable}
 *
 * @author shenyanming
 * Created on 2019/9/7.
 */
@Slf4j
public class MonitorThreadPool extends ThreadPoolExecutor {

    public MonitorThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                             TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 这个方法会在任务被执行之前调用,可用于重新初始化线程局部变量或执行日志记录；
     * 注意：为了正确嵌套多个重写，子类通常应该在该方法的开头调用super.afterexecute
     *
     * @param t the thread that will run task r 用此线程来执行任务r
     * @param r the task that will be executed 将要被执行的任务r
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        if (r instanceof AbstractMonitorRunnable) {
            log.info("任务执行之前监控 ===== start");
            log.info("执行任务的线程ID：{}, 线程名称：{}", t.getId(), t.getName());
            log.info("当前任务信息：{}", r);
            log.info("任务执行之前监控 ===== end");
        }
        System.out.println();
    }

    /**
     * 这个方法会在任务被执行之后调用,当发生异常时，错误信息会被封装到参数t上
     * 注意：为了正确嵌套多个重写，子类通常应该在该方法的开头调用super.afterexecute()
     *
     * @param r the runnable that has completed 已被执行完的任务
     * @param t the exception that caused termination, or null if execution completed normally 导致终止的异常，如果执行正常，则返回null
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (r instanceof AbstractMonitorRunnable) {
            log.info("任务执行之后监控 ===== start");
            if (null != t) {
                log.info("任务[{}}]执行失败，原因：{}", r, t.getMessage());
            }
            // if ok do nothing
            log.info("任务执行之后监控 ===== end\n");
        }
    }


    /**
     * 线程池关闭前会被调用
     * 注意：要正确嵌套多个重叠，子类通常应在此方法中调用super.terminated
     */
    @Override
    protected void terminated() {
        super.terminated();
        log.info("线程池准备关闭,当前线程池状态 ====== start");
        log.info("历史创建过的最大线程数：{}", this.getLargestPoolSize());
        log.info("当前活跃的线程数：{}", getActiveCount());
        log.info("需要执行的任务数量：{}", this.getTaskCount());
        log.info("已执行的任务数量：{}", getCompletedTaskCount());
        // something you prefer to
        // ...
        log.info("线程池准备关闭,当前线程池状态 ====== end");
    }
}
