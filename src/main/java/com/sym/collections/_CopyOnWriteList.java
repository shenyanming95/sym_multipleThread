package com.sym.collections;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * {@link java.util.concurrent.CopyOnWriteArrayList}的并发使用
 * <p>
 *
 * @author shenym
 * @date 2020/1/19
 */
public class _CopyOnWriteList {

    public static void main(String[] args) throws InterruptedException {
        // 普通ArrayList集合. 多线程环境下扩容时, 其它线程来写入, 容易出现ArrayIndexOutOfBoundsException异常
        //List<Integer> arrayList = new ArrayList<>();
        //resolveList(arrayList);

        // 并发CopyOnWriteArrayList集合
        List<Integer> cowList = new CopyOnWriteArrayList<>();
        resolveList(cowList);
    }


    /**
     * 多线程处理集合
     */
    private static void resolveList(List<Integer> list) throws InterruptedException {
        // 获取线程池
        ThreadPoolExecutor threadPool = getThreadPool();

        // 开启5个线程并发写
        for (int i = 0; i < 5; i++) {
            int index = i;
            threadPool.execute(() -> {
                Thread.currentThread().setName("write-thread-" + index);
                // 每个线程写入10个数
                int start = index * 10;
                int end = start + 10;
                for (; start < end; start++) {
                    list.add(start);
                }
            });
        }

        // 防止写线程还未写入, 读线程就已经开始
        Thread.sleep(500);

        // 开启2个线程并发读
        for (int i = 0; i < 2; i++) {
            int index = i;
            threadPool.execute(() -> {
                Thread.currentThread().setName("read-thread-" + index);
                int start = index * 5;
                // 每个线程读出5个数
                int[] array = new int[5];
                for (int j = 0; j < 5; j++) {
                    array[j] = list.get(start);
                    start++;
                }
                System.out.println(Thread.currentThread().getName() + "：" + Arrays.toString(array));
            });
        }

        // 关闭线程池
        Thread.sleep(1000);
        threadPool.shutdown();
    }

    /**
     * 自定义线程池
     */
    private static ThreadPoolExecutor getThreadPool() {
        return new ThreadPoolExecutor(5, 10, 1, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(2),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
