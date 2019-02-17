package com.sym.syncTool;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchange类可以用于两个线程间交换数据，两个线程先后到达交换点，先到达的线程会等待后到达的线程，然后两个线程互相交换数据，
 * 交换后双方持对方的数据，继续执行。
 * <p>
 * 此例子模仿：市场买卖，卖家出售衣服，买家花钱购买
 */
public class ExchangerDemo {

    // 初始化一个交换器
    private Exchanger<Object> exchanger = new Exchanger<>();

    // 随机数
    private Random random = new Random();

    // 买家线程调用的方法
    public void buy(int money) {
        try {
            String name = Thread.currentThread().getName();
            Thread.sleep(random.nextInt(3000));
            System.out.println(name + "已到达交易点...手持RMB：" + money);
            // 先执行的线程在此阻塞，等待伙伴线程的到来，一旦伙伴线程到来，交换数据然后各自继续执行
            Object clothes = exchanger.exchange(money);
            System.out.println(name + "买到衣服喽：" + clothes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 卖家线程调用的方法

    /**
     *
     * @param clothes
     */
    public void sell(String clothes) {
        try {
            String name = Thread.currentThread().getName();
            Thread.sleep(random.nextInt(3000));
            System.out.println(name + "已到达交易点...待售衣服：" + clothes);
            // 先执行的线程在此阻塞，等待伙伴线程的到来，一旦伙伴线程到来，交换数据然后各自继续执行
            Object money = exchanger.exchange(clothes);
            System.out.println(name + "赚到RMB：" + money);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ExchangerDemo demo = new ExchangerDemo();
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        // 开启一个子线程作为卖家线程
        threadPool.execute(()->{
            Thread.currentThread().setName("淘宝");
            demo.sell("春冬季时尚优衣库羽绒服");
        });
        // 主线程作为买家线程
        Thread.currentThread().setName("小明");
        demo.buy(1150);
        // 关闭线程池
        threadPool.shutdown();
    }

}
