package com.sym.threadCommunication;
/**
 * 主线程打印5次，然后子线程打印7次，以此规律执行10次。
 * 主要是学习传统线程间通信：wait()、notify()、notifyAll()
 *
 * @Auther: shenym
 * @Date: 2018-12-20 19:26
 */
public class ThreadConnection {

    /**
     * main方法代表主线程，好的编程习惯，不要在线程方法内执行逻辑，把代码逻辑放到外部，线程内部方法(run方法)
     * 只是简单调用即可
     *
     * @param args
     */
    public static void main(String[] args) {

        // 只创建一个 PrintUtil 对象，主线程和子线程共用一把锁
        PrintUtil util = new PrintUtil();

        // 使用lambda表达式创建开启一个子线程，子线程打印10次
        new Thread(() -> {
            // 子线程打印10次
            for( int j=0;j<10;j++ ){
                util.threadPrint();
            }
        }).start();

        // 主线程打印10次
        for( int i=0;i<10;i++ ){
            util.mainPrint();
        }



    }

}
