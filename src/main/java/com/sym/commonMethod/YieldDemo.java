package com.sym.commonMethod;

/**
 * Thread.yield()方法的学习
 *
 * Created by 沈燕明 on 2019/2/17.
 */
public class YieldDemo {
    
    public static void main(String[] args) {
        YieldDemo.runTest();
    }

    /**
     * yield()让当前线程让出cpu资源，从运行状态转换成就绪状态。
     * 让同优先级的其它线程，同时包括当前线程自己，抢夺cpu资源而执行。
     * 所以yield()方法有可能没有任何效果，当前线程让出cpu资源又重新获取到cpu调度的机会
     */
    private static void runTest(){
        // 创建线程t1并启动
        Thread t1 = new Thread(()->{
            for( int i=0;i<7;i++ ){
                // 当i=5的时候，t1让出cpu资源，然后和t2一起抢夺cpu调度
                // 谁抢到谁就可以执行
                if( i == 5 ){
                    Thread.yield();
                }
                System.out.println("线程t1执行-"+i);
            }
        });
        t1.start();

        // 创建线程t2并启动
        Thread t2 = new Thread(()->{
            for( int i=0;i<7;i++ ){
                System.out.println("线程t2执行-"+i);
            }
        });
        t2.start();
    }
}
