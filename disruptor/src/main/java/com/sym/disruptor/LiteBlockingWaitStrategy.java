package com.sym.disruptor;

import com.sym.disruptor.util.ThreadHints;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Variation of the {@link BlockingWaitStrategy} that attempts to elide conditional wake-ups when
 * the lock is uncontended.  Shows performance improvements on microbenchmarks.  However this
 * wait strategy should be considered experimental as I have not full proved the correctness of
 * the lock elision code.
 */
public final class LiteBlockingWaitStrategy implements WaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();
    private final AtomicBoolean signalNeeded = new AtomicBoolean(false);

    @Override
    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier)
            throws AlertException, InterruptedException {
        long availableSequence;
        if (cursorSequence.get() < sequence) {
            lock.lock();

            try {
                do {
                    signalNeeded.getAndSet(true);

                    if (cursorSequence.get() >= sequence) {
                        break;
                    }

                    barrier.checkAlert();
                    processorNotifyCondition.await();
                }
                while (cursorSequence.get() < sequence);
            } finally {
                lock.unlock();
            }
        }

        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
            ThreadHints.onSpinWait();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
        if (signalNeeded.getAndSet(false)) {
            lock.lock();
            try {
                processorNotifyCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public String toString() {
        return "LiteBlockingWaitStrategy{" +
                "processorNotifyCondition=" + processorNotifyCondition +
                '}';
    }
}
