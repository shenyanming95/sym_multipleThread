package com.sym.container.map;

import com.sym.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ConcurrentHashMap}
 *
 * @author shenyanming
 * Created on 2020/8/19 10:52
 */
@Slf4j
public class ConcurrentHashMapDemo {

    public static void main(String[] args) throws IOException {
        // 预先为map添加多个数据
        Map<String, String> map = new ConcurrentHashMap<>(16);
        for (int i = 0; i < 100; i++) {
            map.put(random.nextInt(10000) + "", "");
        }

        // 线程1
        new Thread(() -> {
            try {
                for (; ; ) {
                    ConcurrentHashMapDemo.add(map);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 线程2
        new Thread(() -> {
            try {
                ConcurrentHashMapDemo.iterator(map);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        ThreadUtil.keepAlive();
    }


    private static Random random = new Random();

    /**
     * 一个线程执行这个方法, 一直在遍历Map
     */
    private static void iterator(Map<String, String> map) throws InterruptedException {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            log.info("key:{}, value:{}", key, value);
            Thread.sleep(500);
        }
    }

    /**
     * 一个线程执行这个方法, 向Map中添加数据
     */
    private static void add(Map<String, String> map) throws InterruptedException {
        map.put(random.nextInt(1000) + "", "66");
        Thread.sleep(100);
    }
}
