package com.sym.customBuffer.condition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: shenym
 * @Date: 2018-12-28 16:01
 */
public class ConditionBufferTest {

    public static void main(String[] args){

        // 创建一个缓冲区
        ConditionBuffer buffer = new ConditionBuffer();

        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(6);

        // 开启4个线程写入,每个线程写入5个数据
        for(int i=0;i<4;i++){
            final int j= i+1 ;
            threadPool.execute(()->{
                Thread.currentThread().setName("生产者"+j);
                int count = 5;
                while(count>0){
                    buffer.put("数据"+j);
                    count--;
                }
            });
        }

        // 开启2个线程读取,执行无限次
        for(int i=4;i<6;i++){
            final int j= i+1 ;
            threadPool.execute(()->{
                Thread.currentThread().setName("消费者"+j);
                while(true){
                    buffer.get();
                }
            });
        }

        threadPool.shutdown();

    }
}
