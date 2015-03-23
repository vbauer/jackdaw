package com.github.vbauer.jackdaw.context;

import org.apache.commons.lang3.Validate;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author Vladislav Bauer
 */

public final class ProcessorContextHolder {

    private static final ThreadLocal<ProcessorContext> CONTEXT = new ThreadLocal<ProcessorContext>();


    private ProcessorContextHolder() {
        throw new UnsupportedOperationException();
    }


    public static void withContext(final ProcessorContext processorContext, final Runnable runnable) {
        Validate.notNull(processorContext, "Processor context must be defined");
        CONTEXT.set(processorContext);
        try {
            runnable.run();
        } finally {
            CONTEXT.remove();
        }
    }

    public static ProcessorContext getContext() {
        final ProcessorContext processorContext = CONTEXT.get();
        Validate.notNull(processorContext, "Processor context is not setup");
        return processorContext;
    }

    public static ProcessingEnvironment getProcessingEnvironment() {
        final ProcessorContext processorContext = getContext();
        return processorContext.getProcessingEnvironment();
    }

}
