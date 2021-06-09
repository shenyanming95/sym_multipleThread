package com.sym.parallel.processor;

import com.sym.parallel.Main;
import com.sym.disruptor.EventHandler;
import com.sym.disruptor.WorkHandler;
import com.sym.disruptor.dsl.Disruptor;
import com.sym.disruptor.dsl.EventHandlerGroup;

import java.util.Objects;
import java.util.UUID;

/**
 * @author shenyanming
 * Created on 2021/6/8 09:33
 */
public class ConsumerProcessor implements Runnable {

    private Disruptor<Main.Data> disruptor;
    private RunType type;
    private FirstHandler h1 = new FirstHandler();
    private SecondHandler h2 = new SecondHandler();
    private ThirdHandler h3 = new ThirdHandler();

    public ConsumerProcessor(Disruptor<Main.Data> disruptor, RunType type){
        this.type = Objects.requireNonNull(type);
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        if (RunType.SERIAL == type) {
            // 同步消费模式, 它会返回一个 EventHandlerGroup, 用它关联消费者可以达到串行处理模式.
            EventHandlerGroup<Main.Data> group = disruptor.handleEventsWith(h1);
            group.handleEventsWith(h2).handleEventsWithWorkerPool(h3);
        } else if (RunType.PARALLEL == type) {
            // 异步消费模式
            disruptor.handleEventsWith(h1, h2, h3);
        }
    }

    public static enum RunType{
        SERIAL, PARALLEL
    }

    private static class FirstHandler implements EventHandler<Main.Data> {
        @Override
        public void onEvent(Main.Data event, long sequence, boolean endOfBatch){
            // 第一个handler, 为event设置值
            Main.sleep(1500);
            event.setId(System.currentTimeMillis());
            event.setName(UUID.randomUUID().toString().replaceAll("-", ""));
        }
    }

    private static class SecondHandler implements EventHandler<Main.Data> {
        @Override
        public void onEvent(Main.Data event, long sequence, boolean endOfBatch) {
            System.out.println("data.id：" + event.getId() + ", " + Thread.currentThread().getName());
        }
    }

    /**
     * {@link WorkHandler}作用跟{@link EventHandler}一样, 只不过它不需要处理序列号.
     */
    private static class ThirdHandler implements EventHandler<Main.Data>, WorkHandler<Main.Data> {

        @Override
        public void onEvent(Main.Data event, long sequence, boolean endOfBatch) {
            this.onEvent(event);
        }

        @Override
        public void onEvent(Main.Data event) {
            System.out.println("data.name：" + event.getName() + ", " + Thread.currentThread().getName());
        }
    }
}
