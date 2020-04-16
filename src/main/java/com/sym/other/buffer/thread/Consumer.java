package com.sym.other.buffer.thread;

import com.sym.other.buffer.OwnBuffer;

/**
 * 消费者
 * @author ym.shen
 * Created on 2020/4/16 11:57
 */
public class Consumer implements Runnable{

    /**
     * 缓冲区
     */
    private final OwnBuffer ownBuffer;

    public Consumer(OwnBuffer ownBuffer){
        this.ownBuffer = ownBuffer;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            synchronized (ownBuffer){
                while( ownBuffer.getData().size() == 0 ){
                    try {
                        ownBuffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ownBuffer.notifyAll();
                ownBuffer.get("1");
                System.out.println("消费者取数据，缓冲区大小="+ownBuffer.getData().size());
            }
        }
    }
}
