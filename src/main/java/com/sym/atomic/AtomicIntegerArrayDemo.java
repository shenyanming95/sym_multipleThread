package com.sym.atomic;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 原子操作类 AtomicIntegerArray 的一个demo
 *
 * @author 沈燕明
 * @date 2019/5/6 14:50
 */
public class AtomicIntegerArrayDemo {

    // 若以这种方式创建AtomicIntegerArray，它会初始化一个指定size的整型数组，默认值都为0
    private AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    // 若以这种方式创建AtomicIntegerArray，它将当前数组拷贝一份，不会影响到原数组
    private AtomicIntegerArray atomicIntegerArray1 = new AtomicIntegerArray(new int[]{1, 2, 3, 4});

    /**
     * addAndGet(i,delta) -- 将数组中下标为i的值加上delta，返回增加后的值
     */
    @Test
    public void addAndGetTest() throws InterruptedException {
        System.out.println("原数组下标为5的值为：" + atomicIntegerArray.get(5));
        final CountDownLatch latch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                atomicIntegerArray.addAndGet(5, 12);
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println("线程操作后，数组下标为5的值为：" + atomicIntegerArray.get(5));
    }

}
