package com.sym.disruptor;

/**
 * Called by the {@link RingBuffer} to pre-populate all the events to fill the RingBuffer.
 *
 * @param <T> event implementation storing the data for sharing during exchange or parallel coordination of an event.
 */
public interface EventFactory<T> {
    /*
     * Implementations should instantiate an event object, with all memory already allocated where possible.
     */
    T newInstance();
}