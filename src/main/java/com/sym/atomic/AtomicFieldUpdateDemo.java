package com.sym.atomic;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author shenym
 * @date 2020/2/20 22:28
 */

public class AtomicFieldUpdateDemo {

    private AtomicDemoClass demoClass = new AtomicDemoClass();

    /**
     * 借助{@link AtomicIntegerFieldUpdater}可以让一个类的一个int类型的变量实现原子更新,
     * 前提是这个变量必须不为private, 且不是static, 且必须为volatile修饰这三个条件一起满足。
     * 同理, 还会有：
     * {@link java.util.concurrent.atomic.AtomicLongFieldUpdater}
     */
    @Test
    public void atomicIntegerFieldUpdaterTest() {
        AtomicIntegerFieldUpdater<AtomicDemoClass> fieldUpdater = AtomicIntegerFieldUpdater.
                newUpdater(AtomicDemoClass.class, "ftnCount");
        // 开启10个线程调用
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            new Thread(() -> {
                for (; ; ) {
                    int oldValue = fieldUpdater.get(demoClass);
                    int newValue = oldValue + index;
                    if (fieldUpdater.compareAndSet(demoClass, oldValue, newValue)) {
                        System.out.println(newValue);
                        break;
                    }
                }
            }).start();
        }
        // 主线程在此阻塞
        sync();
    }


    @Test
    public void atomicReferenceFieldUpdater() {
        AtomicReferenceFieldUpdater<AtomicDemoClass, String> fieldUpdater = AtomicReferenceFieldUpdater.
                newUpdater(AtomicDemoClass.class, String.class, "message");
        // 开启10个线程调用
        for (int i = 1; i <= 10; i++) {
            String index = i + "";
            new Thread(() -> {
                for (; ; ) {
                    String oldValue = fieldUpdater.get(demoClass);
                    String newValue = oldValue + " " + index;
                    if (fieldUpdater.compareAndSet(demoClass, oldValue, newValue)) {
                        System.out.println(newValue);
                        break;
                    }
                }
            }).start();
        }
        // 主线程在此阻塞
        sync();
    }

    static class AtomicDemoClass {
        /*
         * 使用 AtomicIntegerFieldUpdater 加强此属性, 必须被volatile修饰且不为private和static类型
         */
        volatile int ftnCount;

        /*
         * 使用 AtomicReferenceFieldUpdater 加强此属性, 必须被volatile修饰且不为private和static类型
         */
        volatile String message = "并发";
    }

    /**
     * 同步阻塞
     */
    private void sync() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
