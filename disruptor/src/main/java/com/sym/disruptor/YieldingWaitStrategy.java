package com.sym.disruptor;


/**
 * Yielding strategy that uses a Thread.yield() for {@link EventProcessor}s waiting on a barrier
 * after an initially spinning.
 * <p>
 * This strategy will use 100% CPU, but will more readily give up the CPU than a busy spin strategy if other threads
 * require CPU resource.
 */
public final class YieldingWaitStrategy implements WaitStrategy {
    private static final int SPIN_TRIES = 100;

    @Override
    public long waitFor(
            final long sequence, Sequence cursor, final Sequence dependentSequence, final SequenceBarrier barrier)
            throws AlertException, InterruptedException {
        long availableSequence;
        int counter = SPIN_TRIES;

        while ((availableSequence = dependentSequence.get()) < sequence) {
            counter = applyWaitMethod(barrier, counter);
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
    }

    private int applyWaitMethod(final SequenceBarrier barrier, int counter)
            throws AlertException {
        barrier.checkAlert();

        if (0 == counter) {
            Thread.yield();
        } else {
            --counter;
        }

        return counter;
    }
}
