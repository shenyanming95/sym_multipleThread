package com.sym.threadSharing.diffCode;

/**
 * 用外部类和内部类的原理，将Runnable当做内部类，共享数据放在外部类中，也可以实现共享
 * <p>
 * Created by 沈燕明 on 2018/12/22.
 */
public class ThreadSharingWithOuterClass {

    private int data = 10;

    public void print(String name) {
        while (data > 0) {
            System.out.println(name + "->" + --data);
        }
    }

    /**
     * 内部类线程1
     */
    class ThreadOne implements Runnable {

        @Override
        public void run() {
            print(Thread.currentThread().getName());
        }
    }

    /**
     * 内部类线程2
     */
    class ThreadTwo implements Runnable {

        @Override
        public void run() {
            print(Thread.currentThread().getName());
        }
    }

    /**
     * main()方法测试
     *
     * @param args
     */
    public static void main(String[] args) {
        // 初始化内部类，需要先初始化外部类，然后通过外部类实例对象初始化内部类
        ThreadSharingWithOuterClass thread = new ThreadSharingWithOuterClass();
        new Thread(thread.new ThreadOne()).start();
        new Thread(thread.new ThreadTwo()).start();
    }


}
