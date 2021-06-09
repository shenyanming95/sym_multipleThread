package com.sym.parallel;

import com.sym.parallel.processor.ConsumerProcessor;
import com.sym.parallel.processor.ProducerProcessor;
import com.sym.disruptor.BusySpinWaitStrategy;
import com.sym.disruptor.dsl.Disruptor;
import com.sym.disruptor.dsl.ProducerType;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单消费者并行消费.
 *
 * @author shenyanming
 * Created on 2021/6/8 09:12
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CountDownLatch latch = new CountDownLatch(1);

        Disruptor<Data> disruptor = new Disruptor<>(
                Data::new,
                1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        // 设置消费者, 串行消费 or 并行消费  SERIAL PARALLEL
        new ConsumerProcessor(disruptor, ConsumerProcessor.RunType.SERIAL).run();

        // 投递消息
        executor.execute(new ProducerProcessor(disruptor, latch));

        // start
        disruptor.start();

        // close
        latch.await();
        disruptor.shutdown();
        executor.shutdown();
    }

    @SneakyThrows
    public static void sleep(int millis) {
        Thread.sleep(millis);
    }

    @lombok.Data
    public static class Data {
        private Long id;
        private String name;
    }




}
