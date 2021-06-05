package com.sym.disruptor.dsl;

import com.sym.disruptor.Sequence;
import com.sym.disruptor.SequenceBarrier;

import java.util.concurrent.Executor;

interface ConsumerInfo
{
    Sequence[] getSequences();

    SequenceBarrier getBarrier();

    boolean isEndOfChain();

    void start(Executor executor);

    void halt();

    void markAsUsedInBarrier();

    boolean isRunning();
}
