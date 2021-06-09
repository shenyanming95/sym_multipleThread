package com.sym.quickstart;

import com.sym.disruptor.BlockingWaitStrategy;
import com.sym.disruptor.EventFactory;
import com.sym.disruptor.EventHandler;
import com.sym.disruptor.RingBuffer;
import com.sym.disruptor.dsl.Disruptor;
import com.sym.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * disruptor简单示例.
 *
 * @author shenyanming
 * Created on 2021/6/6 10:01.
 */
public class Main {

    public static void main(String[] args) {
        // 1.构建 Disruptor 组件
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        Disruptor<SimpleEvent> disruptor = new Disruptor<>(
                new SimpleEventFactory(),
                1024,
                executor,
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        // 2.设置消费者
        disruptor.handleEventsWith(new SimpleConsumer());

        // 3.启动 disruptor
        disruptor.start();

        // 4.发送消息
        SimpleProducer producer = new SimpleProducer(disruptor.getRingBuffer());
        for (long i = 0; i < 50; i++) {
            producer.sendData(i);
        }

        // 5.关闭资源
        disruptor.shutdown();
        executor.shutdown();
    }


    static class SimpleEvent {
        private long uid;

        @Override
        public String toString() {
            return Thread.currentThread().getName() + ", " + uid;
        }
    }

    static class SimpleEventFactory implements EventFactory<SimpleEvent> {
        @Override
        public SimpleEvent newInstance() {
            return new SimpleEvent();
        }
    }

    static class SimpleConsumer implements EventHandler<SimpleEvent> {

        @Override
        public void onEvent(SimpleEvent event, long sequence, boolean endOfBatch) {
            System.out.println("seq: " + sequence + ", " + event);
        }
    }

    static class SimpleProducer {
        RingBuffer<SimpleEvent> ringBuffer;

        public SimpleProducer(RingBuffer<SimpleEvent> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        void sendData(long data) {
            // 下一个可以用的序列号
            long sequence = ringBuffer.next();
            try {
                // 获取此序列号对应的实例对象(未被初始化)
                SimpleEvent simpleEvent = ringBuffer.get(sequence);
                simpleEvent.uid = data;
            } finally {
                // 投递消息
                ringBuffer.publish(sequence);
            }
        }
    }
}





