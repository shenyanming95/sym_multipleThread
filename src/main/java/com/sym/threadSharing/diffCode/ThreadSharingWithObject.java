package com.sym.threadSharing.diffCode;
/**
 * 多个线程操作外部对象，来实现线程共享数据
 *
 * Created by 沈燕明 on 2018/12/22.
 */
public class ThreadSharingWithObject {

    public static void main(String[] args) {
        // 线程都操作 sharingData 实例对象
        SharingData sharingData = new SharingData(1,"测试");

        new Thread(()->{
            sharingData.setName("线程1");
            System.out.println(sharingData);
        },"线程1").start();

        new Thread(()->{
            sharingData.setName("线程2");
            System.out.println(sharingData);
        },"线程2").start();

    }

}
