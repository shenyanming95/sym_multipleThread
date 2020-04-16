package com.sym.atomic;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 沈燕明 on 2018/12/22.
 */
public class AtomicIntegerDemo {

    /**
     * 默认初始值为10
     */
    private AtomicInteger integer = new AtomicInteger(10);

    /**
     * getAndIncrement() -- 将当前值加1，并返回之前的值
     * getAndDecrement() -- 将当前值减1，并返回之前的值
     */
    @Test
    public void getAndDecrementTest() {
        // 开启3个线程
        for (int i = 1; i < 4; i++) {
            new Thread(() -> {
                String name = Thread.currentThread().getName();
                while (integer.get() > 0) {
                    System.out.println(name + "->" + integer.getAndDecrement());
                }
            }).start();
        }
    }

    /**
     * getAndAdd() -- 将当前值与参数相加，并返回添加前的值
     * addAndGet() -- 将当前值与参数相加，并返回添加后的值
     */
    @Test
    public void addAndGetTest() {
        for (int i = 1; i < 4; i++) {
            new Thread(() -> {
                String name = Thread.currentThread().getName();
                int result = integer.getAndAdd(10);
                System.out.println(name + "：->" + result);
            }).start();
        }
    }


    /**
     * compareAndSet()：CAS方式修改，只会尝试一次，修改成功返回true，反之返回false
     */
    @Test
    public void casTest() {
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 1; i < 4; i++) {
            new Thread(() -> {
                int z = new Random().nextInt(1000);
                int val = integer.get();
                if (integer.compareAndSet(val, z)) {
                    System.out.println(Thread.currentThread().getName() + "成功修改值为：" + integer.get());
                }
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("最终值：" + integer.get());
    }

}
