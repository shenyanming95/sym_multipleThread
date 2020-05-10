package com.sym.blockingqueue.delay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列{@link java.util.concurrent.DelayQueue}要求元素必须实现
 * {@link Delayed}接口, 用来实现排序和获取延迟时间.
 *
 * @author shenyanming
 * @date 2020/5/10 8:16.
 */
@ToString
@Slf4j
@Data
public class DelayMessage implements Delayed {

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
     * {@link java.util.concurrent.DelayQueue}会来调用此方法, 当此方法返回0或负值时,
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
