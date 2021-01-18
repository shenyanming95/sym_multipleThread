package com.sym.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程的一些常用工具
 *
 * @author ym.shen
 * Created on 2020/4/16 11:46
 */
@Slf4j
public class ThreadUtil {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * 让程序保持运行, 在多线程环境下防止程序退出
     */
    public static void keepAlive() {
        keepAlive(null);
    }

    /**
     * 让程序保持运行, 在多线程环境下防止程序退出
     *
     * @param callbackWhenExist 钩子函数, 在程序退出时回调, 一般用来处理资源
     */
    public static void keepAlive(Runnable callbackWhenExist) {
        if (null != callbackWhenExist) {
            Runtime.getRuntime().addShutdownHook(new Thread(callbackWhenExist));
        }
        // log.debug("键入其它值以退出 ~ ：");
        scanner.nextLine();
    }

    /**
     * 创建一个默认的线程池
     *
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getThreadPool() {
        return new ThreadPoolExecutor(
                10,
                20,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(64),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
