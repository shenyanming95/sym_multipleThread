package com.sym.atomic.algorithm;

import com.sym.atomic.algorithm.component.ServerInfo;

import java.util.List;

/**
 * 模拟负载均衡接口
 *
 * @author shenyanming
 * Created on 2020/6/12 18:20
 */
public interface LoadBalance {

    /**
     * 通过负载均衡策略选择一个服务
     *
     * @param serverList 所有可用服务列表
     * @return 最终要执行的服务
     */
    ServerInfo select(List<ServerInfo> serverList);

}
