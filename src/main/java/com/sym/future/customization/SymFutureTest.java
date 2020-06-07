package com.sym.future.customization;

import com.sym.future.customization.component.SymFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author shenyanming
 * @date 2020/6/7 17:14.
 */
@Slf4j
public class SymFutureTest {

    /**
     * 阻塞测试
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        log.info("提交任务");
        SymFuture<Integer> future = SymFutureExecutors.submit(() -> {
            Thread.sleep(5000);
            return 123;
        });
        Integer result = future.get();
        log.info("获取结果: {}", result);
    }


    /**
     * 监听器测试
     */
    @Test
    public void test02() throws InterruptedException {
        log.info("提交任务");
        SymFuture<Integer> future = SymFutureExecutors.submit(() -> {
            Thread.sleep(5000);
            return 123;
        });
        // 添加监听器
        future.addListener((result)->{
            log.info("监听器1号, 获取结果：{}", result);
        });
        future.addListener((result)->{
            log.info("监听器2号, 获取结果：{}", result);
        });

        // 主线程等待
        Thread.sleep(10000);
    }

}
