package com.sym.other.ownBuffer;

/**
 * @Auther: shenym
 * @Date: 2018-12-06 10:31
 */
public class ConsumerThread implements Runnable {

    // 模拟缓冲区
    private OwnBuffer ownBuffer;

    public ConsumerThread(OwnBuffer ownBuffer){
        this.ownBuffer = ownBuffer;
    }

    @Override
    public void run() {
        while( true ){
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
