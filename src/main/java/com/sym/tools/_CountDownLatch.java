package com.sym.tools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch是通过一个计数器来实现的，构造函数需要指定计数器的初始值,每执行一次countDown()方法, 计数器的值就会减1.
 * 当计数器值到达0时, 它表示所有的线程已经完成了任务, 然后在await()方法上等待的线程就可以恢复执行.
 * </p>
 * 用CountDownLatch模拟这样一种情况：有2个运动员在等待裁判的口令起跑, 等跑到终点后告诉裁判结果, 然后裁判公布结果.
 * 可以使用2个CountDownLatch, 初始值分别为1和2, 当裁判发出起跑信号, 为1的计数器减1, 运动员起跑; 每当一个运动员到终点, 为2的计数器减1,
 * 当2个运动员一起跑到终点，裁判才可以公布成绩
 *
 * @author ym.shen
 * Created on 2020/4/16 15:31
 */
public class _CountDownLatch {

    /**
     * 这个计数器值为1，是给运动员用的，运动员听到裁判的起跑命令后才开始比赛
     */
    private CountDownLatch countForAthlete = new CountDownLatch(1);

    /**
     * 这个计数器值为2，是给裁判用的，当2个运动员到达终点后，裁判才可以统计结果
     */
    private CountDownLatch countForReferee = new CountDownLatch(2);

    /**
     * 运动员起跑
     */
    public void sportsman() {
        String name = Thread.currentThread().getName();
        System.out.println(name + "已准备...等待裁判号令");
        try {
            // 运动员线程在此阻塞，以等待裁判的号令
            countForAthlete.await();
            // 如果代码往下执行,说明裁判已经发号施令了
            System.out.println(name + "起跑...");
            // 模拟耗时
            Thread.sleep(new Random().nextInt(3000));
            System.out.println(name + "已到达终点");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 告诉裁判线程，当前线程已到达终点，将计数器减一
            countForReferee.countDown();
        }
    }

    /**
     * 裁判发出起跑命令并且统计结果
     */
    public void judge() {
        String name = Thread.currentThread().getName();
        try {
            // 用来给运动员准备的时间
            Thread.sleep(3000);
            try {
                System.out.println(name + "一声令下：起跑！");
            } finally {
                // 将计数器减一,让运动员线程继续执行
                countForAthlete.countDown();
            }
            // 裁判线程就在此阻塞，等待运动员到达终点，统计结果
            countForReferee.await();
            System.out.println("所有运动员已到达终点..统计结果");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        _CountDownLatch countDownLatch = new _CountDownLatch();
        // 主线程作为裁判线程
        Thread.currentThread().setName("裁判");
        // 开启两个线程作为运动员线程
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 1; i < 3; i++) {
            final String name = "运动员" + i + "号";
            threadPool.execute(() -> {
                Thread.currentThread().setName(name);
                countDownLatch.sportsman();
            });
        }
        // 主线程，即裁判线程执行，执行start()方法
        countDownLatch.judge();
        // 关闭线程池
        threadPool.shutdown();
    }

}
