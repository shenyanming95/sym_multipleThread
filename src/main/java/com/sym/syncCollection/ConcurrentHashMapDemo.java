package com.sym.syncCollection;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shenym on 2019/10/15.
 */
public class ConcurrentHashMapDemo {

    private static Random random = new Random();

    public static void main(String[] args) throws IOException {
        // 预先为map添加多个数据
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>(256);
        for(int i = 0;i < 100; i++){
            map.put(random.nextInt(10000)+"","");
        }


        // 线程1
        new Thread(()->{
            try {
                for(;;){
                    ConcurrentHashMapDemo.add(map);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 线程2
        new Thread(()->{
            try {
                ConcurrentHashMapDemo.iterator(map);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 主线程等待
        System.in.read();
    }

    /**
     * 一个线程执行这个方法, 一直在遍历Map
     * @param map
     */
    public static void iterator(ConcurrentHashMap<String,String> map) throws InterruptedException {
        Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key+":"+value);
            Thread.sleep(500);
        }
    }


    /**
     * 一个线程执行这个方法, 向Map中添加数据
     */
    public static void add(ConcurrentHashMap<String,String> map) throws InterruptedException {
        map.put(random.nextInt(1000)+"","66");
        Thread.sleep(100);
    }
}
