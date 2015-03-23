package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JMessage;
import com.github.vbauer.jackdaw.code.base.BaseCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.DateTimeUtils;
import com.github.vbauer.jackdaw.util.MessageUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.text.Format;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Vladislav Bauer
 */

public class JMessageCodeGenerator extends BaseCodeGenerator {

    private static final Collection<Format> DATE_FORMATS =
        DateTimeUtils.createDateFormats(DateTimeUtils.DATE_FORMATS);


    @Override
    public Class<? extends Annotation> getAnnotation() {
        return JMessage.class;
    }

    @Override
    public void generate(final CodeGeneratorContext context) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
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
            final String showAfter = annotation.showAfter();

            if (showMessage(showAfter)) {
                final Diagnostic.Kind type = annotation.type();
                final String[] messages = annotation.value();
                final boolean details = annotation.details();
                final Element elem = details ? element : null;

                for (final String message : messages) {
                    MessageUtils.message(type, message, elem);
                }
            }
        }
    }

    private boolean showMessage(final String dateString) {
        final Date date = DateTimeUtils.parseDate(dateString, DATE_FORMATS);
        return date == null || date.after(new Date());
    }

}
