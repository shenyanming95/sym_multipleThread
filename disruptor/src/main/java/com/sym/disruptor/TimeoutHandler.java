package com.sym.disruptor;

public interface TimeoutHandler
{
    void onTimeout(long sequence) throws Exception;
}
