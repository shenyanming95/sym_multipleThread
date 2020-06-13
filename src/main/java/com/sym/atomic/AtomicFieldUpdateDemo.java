package com.sym.atomic;

import com.sym.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * JDK8提供了3个工具类, 用来原子更新某个类的变量, 分别为：
 * {@link AtomicIntegerFieldUpdater}, 对应int类型的变量;
 * {@link AtomicLongFieldUpdater}, 对应long类型的变量;
 * {@link AtomicReferenceFieldUpdater}, 对应任意类型的变量.
 *
 * @author shenyanming
 * @date 2020/2/20 22:28
 */

public class AtomicFieldUpdateDemo {

    private AtomicDemoClass demoClass = new AtomicDemoClass();

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
     * 借助{@link AtomicIntegerFieldUpdater}可以让一个类的一个int类型的变量实现原子更新,
     * 前提是这个变量必须不为private, 且不是static, 且必须为volatile修饰这三个条件一起满足。
     * 同理, {@link java.util.concurrent.atomic.AtomicLongFieldUpdater}具有一样的效果
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
        ThreadUtil.keepAlive();
    }

    /**
     * {@link AtomicReferenceFieldUpdater}实现对引用的原子更新
     */
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
        ThreadUtil.keepAlive();
    }

}
