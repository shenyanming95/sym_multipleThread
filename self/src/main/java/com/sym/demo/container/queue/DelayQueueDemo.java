package com.sym.demo.container.queue;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * {@link DelayQueue}延迟队列
 *
 * @author shenyanming
 * @date 2020/5/10 8:13.
 */
@Slf4j
public class DelayQueueDemo {

    @SneakyThrows
    public static void main(String[] args) {
        // 创建一个延迟5s的消息体
        DelayMessage delayMessage = new DelayMessage("buy", 2, TimeUnit.SECONDS);

        BlockingQueue<DelayMessage> delayQueue = new DelayQueue<>();
        delayQueue.put(delayMessage);

        log.info("准备获取延迟对象");
        DelayMessage message = delayQueue.take();
        log.info("已获取延迟对象: {}", message);
    }

    @ToString
    @Data
    static class DelayMessage implements Delayed {
        /**
         * 数据
         */
        private String data;

        /**
         * 这两个参数合并使用, 表示需要延迟多久
         */
        private long time;
        private TimeUnit timeUnit;

        /**
         * 延迟到该时间点
         */
        private long timeUtil;

        public DelayMessage(String data, long time, TimeUnit timeUnit){
            this.data = data;
            this.time = time;
            this.timeUnit = timeUnit;
            this.timeUtil = System.currentTimeMillis() + timeUnit.toMillis(time);
        }

        /**
         * {@link DelayQueue}会来调用此方法, 当此方法返回0或负值时,
         * 表示延迟时间已到, 延迟队列就会将本对象出队
         * @param unit 计量单位
         * @return 大于0的表示延迟时间, 单位毫秒; 小于等于0表示延迟时间已到, 会执行出队操作
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return timeUtil - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            DelayMessage other = (DelayMessage)o;
            // 延迟时间短的排在前面, 延迟时间大的排在后面
            long currentDelayTime = timeUnit.toMillis(time);
            long otherDelayTime = other.timeUnit.toMillis(time);
            return (int)(currentDelayTime - otherDelayTime);
        }
    }
}
