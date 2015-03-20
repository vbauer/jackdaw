package com.github.vbauer.jackdaw.code;

import com.github.vbauer.jackdaw.code.base.CodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.ProcessorUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author Vladislav Bauer
 */

public final class SourceCodeGenerator {

    private SourceCodeGenerator() {
        throw new UnsupportedOperationException();
    }


    public static boolean generate(final TypeElement element, final String annotationClassName) {
        try {
            final CodeGenerator generator = SourceCodeGeneratorRegistry.find(annotationClassName);
            final Class<? extends CodeGenerator> generatorClass = generator.getClass();
            final String generatorName = generatorClass.getSimpleName();

            ProcessorUtils.message(
                Diagnostic.Kind.NOTE, String.format("Detected %s on %s", generatorName, element)
            );

            generator.generate(new CodeGeneratorContext(element));

            return true;
        } catch (final Exception ex) {
            ProcessorUtils.message(Diagnostic.Kind.ERROR, ExceptionUtils.getMessage(ex));
            return false;
        }

    }

}
