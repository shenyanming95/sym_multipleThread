package com.sym.other.ownBuffer;

/**
 * @Auther: shenym
 * @Date: 2018-12-06 11:16
 */
public class ThreadTest {
    public static void main(String[] args) {
        OwnBuffer buffer = new OwnBuffer();
        buffer.put("1");
        buffer.put("1");
        buffer.put("1");
        buffer.put("1");
        buffer.put("1");

        ProducerThread pt = new ProducerThread(buffer);
        ConsumerThread ct = new ConsumerThread(buffer);
        new Thread(pt).start();
        new Thread(ct).start();


    }
}
