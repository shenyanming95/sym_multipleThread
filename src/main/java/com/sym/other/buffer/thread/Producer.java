package com.sym.other.buffer.thread;

import com.sym.other.buffer.OwnBuffer;

/**
 * 生产者
 * @author ym.shen
 * Created on 2020/4/16 11:57
 */
public class Producer implements Runnable{

    /**
     * 缓冲区
     */
    private final OwnBuffer ownBuffer;

    public Producer(OwnBuffer ownBuffer){
        this.ownBuffer = ownBuffer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            synchronized (ownBuffer){
                while ( ownBuffer.getData().size() == 10 ){
                    try {
                        ownBuffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ownBuffer.notifyAll();
                ownBuffer.put("1");
                System.out.println("生产者开始生产,缓冲区大小="+ownBuffer.getData().size());
            }
        }
    }
}
