package com.github.vbauer.jackdaw.context;

import org.apache.commons.lang3.Validate;

/**
 * @author Vladislav Bauer
 */

public final class ProcessorContextHolder {

    private static final ThreadLocal<ProcessorContext> CONTEXT = new ThreadLocal<ProcessorContext>();


    private ProcessorContextHolder() {
        throw new UnsupportedOperationException();
    }


    public static void withContext(final ProcessorContext processorContext, final Runnable runnable) {
        Validate.notNull(processorContext);
        CONTEXT.set(processorContext);
        try {
            runnable.run();
        } finally {
            CONTEXT.remove();
        }
    }

    public static ProcessorContext getContext() {
        final ProcessorContext processorContext = CONTEXT.get();
        Validate.notNull(processorContext);
        return processorContext;
    }

}
