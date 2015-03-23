package com.github.vbauer.jackdaw.code;

import com.github.vbauer.jackdaw.code.base.CodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.MessageUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

/**
 * @author Vladislav Bauer
 */

public final class SourceCodeGenerator {

    private SourceCodeGenerator() {
        throw new UnsupportedOperationException();
    }


    public static boolean generate(
        final String annotationClassName, final Collection<TypeElement> elements
    ) {
        try {
            final CodeGenerator generator = SourceCodeGeneratorRegistry.find(annotationClassName);
            generator.onStart();

            for (final TypeElement element : elements) {
                generate(generator, element);
            }
            generator.onFinish();
            return true;
        } catch (final Exception ex) {
            MessageUtils.error(ExceptionUtils.getMessage(ex));
            return false;
        }

    }


    private static void generate(
        final CodeGenerator generator, final TypeElement element
    ) throws Exception {
        final Class<? extends CodeGenerator> generatorClass = generator.getClass();
        final String generatorName = generatorClass.getSimpleName();

        MessageUtils.note(String.format("Detected %s on %s", generatorName, element));
        generator.generate(CodeGeneratorContext.create(element));
    }

}
