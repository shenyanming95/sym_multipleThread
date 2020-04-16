package com.sym.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author ym.shen
 * @date 2018/12/31
 */
public class _ArrayBlockingQueue {

    /**
     * 创建指定容量的阻塞队列,因为它是数组实现的,所以必须指定容量
     */
    private ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(5);

    /**
     * 生产者线程往阻塞队列中添加数据
     *
     * @param obj 数据
     */
    public void put(Object obj) {
        String name = Thread.currentThread().getName();
        try {
            blockingQueue.put(obj);
            System.out.println(name + "添加：" + obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费者线程往阻塞队列中取走数据
     */
    public void take() {
        String name = Thread.currentThread().getName();
        try {
            Object obj = blockingQueue.take();
            System.out.println(name + "取走：" + obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
