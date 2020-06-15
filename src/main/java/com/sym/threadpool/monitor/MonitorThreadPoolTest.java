package com.sym.threadpool.monitor;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 线程池监控的测试类
 *
 * Created by shenYm on 2019/9/7.
 */
public class MonitorThreadPoolTest {

    private MonitorThreadPool symMonitorThreadPool;

    @Before
    public void init(){
        symMonitorThreadPool = new MonitorThreadPool(2,
                4,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(6));
    }

    /**
     * 执行一个成功的线程
     * @throws InterruptedException
     */
    @Test
    public void testOne() throws InterruptedException {
        AbstractMonitorRunnable runnable = new AbstractMonitorRunnable(){
            private static final long serialVersionUID = 1188315023040219806L;
            @Override
            public void run() {
            }
        };
        runnable.setId(100L);
        runnable.setName("测试任务1号");
        runnable.setGroupType(AbstractMonitorRunnable.groupType.TELEMARKETING);
        runnable.setDesc("面对疾风吧");
        symMonitorThreadPool.execute(runnable);
        Thread.sleep(5000);
        symMonitorThreadPool.shutdown();
    }


    /**
     * 执行一个报错的线程
     */
    @Test
    public void testTwo() throws InterruptedException {
        AbstractMonitorRunnable runnable = new AbstractMonitorRunnable(){
            private static final long serialVersionUID = 4769166163856115066L;
            @Override
            public void run() {
                throw new IllegalArgumentException("something wrong");
            }
        };
        runnable.setId(110L);
        runnable.setName("测试任务2号");
        runnable.setGroupType(AbstractMonitorRunnable.groupType.COLLECTION);
        runnable.setDesc("长路漫漫！");
        symMonitorThreadPool.execute(runnable);
        Thread.sleep(5000);
        symMonitorThreadPool.shutdown();
    }


    /**
     * 执行超过线程池最大线程数量的线程数
     */
    @Test
    public void testThree() throws InterruptedException {
        for( int i = 0;i<10;i++ ){
            final int z = i;
            AbstractMonitorRunnable runnable = new AbstractMonitorRunnable() {
                private static final long serialVersionUID = -7982795401212112033L;
                @Override
                public void run() {
                    if( z % 2 == 0) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            runnable.setId(Long.parseLong(z+""));
            runnable.setName("线程任务："+z);
            runnable.setDesc("随风而来，随遇而遇");
            runnable.setGroupType(AbstractMonitorRunnable.groupType.REVIEW);
            symMonitorThreadPool.execute(runnable);
        }
        Thread.sleep(10000);
        symMonitorThreadPool.shutdown();
    }

}
