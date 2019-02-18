package com.sym;

import java.util.concurrent.locks.LockSupport;

/**
 * @Auther: shenym
 * @Date: 2019-02-18 11:33
 */
public class ThreadTest {


    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(()->{
            System.out.println("线程t1开始阻塞...");
            LockSupport.park();
            System.out.println("线程t1结束阻塞");
        });

        ThreadTest.park(t1);
        Thread.sleep(2000);
        ThreadTest.unPark(t1);
    }

    private static void park(Thread t1){
        t1.start();
    }

    private static void unPark(Thread t1){
        LockSupport.unpark(t1);
    }
}
