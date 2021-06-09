package com.sym.multi;

import com.sym.disruptor.*;
import com.sym.disruptor.dsl.ProducerType;
import lombok.Data;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多生产者, 多消费者
 *
 * @author shenyanming
 * Created on 2021/6/9 22:12.
 */
public class Main {
    public static void main(String[] args) {

        // 创建 RingBuffer
        RingBuffer<Events> ringBuffer = RingBuffer.create(
                ProducerType.MULTI,
                Events::new,
                1024,
                new SleepingWaitStrategy());

        // 创建 序列号屏障
        SequenceBarrier barrier = ringBuffer.newBarrier();

        // 准备 消费者
        Consumer[] consumers = new Consumer[5];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("seq" + i);
        }

        // 创建 多消费者工作池
        WorkerPool<Events> workerPool = new WorkerPool<>(
                ringBuffer,
                barrier,
                new DefaultExceptionHandler(),
                consumers);

        // 设置 多个消费者的 sequence 序号用于单独统计消费进度
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 启动 workPool
        ExecutorService workPoolExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        workerPool.start(workPoolExecutor);

        // 多个线程模拟多个生产者
        AtomicLong count = new AtomicLong(10);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        ExecutorService producerExecutor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            producerExecutor.execute(() -> {
                try {
                    // 等待多个生产者创建好
                    cyclicBarrier.await();
                    // 发送消息
                    for (int j = 0; j < 2; j++) {
                        long seq = ringBuffer.next();
                        ringBuffer.get(seq).setEventId(count.getAndIncrement());
                        ringBuffer.publish(seq);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        // 主线程小休一会儿后放行
        com.sym.parallel.Main.sleep(1500);
        cyclicBarrier.reset();

        // 主线程小休一会儿关闭
        com.sym.parallel.Main.sleep(5000);
        System.exit(0);
    }

    /**
     * 自定义事件类型
     */
    @Data
    private static class Events {
        private long eventId;
    }

    /**
     * 消费者, 必须实现{@link com.sym.disruptor.WorkHandler}
     */
    private static class Consumer implements WorkHandler<Events> {

        private static AtomicInteger count = new AtomicInteger(1);

        private String seq;

        Consumer(String seq) {
            this.seq = seq;
        }

        @Override
        public void onEvent(Events event) {
            System.out.println("consumer[" + seq + "], handle=" + event + ", " + count.getAndIncrement());
        }
    }

    private static class DefaultExceptionHandler implements ExceptionHandler<Events> {

        @Override
        public void handleEventException(Throwable ex, long sequence, Events event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }
}
