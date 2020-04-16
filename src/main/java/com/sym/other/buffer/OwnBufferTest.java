package com.sym.other.buffer;

import com.sym.other.buffer.thread.Consumer;
import com.sym.other.buffer.thread.Producer;
import org.junit.Test;

/**
 * @author ym.shen
 * Created on 2020/4/16 12:01
 */
public class OwnBufferTest {

    @Test
    public void test(){
        OwnBuffer buffer = new OwnBuffer();
        buffer.put("1");
        buffer.put("2");
        buffer.put("3");
        buffer.put("4");
        buffer.put("5");

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
