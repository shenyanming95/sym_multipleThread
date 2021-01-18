package com.sym;

import com.sym.implement.threadpool.future.IFuture;
import com.sym.implement.threadpool.future.util.FutureListeners;
import com.sym.implement.threadpool.future.pool.FutureThreadPool;
import com.sym.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@link Future}扩展类{@link IFuture}的测试类
 *
 * @author shenyanming
 * @date 2020/6/15 22:30.
 */
@Slf4j
public class FutureTest {

    /**
     * 原生的线程池{@link ThreadPoolExecutor}
     */
    private ThreadPoolExecutor origin;

    /**
     * 扩展的线程池{@link FutureThreadPool}
     */
    private FutureThreadPool spread;

    /**
     * 初始化线程池
     */
    @Before
    public void init() {
        origin = ThreadUtil.getThreadPool();
        spread = new FutureThreadPool();
    }

    /**
     * 原生线程池执行异步任务
     */
    @Test
    public void test01() throws InterruptedException, ExecutionException {
        System.out.println("主线程开始运行... ...");
        FutureTask<Integer> result = new FutureTask<>(() -> {
            // 子线程执行逻辑...
            Thread.sleep(2000);
            return 12580;
        });
        //使用线程池执行线程
        origin.submit(result);
        origin.shutdown();
        // 主线程把复杂任务交于子线程执行,继续干自己的事
        System.out.println("主线程干别的事...");
        Thread.sleep(5000);
        // 主线程获取子线程执行结果,如果此时子线程仍未执行完,则主线程会阻塞等待它执行
        System.out.println("获取子线程的返回值 result=" + result.get());
        System.out.println("主线程结束运行... ...");

        ThreadUtil.keepAlive();
    }

    /**
     * 扩展类线程池执行阻塞任务
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException {
        log.info("提交任务");
        Future<Integer> future = spread.submit(() -> {
            Thread.sleep(5000);
            return 123;
        });
        Integer result = future.get();
        log.info("获取结果: {}", result);
    }

    /**
     * 扩展类线程池执行监听器任务：正常执行
     */
    @Test
    public void test03() throws InterruptedException {
        log.info("提交任务");
        IFuture<Integer> future = spread.submit(() -> {
            Thread.sleep(5000);
            return 123;
        });
        // 添加监听器
        future.addListener(FutureListeners.completeListener((result)->{
            log.info("监听器1号, 获取结果：{}", result);
        }));
        future.addListener(FutureListeners.completeListener((result)->{
            log.info("监听器2号, 获取结果：{}", result);
        }));
        // 主线程等待一会
        Thread.sleep(10000);
    }

    /**
     * 扩展类线程池执行监听器任务：异常执行
     */
    @Test
    public void test04() throws InterruptedException {
        log.info("提交任务");
        IFuture<Integer> future = spread.submit(() -> {
            Thread.sleep(5000);
            // 模拟发生异常
            throw new NullPointerException("npt");
        });
        // 添加监听器
        future.addListener(FutureListeners.errorListener((throwable)->{
            log.info("异常处理, 原因：{}", throwable);
        }));
        // 主线程等待一会
        Thread.sleep(10000);
    }
}
