package com.github.vbauer.jackdaw.util;

import com.github.vbauer.jackdaw.context.ProcessorContextHolder;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * @author Vladislav Bauer
 */

public final class MessageUtils {

    private MessageUtils() {
        throw new UnsupportedOperationException();
    }


    public static void note(final String message) {
        message(Diagnostic.Kind.NOTE, message);
    }

    public static void error(final String message) {
        message(Diagnostic.Kind.ERROR, message);
    }

    public static void message(final Diagnostic.Kind type, final String message) {
        final Messager messager = getMessager();
        messager.printMessage(type, message);
    }

    public static void message(
        final Diagnostic.Kind type, final String message, final Element element
    ) {
        if (element != null) {
            final Messager messager = getMessager();
            messager.printMessage(type, message, element);
        } else {
            message(type, message);
        }
    }


    private static Messager getMessager() {
        final ProcessingEnvironment env = ProcessorContextHolder.getProcessingEnvironment();
        return env.getMessager();
    }

}
