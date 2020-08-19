package com.sym.communication.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JUC下的线程通信, {@link Condition}
 * 模拟实现的缓冲区
 *
 * @author shenyanming
 * @date 2018-12-28 14:20
 */
public class ConditionBuffer<T> {

    /**
     * 底层存储数组的默认大小
     */
    private final static int DEFAULT_SIZE = 10;

    /**
     * 定义对象数组来存储数据
     */
    private Object[] dataArray;

    /**
     * 定义对象存储数组的总量、生产者put时的下标、生产者get时的下标
     */
    private int count, putIndex, getIndex;

    /**
     * 把Lock定义为成员变量,这样创建实例时,每个缓冲区各自拥有自己的锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 消费者线程的条件变量
     */
    private Condition consumerCondition = lock.newCondition();

    /**
     * 生产者线程的条件变量
     */
    private Condition producerCondition = lock.newCondition();

    public ConditionBuffer() {
        this(DEFAULT_SIZE);
    }

    public ConditionBuffer(int capacity) {
        dataArray = new Object[capacity];
        count = 0;
        putIndex = 0;
        getIndex = 0;
    }

    /**
     * 生产者添加新数据
     *
     * @param t 数据
     */
    public void put(T t) {
        lock.lock();
        try {
            // 判断底层对象数组是否已满,注意是逻辑上的判断哦
            while (count == dataArray.length) {
                try {
                    // 如果数组已满,,则生产者不再生产数据,让生产者线程挂起
                    producerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 如果数组未满,那么生产者可以安心地put数据
            if (putIndex == dataArray.length) {
                // 因为这是一个循环数组,当putIndex等于数组长度时,说明此时数组数据已被取走，只不过仍保留在数组而已
                putIndex = 0;
            }
            dataArray[putIndex++] = t;
            count++;
            System.out.println(Thread.currentThread().getName() + "-写入：" + t+"-当前容量："+count);
            // 数据存放完以后,唤醒消费者线程取走数据
            consumerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 消费者取走数据
     *
     * @return 数据
     */
    @SuppressWarnings("unchecked")
    public T get() {
        lock.lock();
        try {

            // 判断底层对象数组是否已空,注意是逻辑上的判断哦
            while (count == 0) {
                try {
                    // 如果数组已空,则消费者不能再取走数据,让消费者线程挂起
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 如果数组未空,那么消费者可以安心地get数据
            if (getIndex == dataArray.length) {
                // 因为这是一个循环数组,当getIndex等于数组长度时,说明此时数组数据已被取走，只不过仍保留在数组而已
                getIndex = 0;
            }
            Object retObj = dataArray[getIndex++];
            count--;
            System.out.println(Thread.currentThread().getName() + "-取走：" + retObj+",当前容量："+count);
            // 数组取走以后,就可以唤醒生产者继续生产
            producerCondition.signalAll();
            return (T)retObj;
        } finally {
            lock.unlock();
        }
    }


}
