package com.sym.atomic.algorithm.impl;

import com.sym.atomic.algorithm.LoadBalance;
import com.sym.atomic.algorithm.component.ServerInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询的方式执行每一个服务
 *
 * @author shenyanming
 * Created on 2020/6/12 18:23
 */
@Slf4j
public class RoundRobinLoadBalance implements LoadBalance {

    /**
     * 通过一个原子类来并发地选取服务
     */
    private AtomicInteger atomicInteger = new AtomicInteger(-1);

    @Override
    public ServerInfo select(List<ServerInfo> serverList) {
        return serverList.get(getNextIndex(serverList.size()));
    }

    /**
     * 返回轮询的下一个索引下标
     *
     * @return int
     */
    private int getNextIndex(int serverSize) {
        // 听说把成员变量赋值到本地变量, 会更快！！
        AtomicInteger localAtomicInteger = atomicInteger;
        for (; ; ) {
            // 当前索引
            int curValue = localAtomicInteger.get();
            // 下一个索引
            int nextValue = (curValue + 1) % serverSize;
            // 通过CAS设置成功的才需要修改
            if (localAtomicInteger.compareAndSet(curValue, nextValue)) {
                return nextValue;
            }
            // 如果CAS设置失败的, 就回到循环, 重新再修改
        }
    }
}
