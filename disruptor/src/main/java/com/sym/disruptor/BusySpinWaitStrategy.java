package com.sym.disruptor;


import com.sym.disruptor.util.ThreadHints;

/**
 * Busy Spin strategy that uses a busy spin loop for {@link EventProcessor}s waiting on a barrier.
 * <p>
 * This strategy will use CPU resource to avoid syscalls which can introduce latency jitter.  It is best
 * used when threads can be bound to specific CPU cores.
 */
public final class BusySpinWaitStrategy implements WaitStrategy {
    @Override
    public long waitFor(
            final long sequence, Sequence cursor, final Sequence dependentSequence, final SequenceBarrier barrier)
            throws AlertException, InterruptedException {
        long availableSequence;

        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
            ThreadHints.onSpinWait();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
    }
}
