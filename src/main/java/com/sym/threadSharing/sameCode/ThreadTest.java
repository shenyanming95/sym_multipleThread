package com.sym.threadSharing.sameCode;

/**
 * 测试多线程共享数据
 * <p>
 * Created by 沈燕明 on 2018/12/22.
 */
public class ThreadTest {

    public static void main(String[] args) {

        // 只初始化一个Runnable实现类
        Runnable runnable = new DataRunnable(10);

        // 开启3个线程操作
        for (int i = 1; i < 4; i++) {
            new Thread(runnable,"线程"+i).start();
        }

    }
}
