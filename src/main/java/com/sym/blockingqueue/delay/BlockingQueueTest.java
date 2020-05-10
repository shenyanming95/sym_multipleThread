package com.sym.blockingqueue.delay;

import com.sym.blockingqueue.delay.entity.DelayMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * {@link java.util.concurrent.DelayQueue}延迟队列
 *
 * @author shenyanming
 * @date 2020/5/10 8:13.
 */
@Slf4j
public class BlockingQueueTest {

    @Test
    public void test01() throws InterruptedException {
        // 创建一个延迟5s的消息体
        DelayMessage delayMessage = new DelayMessage("buy", 2, TimeUnit.SECONDS);

        BlockingQueue<DelayMessage> delayQueue = new DelayQueue<>();
        delayQueue.put(delayMessage);

        log.info("准备获取延迟对象");
        DelayMessage message = delayQueue.take();
        log.info("已获取延迟对象: {}", message);
    }
}
