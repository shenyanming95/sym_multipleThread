package com.sym.disruptor.dsl;

import com.sym.disruptor.EventHandler;
import com.sym.disruptor.EventProcessor;
import com.sym.disruptor.Sequence;
import com.sym.disruptor.SequenceBarrier;

import java.util.concurrent.Executor;

/**
 * <p>Wrapper class to tie together a particular event processing stage</p>
 * <p>
 * <p>Tracks the event processor instance, the event handler instance, and sequence barrier which the stage is attached to.</p>
 *
 * @param <T> the type of the configured {@link EventHandler}
 */
class EventProcessorInfo<T> implements ConsumerInfo {
    private final EventProcessor eventprocessor;
    private final EventHandler<? super T> handler;
    private final SequenceBarrier barrier;
    private boolean endOfChain = true;

    EventProcessorInfo(
            final EventProcessor eventprocessor, final EventHandler<? super T> handler, final SequenceBarrier barrier) {
        this.eventprocessor = eventprocessor;
        this.handler = handler;
        this.barrier = barrier;
    }

    public EventProcessor getEventProcessor() {
        return eventprocessor;
    }

    @Override
    public Sequence[] getSequences() {
        return new Sequence[]{eventprocessor.getSequence()};
    }

    public EventHandler<? super T> getHandler() {
        return handler;
    }

    @Override
    public SequenceBarrier getBarrier() {
        return barrier;
    }

    @Override
    public boolean isEndOfChain() {
        return endOfChain;
    }

    @Override
    public void start(final Executor executor) {
        executor.execute(eventprocessor);
    }

    @Override
    public void halt() {
        eventprocessor.halt();
    }

    /**
     *
     */
    @Override
    public void markAsUsedInBarrier() {
        endOfChain = false;
    }

    @Override
    public boolean isRunning() {
        return eventprocessor.isRunning();
    }
}
