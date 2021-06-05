package com.sym.implement.buffer;

/**
 * 传统线程通信, {@link Object#wait()}、{@link Object#notify()}、{@link Object#notifyAll()}.
 * 模拟实现的缓冲区
 *
 * @author ym.shen
 * @date 2018-12-28 16:30
 */
public class TraditionBuffer<T> {

    /**
     * 底层存储数组的默认大小
     */
    private final static int DEFAULT_SIZE = 10;

    /**
     * 定义对象数组来存储数据
     */
    private final Object[] dataArray;

    /**
     * 定义对象存储数组的总量、生产者put时的下标、生产者get时的下标
     */
    private int count, putIndex, getIndex;

    public TraditionBuffer() {
        this(DEFAULT_SIZE);
    }

    public TraditionBuffer(int capacity) {
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
        synchronized (dataArray) {
            // 如果数组已满，则生产者不能再添加数据了
            while (count == dataArray.length) {
                try {
                    // 将生产者线程挂起
                    dataArray.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 如果数组未满，则生产者可以添加新数据
            if (putIndex == dataArray.length) {
                // 将对象数组逻辑上当成循环体，每当putIndex等于数组容量，则从头开始
                putIndex = 0;
            }
            dataArray[putIndex++] = t;
            count++;
            System.out.println(Thread.currentThread().getName() + "-写入：" + t + "-当前容量：" + count);
            // 数据写入以后就要唤醒消费者线程取走数据
            dataArray.notifyAll();
        }
    }


    /**
     * 消费者取走数据
     *
     * @return 数据
     */
    @SuppressWarnings("unchecked")
    public T get() {
        synchronized (dataArray) {
            // 如果数组已空，则消费者不能再取走数据
            while (count == 0) {
                // 将消费者线程挂起
                try {
                    dataArray.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 如果数组未空，则消费者可以取走数据
            if (getIndex == dataArray.length) {
                // 将对象数组逻辑上当成循环体，每当getIndex等于数组容量，则从头开始
                getIndex = 0;
            }
            Object retObj = dataArray[getIndex++];
            count--;
            System.out.println(Thread.currentThread().getName() + "-取走：" + retObj + ",当前容量：" + count);
            // 数据取走之后就要唤醒生产者继续生产
            dataArray.notifyAll();
            return (T)retObj;
        }
    }
}
