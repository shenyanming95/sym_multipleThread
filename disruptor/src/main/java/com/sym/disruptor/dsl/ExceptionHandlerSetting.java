package com.sym.disruptor.dsl;

import com.sym.disruptor.BatchEventProcessor;
import com.sym.disruptor.EventHandler;
import com.sym.disruptor.EventProcessor;
import com.sym.disruptor.ExceptionHandler;

/**
 * A support class used as part of setting an exception handler for a specific event handler.
 * For example:
 * <pre><code>disruptorWizard.handleExceptionsIn(eventHandler).with(exceptionHandler);</code></pre>
 *
 * @param <T> the type of event being handled.
 */
public class ExceptionHandlerSetting<T> {
    private final EventHandler<T> eventHandler;
    private final ConsumerRepository<T> consumerRepository;

    ExceptionHandlerSetting(
            final EventHandler<T> eventHandler,
            final ConsumerRepository<T> consumerRepository) {
        this.eventHandler = eventHandler;
        this.consumerRepository = consumerRepository;
    }

    /**
     * Specify the {@link ExceptionHandler} to use with the event handler.
     *
     * @param exceptionHandler the exception handler to use.
     */
    @SuppressWarnings("unchecked")
    public void with(ExceptionHandler<? super T> exceptionHandler) {
        final EventProcessor eventProcessor = consumerRepository.getEventProcessorFor(eventHandler);
        if (eventProcessor instanceof BatchEventProcessor) {
            ((BatchEventProcessor<T>) eventProcessor).setExceptionHandler(exceptionHandler);
            consumerRepository.getBarrierFor(eventHandler).alert();
        } else {
            throw new RuntimeException(
                    "EventProcessor: " + eventProcessor + " is not a BatchEventProcessor " +
                            "and does not support exception handlers");
        }
    }
}
