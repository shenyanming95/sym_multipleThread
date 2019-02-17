package com.sym.atomic;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 沈燕明 on 2018/12/22.
 */
public class AtomicIntegerDemo {

    private AtomicInteger integer = new AtomicInteger(10);

    private void print(){
        String name = Thread.currentThread().getName();
        while( integer.get() > 0 ){
            System.out.println(name+"->"+integer.getAndDecrement());
        }
    }

    public void printTest(){
        for( int i=1;i<4;i++ ){
            new Thread(()-> {
                this.print();
            }).start();
        }
    }

    private void cas(int i){
        int val = integer.get();
        if( integer.compareAndSet(val,i) ){
            System.out.println(Thread.currentThread().getName()+"成功修改值为："+integer.get());
        }
    }

    public void casTest(){
        CountDownLatch latch = new CountDownLatch(3);
        for( int i=1;i<4;i++ ){
            new Thread(()-> {
                this.cas(new Random().nextInt(1000));
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("最终值："+integer.get());
    }
    
    public static void main(String[] args) {
        AtomicIntegerDemo demo = new AtomicIntegerDemo();
        //demo.printTest();
        demo.casTest();
    }
}
