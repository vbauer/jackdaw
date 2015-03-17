package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JMessage;
import com.github.vbauer.jackdaw.code.base.CodeGenerator;
import com.github.vbauer.jackdaw.util.ProcessorUtils;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.List;

/**
 * @author Vladislav Bauer
 */

public class JMessageCodeGenerator extends CodeGenerator {

    private static final String SEPARATOR = ": ";


    public JMessageCodeGenerator(final TypeElement typeElement) {
        super(typeElement);
    }

    @Override
    public void generate() throws Exception {
        printInfoIfNecessary(typeElement);
    }


    private <T extends Element> void printInfoIfNecessary(final T rootElement) {
        printElementInfoIfNecessary(rootElement);

        if (rootElement instanceof ExecutableElement) {
            final ExecutableElement executableElement = (ExecutableElement) rootElement;
            printInfoIfNecessary(executableElement.getParameters());
        }

        final List<? extends Element> elements = rootElement.getEnclosedElements();
        printInfoIfNecessary(elements);
    }

    private <T extends Element> void printInfoIfNecessary(final List<T> elements) {
        for (final T element : elements) {
            printInfoIfNecessary(element);
        }
    }

    private void printElementInfoIfNecessary(final Element element) {
        final JMessage annotation = element.getAnnotation(JMessage.class);

        if (annotation != null) {
            final String[] messages = annotation.value();
            for (final String message : messages) {
                printInfo(element, annotation, message);
            }
        }
    }

    private void printInfo(
        final Element element, final JMessage annotation, final String message
    ) {
        final Diagnostic.Kind type = annotation.type();
        final boolean details = annotation.details();
        final String detailsInfo = details ? (element.toString() + SEPARATOR) : StringUtils.EMPTY;

        ProcessorUtils.message(type, detailsInfo + message);
    }

}
