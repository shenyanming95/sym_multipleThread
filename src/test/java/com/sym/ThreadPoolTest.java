package com.sym;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试类
 *
 * @author shenym
 * @date 2020/2/26 15:36
 */

public class ThreadPoolTest {

    private ThreadPoolExecutor threadPoolExecutor;

    @Before
    public void init(){
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }


    /**
     * 线程ID是不会改变的
     */
    @Test
    public void threadIdTest() throws IOException {
        // 线程池提交第一个任务
        threadPoolExecutor.execute(()->{
            long threadId = Thread.currentThread().getId();
            System.out.println(threadId);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 线程池提交第二个任务
        threadPoolExecutor.execute(()->{
            System.out.println(Thread.currentThread().getId());
        });

        System.in.read();
    }
}
