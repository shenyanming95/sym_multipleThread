package com.sym.disruptor;

public interface BatchStartAware {
    void onBatchStart(long batchSize);
}
