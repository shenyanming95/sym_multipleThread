package com.sym.threadSharing.sameCode;

import lombok.NoArgsConstructor;

/**
 * 如果线程之间执行逻辑都一样，可以创建 Runnable 接口实现类，将共享数据定义在实现类中，
 * 然后用这个接口实现类创建线程。这样每个线程操作的数据就是同一个对象
 *
 * Created by 沈燕明 on 2018/12/22.
 */
@NoArgsConstructor
public class DataRunnable implements Runnable {

    // 共享数据
    private int data;

    @Override
    public void run() {
        doSomething();
    }

    public void doSomething(){
        while( data>0 ){
            System.out.println(Thread.currentThread().getName()+",取走数据,剩余："+ --data);
        }
    }

    public DataRunnable(int data){
        this.data = data;
    }
}
