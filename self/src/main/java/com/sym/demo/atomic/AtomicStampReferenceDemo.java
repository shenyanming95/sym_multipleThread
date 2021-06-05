package com.sym.demo.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题现象与解决
 *
 * @author shenym
 * @date 2020/3/4 19:50
 */

public class AtomicStampReferenceDemo {

    public static void main(String[] args) throws InterruptedException {
        ABATest();
        ABA0Test();
    }

    /**
     * 未加版本号, 可能会发生ABA问题
     */
    private static AtomicInteger atomicInteger = new AtomicInteger(10);

    /**
     * 增加版本号, 解决ABA问题
     */
    private static int initialStamp = 1;
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(10, initialStamp);

    /**
     * ABA问题
     */
    public static void ABATest() throws InterruptedException {
        new Thread(() -> {
            // 先将值更新为12
            atomicInteger.compareAndSet(10, 12);
            System.out.println("线程A, 更新值：" + atomicInteger.get());
            // 再重新将值更新为10
            atomicInteger.compareAndSet(12, 10);
            System.out.println("线程A, 更新值：" + atomicInteger.get());
        }).start();

        new Thread(() -> {
            try {
                // 让线程A先执行
                TimeUnit.SECONDS.sleep(1);
                // CAS更新
                boolean b = atomicInteger.compareAndSet(10, 666);
                System.out.println("线程B更新结果：" + b + ", 更新值：" + atomicInteger.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(5000);
    }


    /**
     * 解决ABA问题
     */
    public static void ABA0Test() throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 先由10更新为20
            int stamp = atomicStampedReference.getStamp();
            atomicStampedReference.compareAndSet(10, 20, stamp, stamp + 1);
            System.out.println("线程A, 更新值：" + atomicStampedReference.getReference() + ", 版本号：" + atomicStampedReference.getStamp());
            // 再由20更新为10
            int stamp0 = atomicStampedReference.getStamp();
            atomicStampedReference.compareAndSet(20, 10, stamp0, stamp0 + 1);
            System.out.println("线程A, 更新值：" + atomicStampedReference.getReference() + ", 版本号：" + atomicStampedReference.getStamp());
        });
        thread.start();

        new Thread(() -> {
            try {
                // 先让线程A执行
                thread.join();
                // 然后再更新, 由10更新为66. 除非携带版本号一起更新, 不然此次更新一定失败
                boolean b = atomicStampedReference.compareAndSet(10, 66, initialStamp, initialStamp + 1);
                System.out.println("线程B更新结果：" + b + ", 更新值：" + atomicStampedReference.getReference() + ", 版本号：" + atomicStampedReference.getStamp());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(5000);
    }
}
