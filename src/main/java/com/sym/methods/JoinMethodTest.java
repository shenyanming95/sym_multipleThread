package com.sym.methods;

import com.sym.util.ThreadUtil;
import org.junit.Test;

/**
 * Thread.join()方法的学习
 *
 * @author ym.shen
 * Created on 2020/4/16 11:49
 */
public class JoinMethodTest {

    /**
     * join()会让当前线程挂起，给指定线程执行，
     * 等指定线程执行完，当前线程会重新申请CPU资源执行
     */
    @Test
    public void runTest(){
        Thread t1 = new Thread(()->{
            for(int i=1;i<6;i++){
                System.out.println("线程1执行");
            }
        });
        Thread t2 = new Thread(()->{
            try {
                // t2线程会挂起，让t1线程先执行，直至t1线程执行完
                t1.join();
                for(int i=1;i<6;i++){
                    System.out.println("线程2执行");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        ThreadUtil.sync();
    }

}
