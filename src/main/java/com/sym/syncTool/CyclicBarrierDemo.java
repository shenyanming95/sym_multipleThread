package com.sym.syncTool;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier，同步屏障，它设定一个屏障，仅当指定的线程一起到达这个屏障，才可以继续地往下执行，否则一直等待；
 * 或者其中等待的线程已经超时了，又或者其中等待的线程已经被中断了，都会让屏障失效，所有阻塞线程被唤醒重新执行。
 *
 * 假设：有3个人，先到达集合点，然后一起出发去终点，最后一起干活
 */
public class CyclicBarrierDemo {

    // 实例化一个等待数为3的屏障，如果只有2个线程在运行，则会永远地等待下去
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    /**
     * 设置两个屏障点，让3个线程都到达屏障点，才可以继续执行；同时，验证了CyclicBarrier是可重复使用的
     */
    public void goAndWork(){
        try{
            String name = Thread.currentThread().getName();
            // 模拟每个人到达的时间不一样
            Thread.sleep(new Random().nextInt(3000));
            System.out.println(name+"已到达..准备下一个集合点");
            // 第一个屏障点：线程执行到这边阻塞，直至3个线程全部到达
            cyclicBarrier.await();
            // 模拟每个人到达的时间不一样
            Thread.sleep(new Random().nextInt(3000));
            System.out.println(name+"已到达..准备到终点");
            // 第二个屏障点
            cyclicBarrier.await();
            // System.out.println(name+"说：干活啦");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        // 开启三个执行
        for( int i=0;i<3;i++ ){
            final int j = i+1;
            threadPool.execute(()->{
                Thread.currentThread().setName("线程"+j);
                demo.goAndWork();
            });
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()){ }
        System.out.println("人到齐了...开始干活");
    }

}
