package com.sym.commonMethod;

/**
 * 线程优先级
 *
 * Created by 沈燕明 on 2019/2/17.
 */
public class PriorityDemo {
    
    public static void main(String[] args) {
        PriorityDemo.setPriorityTest();
    }

    /**
     * setPriority()可以设置线程的优先级，反映线程的重要程度或紧张程度
     * 但是优先级与线程执行顺序没有绝对联系，只不过优先级高的线程抢到cpu资源的概率较大而已
     */
    private static void setPriorityTest(){
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
