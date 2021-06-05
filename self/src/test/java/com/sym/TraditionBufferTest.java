package com.sym;

import com.sym.implement.buffer.TraditionBuffer;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ym.shen
 * Created on 2020/4/16 15:25
 */
public class TraditionBufferTest {

    @Test
    public void test() {
        // 创建一个缓冲区
        TraditionBuffer<String> buffer = new TraditionBuffer<>();

        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(6);

        // 开启3个线程写入,每个线程写入4个数据
        for (int i = 0; i < 3; i++) {
            final int k = i + 1;
            threadPool.execute(() -> {
                Thread.currentThread().setName("生产者" + k);
                int count = 4;
                while (count > 0) {
                    buffer.put("数据" + k);
                    count--;
                }
            });
        }

        // 开启2个线程读取,执行无限次
        for (int i = 3; i < 5; i++) {
            final int j = i + 1;
            threadPool.execute(() -> {
                Thread.currentThread().setName("消费者" + j);
                while (!Thread.currentThread().isInterrupted()) {
                    buffer.get();
                }
            });
        }
    }

}
