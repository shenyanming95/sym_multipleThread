package com.sym.atomic.algorithm.impl;

import com.sym.atomic.algorithm.LoadBalance;
import com.sym.atomic.algorithm.component.ServerInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每个服务执行指定次数, 换下一个服务, 依次类推
 *
 * @author shenyanming
 * Created on 2020/6/13 13:37
 */
public class RoundSpecifiedTimesLoadBalance implements LoadBalance {

    public RoundSpecifiedTimesLoadBalance(int maxTimes){
        atomicIndex = new AtomicInteger(0);
        atomicCount = new AtomicInteger(0);
        this.maxTimes = maxTimes;
    }

    private AtomicInteger atomicIndex;
    private AtomicInteger atomicCount;
    private int maxTimes;


    @Override
    public ServerInfo select(List<ServerInfo> serverList) {
        return serverList.get(getNextIndex(serverList.size()));
    }

    /**
     *  获取下一个服务列表的索引index
     * @param size 总的服务数量
     * @return 下一个服务
     */
    private int getNextIndex(int size){
        for(;;){
            // 当前索引
            int index = atomicIndex.get();
            // 当前计数量
            int count = atomicCount.get();

            if(count >= maxTimes){
                // 已达到最大调用次数, 将计数置为1(这边不能重置为0, 因为这边已经递增了索引并调用了一次), 返回下一个索引
                int nextIndex = (index + 1) % size;
                if(atomicCount.compareAndSet(count, 1)
                        && atomicIndex.compareAndSet(index, nextIndex)){
                    return nextIndex;
                }
            }else{
                // 未达到最大调用次数, 将计数加1, 返回当前索引
                if(atomicCount.compareAndSet(count, count + 1)){
                    return index;
                }
            }
        }
    }
}
