package com.sym.demo.threadpool;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * {@link ForkJoinPool}、{@link ForkJoinTask}
 *
 * @author shenyanming
 * Created on 2020/8/19 17:57
 */
@Slf4j
public class ForkJoinThreadPool {

    public static void main(String[] args) throws InterruptedException {
        // JDK的fork/join框架, 主要逻辑是放在对任务的定义, 任务定义好以后,
        // 直接使用ForkJoinPool来执行
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);

        // 准备100个数据
        List<Long> list = new ArrayList<>(100);
        for(long i = 0; i < 100; i++){
            list.add(i);
        }

        // 并行计算
        long start = System.nanoTime();
        Long value = forkJoinPool.invoke(new MergeListForkJoinTask(list));
        long end = System.nanoTime();
        log.info("并行计算, 结果为:{}, 耗时:{} ms", value, (end - start)/1000000);

        // 串行计算
        start = System.nanoTime();
        long sum = 0;
        for (Long aLong : list) {
            // 模拟复杂的计算
            Thread.sleep(50);
            sum += aLong;
        }
        end = System.nanoTime();
        log.info("串行计算, 结果为:{}, 耗时:{} ms", sum, (end - start)/1000000);
        forkJoinPool.shutdown();
    }


    /**
     * fork/join的思想实现, 需要在{@link ForkJoinTask}来完成, 一般使用它的两个抽象子类
     * {@link RecursiveTask}, 有返回值; {@link RecursiveAction}, 无返回值.
     * fork就是将大任务划分为小任务, 在哪里划分呢? 答案就是在{@link RecursiveTask#compute()}中,
     * 根据一定规则, 比如本例, 一个大的List, 划分为size为100的小List进行计算.
     */
    private static class MergeListForkJoinTask extends RecursiveTask<Long> {
        /**
         * 巨大无比的List
         */
        private List<Long> originList;

        /**
         * 每个子List最多计算10个
         */
        private static int threshold = 10;

        public MergeListForkJoinTask(List<Long> list) {
            originList = list;
        }

        /**
         * 定义计算逻辑
         *
         * @return 总的计算结果
         */
        @SneakyThrows
        @Override
        protected Long compute() {
            long sum = 0;
            if (originList.size() <= threshold) {
                // 量级太小, 直接计算
                for (Long l : originList) {
                    // 模拟复杂的计算
                    Thread.sleep(50);
                    sum += l;
                }
                return sum;
            }
            // 量级太大, 截取多个子List出来, 给其它线程计算, 即fork操作
            List<List<Long>> subList = Lists.partition(originList, threshold);
            List<MergeListForkJoinTask> taskList = Lists.newArrayList();
            subList.forEach(list -> taskList.add(new MergeListForkJoinTask(list)));

            // 将任务分给其它线程完成
            invokeAll(taskList);

            // 汇聚结果
            for (MergeListForkJoinTask task : taskList) {
                sum += task.join();
            }
            return sum;
        }
    }
}
