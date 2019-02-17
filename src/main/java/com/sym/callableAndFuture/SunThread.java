package com.sym.callableAndFuture;

import java.util.concurrent.Callable;

/**
 * 默认一个需要进行复杂运算的过程，主线程将这个复杂运算交于子线程执行，
 * 开启此子线程来执行复杂的运算，让主线程可以继续干别的事
 */
public class SunThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程开始运行复杂程序");
        //模拟正在进行复杂的运算
        Thread.sleep(1000);
        System.out.println("子线程结束运行复杂程序");
        return 12580;
    }
}
