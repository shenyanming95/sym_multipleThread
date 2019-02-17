package com.sym.other.ownBuffer;

/**
 * @Auther: shenym
 * @Date: 2018-12-06 11:08
 */
public class ProducerThread implements Runnable {

    private OwnBuffer ownBuffer;

    public ProducerThread(OwnBuffer ownBuffer){
        this.ownBuffer = ownBuffer;
    }


    @Override
    public void run() {
        while (true){
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
