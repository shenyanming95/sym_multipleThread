package com.sym;

import com.sym.implement.loadbalance.LoadBalance;
import com.sym.implement.loadbalance.component.ServerInfo;
import com.sym.implement.loadbalance.impl.RoundRobinLoadBalance;
import com.sym.implement.loadbalance.impl.RoundSpecifiedTimesLoadBalance;
import com.sym.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shenyanming
 * Created on 2020/6/13 11:10
 */
@Slf4j
public class LoadBalanceTest {

    private ThreadPoolExecutor poolExecutor;

    @Before
    public void init(){
        poolExecutor = ThreadUtil.getThreadPool();
    }

    /**
     * 轮询测试
     */
    @Test
    public void test01(){
        // 假设当前服务有3个, 10个线程请求, 那么[0]出现4次, [1],[2]各出现3次
        List<ServerInfo> serverInfos = ServerInfo.initList(3);
        // 创建负载均衡
        LoadBalance loadBalance = new RoundRobinLoadBalance();

        // 多线程环境下验证
        for(int i = 0; i < 10; i++){
            poolExecutor.execute(()->{
                ServerInfo serverInfo = loadBalance.select(serverInfos);
                log.info("{} - {}", Thread.currentThread().getName(), serverInfo.getId());
            });
        }
        ThreadUtil.keepAlive();
    }

    /**
     * 指定最大调用次数测试
     */
    @Test
    public void test02(){
        // 假设当前服务有2个, 10个线程请求, 最大允许调用2次. 则[0]调用6次, [1]调用1次
        List<ServerInfo> serverInfos = ServerInfo.initList(2);
        // 创建负载均衡
        LoadBalance loadBalance = new RoundSpecifiedTimesLoadBalance(2);

        // 多线程环境下验证
        for(int i = 0; i < 10; i++){
            poolExecutor.execute(()->{
                ServerInfo serverInfo = loadBalance.select(serverInfos);
                log.info("{} - {}", Thread.currentThread().getName(), serverInfo.getId());
            });
        }
        ThreadUtil.keepAlive();
    }
}
