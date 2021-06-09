package com.sym.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * No operation version of a {@link EventProcessor} that simply tracks a {@link Sequence}.
 * <p>
 * This is useful in tests or for pre-filling a {@link RingBuffer} from a publisher.
 */
public final class NoOpEventProcessor implements EventProcessor {
    private final SequencerFollowingSequence sequence;
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Construct a {@link EventProcessor} that simply tracks a {@link Sequence} object.
     *
     * @param sequencer to track.
     */
    public NoOpEventProcessor(final RingBuffer<?> sequencer) {
        sequence = new SequencerFollowingSequence(sequencer);
    }

    @Override
    public Sequence getSequence() {
        return sequence;
    }

    @Override
    public void halt() {
        running.set(false);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Thread is already running");
        }
    }

    /**
     * Sequence that follows (by wrapping) another sequence
     */
    private static final class SequencerFollowingSequence extends Sequence {
        private final RingBuffer<?> sequencer;

        private SequencerFollowingSequence(final RingBuffer<?> sequencer) {
            super(Sequencer.INITIAL_CURSOR_VALUE);
            this.sequencer = sequencer;
        }

        @Override
        public long get() {
            return sequencer.getCursor();
        }
    }
}
