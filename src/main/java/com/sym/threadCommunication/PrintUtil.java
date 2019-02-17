package com.sym.threadCommunication;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一个好的编程习惯，不要把代码逻辑直接定义到线程方法内，独立到外部来实现，线程方法内只是调用
 *
 * @Auther: shenym
 * @Date: 2018-12-20 19:29
 */
public class PrintUtil {

    // 加上volatile保证变量的内存可见性
    private volatile AtomicBoolean flag = new AtomicBoolean(true);

    /**
     * 用于给主线程打印的方法
     */
    public void mainPrint() {
        synchronized (flag) {
            // 建议在while循环内判断wait()条件，保证线程运行前后都可以检查运行条件
            while (flag.get() == false) {
                try {
                    // 当flag为false，条件不满足，主线程挂起等待
                    flag.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 当条件满足时，此时flag为true,主线程执行自己的逻辑
            for (int i = 1; i <= 5; i++) {
                System.out.println("主线程执行.." + i + "次");
            }
            // 执行完毕后，改变共享变量的值，唤醒子线程，建议使用notifyAll()来唤醒
            flag.set(false);
            flag.notifyAll();
        }
    }


    /**
     * 用于给子线程打印的方法
     */
    public void threadPrint() {
        synchronized (flag) {
            // 建议在while循环内判断wait()条件，保证线程运行前后都可以检查运行条件
            while (flag.get() == true) {
                try {
                    // 当flag为true时，子线程挂起等待
                    flag.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 当条件满足时，此时flag为false,子线程执行自己的逻辑
            for (int i = 1; i <= 7; i++) {
                System.out.println("子线程执行.." + i + "次");
            }
            // 子线程执行完自己的逻辑，改变共享变量的值，唤醒主线程。推荐使用notifyAll()来唤醒
            flag.set(true);
            flag.notifyAll();
        }
    }
}
