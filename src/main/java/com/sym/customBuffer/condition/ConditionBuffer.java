package com.sym.customBuffer.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock+Condition实现的缓冲区
 *
 * @Auther: shenym
 * @Date: 2018-12-28 14:20
 */
public class ConditionBuffer {

    // 底层存储数组的默认大小
    private final static int DEFAULTSIZE = 10;

    // 定义对象数组来存储数据
    private Object[] dataArray;

    // 定义对象存储数组的总量、生产者put时的下标、生产者get时的下标
    private int count, putIndex, getIndex;

    // 把Lock定义为成员变量,这样创建实例时,每个缓冲区各自拥有自己的锁
    private Lock lock = new ReentrantLock();

    // 消费者线程的条件变量
    private Condition consumerCondition = lock.newCondition();

    // 生产者线程的条件变量
    private Condition producerCondition = lock.newCondition();

    public ConditionBuffer() {
        this(DEFAULTSIZE);
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
     * @param obj
     */
    public void put(Object obj) {
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
            dataArray[putIndex++] = obj;
            count++;
            System.out.println(Thread.currentThread().getName() + "-写入：" + obj+"-当前容量："+count);
            // 数据存放完以后,唤醒消费者线程取走数据
            consumerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 消费者取走数据
     *
     * @return
     */
    public Object get() {
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
            return retObj;
        } finally {
            lock.unlock();
        }
    }


}
