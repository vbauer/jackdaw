package com.github.vbauer.jackdaw.context;

import com.github.vbauer.jackdaw.util.MessageUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author Vladislav Bauer
 */

public final class ProcessorContextHolder {

    private static final InheritableThreadLocal<ProcessorContext> CONTEXT =
        new InheritableThreadLocal<ProcessorContext>();


    private ProcessorContextHolder() {
        throw new UnsupportedOperationException();
    }


    public static void withContext(final ProcessorContext context, final Runnable runnable) {
        Validate.notNull(context, "Processor context must be defined");
        Validate.notNull(runnable, "Operation must be defined");

        CONTEXT.set(context);
        try {
            runnable.run();
        } catch (final Exception ex) {
            MessageUtils.error(ExceptionUtils.getMessage(ex));
        }  finally {
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
