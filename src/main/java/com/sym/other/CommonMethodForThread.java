package com.sym.other;

/**
 * Thread类中的常用方法
 *
 * Created by 沈燕明 on 2019/2/16.
 */
public class CommonMethodForThread {

    public static void main(String[] args) {
        //CommonMethodForThread.joinDemo();
        //CommonMethodForThread.interruptDemo();
        CommonMethodForThread.setPriorityDemo();
    }

    /**
     * join()会让当前线程挂起，给指定线程执行，等执行线程执行完，当前线程再重新申请CPU资源执行
     */
    public static void joinDemo(){
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
    }

    /**
     * interrupt()可以强制中断线程，它实际是让线程的中断标志为true，
     * 线程仍然处于存活状态，仍在继续运行中
     */
    public static void interruptDemo(){
        // 开启一个子线程让其休眠5s，在期间中断它
        Thread t1 = new Thread(()->{
            try {
                System.out.println("线程休眠5s...");
                Thread.sleep(5000);
                System.out.println("线程休眠完毕...");
            } catch (InterruptedException e) {
                System.out.println("线程被中断了");
            }
        });
        t1.start();
        // 主线程休眠5s后，中断t1线程
        try {
            Thread.sleep(2000);
            t1.interrupt();
            System.out.println("线程是否存活?=="+t1.isAlive());
            Thread.interrupted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * setPriority()可以设置线程的优先级，反映线程的重要程度或紧张程度
     * 但是优先级与线程执行顺序没有绝对联系，只不过优先级高的线程抢到cpu资源的概率较大而已
     */
    public static void setPriorityDemo(){
        Thread t1 = new Thread(()->{
            System.out.println("t1线程");
        });
        Thread t2 = new Thread(()->{
            System.out.println("t2线程");
        });
        // 默认的
        Thread t3 = new Thread(()->{
            System.out.println("t3线程");
        });
        Thread t4 = new Thread(()->{
            System.out.println("t4线程");
        });
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t3.setPriority(Thread.NORM_PRIORITY);
        t4.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }


}
