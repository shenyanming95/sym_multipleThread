package com.sym.api.thread;

import com.sym.util.ThreadUtil;
import org.junit.Test;

/**
 * {@link Thread#join()}, 当前线程挂起, 让出CPU资源.
 * 给指定的线程执行完
 *
 * @author shenyanming
 * Created on 2020/8/19 11:10
 */
public class ThreadJoinApiTest {

    @Test
    public void runTest() {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i < 6; i++) {
                System.out.println("线程1执行");
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                // t2线程会挂起，让t1线程先执行，直至t1线程执行完
                t1.join();
                for (int i = 1; i < 6; i++) {
                    System.out.println("线程2执行");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        ThreadUtil.keepAlive();
    }

}
