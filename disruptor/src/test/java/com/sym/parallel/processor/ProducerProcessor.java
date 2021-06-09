package com.sym.parallel.processor;

import com.sym.parallel.Main;
import com.sym.disruptor.EventTranslator;
import com.sym.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenyanming
 * Created on 2021/6/8 09:22
 */
@AllArgsConstructor
public class ProducerProcessor implements Runnable {

    private Disruptor<Main.Data> disruptor;
    private CountDownLatch latch;

    @Override
    public void run() {
        DataTranslator translator = new DataTranslator();
        for (int i = 0; i < 1; i++) {
            disruptor.publishEvent(translator);
        }
        latch.countDown();
    }


    private static class DataTranslator implements EventTranslator<Main.Data> {
        @Override
        public void translateTo(Main.Data event, long sequence) {
            // 可以在这里初始化event
        }
    }
}
