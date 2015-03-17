package com.github.vbauer.jackdaw.code;

import com.github.vbauer.jackdaw.annotation.JAdapter;
import com.github.vbauer.jackdaw.annotation.JBean;
import com.github.vbauer.jackdaw.annotation.JBuilder;
import com.github.vbauer.jackdaw.annotation.JClassDescriptor;
import com.github.vbauer.jackdaw.annotation.JComparator;
import com.github.vbauer.jackdaw.annotation.JFactoryMethod;
import com.github.vbauer.jackdaw.annotation.JFunction;
import com.github.vbauer.jackdaw.annotation.JMessage;
import com.github.vbauer.jackdaw.annotation.JPredicate;
import com.github.vbauer.jackdaw.annotation.JRepeatable;
import com.github.vbauer.jackdaw.code.base.CodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JAdapterCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JBeanCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JBuilderCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JClassDescriptorCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JComparatorCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JFactoryMethodCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JFunctionCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JMessageCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JPredicateCodeGenerator;
import com.github.vbauer.jackdaw.code.generator.JRepeatableCodeGenerator;
import com.github.vbauer.jackdaw.util.ProcessorUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public final class SourceCodeGenerator {

    private static Map<String, Class<? extends CodeGenerator>> CODE_GENERATORS =
        createCodeWriterMap();


    private SourceCodeGenerator() {
        throw new UnsupportedOperationException();
    }


    public static boolean generate(final TypeElement element, final String annotationClassName) {
        final Class<? extends CodeGenerator> codeGeneratorClass =
            CODE_GENERATORS.get(annotationClassName);

        return generateCode(element, codeGeneratorClass);
    }

    public static Set<String> getSupportedAnnotations() {
        return CODE_GENERATORS.keySet();
    }


    private static boolean generateCode(
        final TypeElement element, final Class<? extends CodeGenerator> codeGeneratorClass
    ) {
        try {
            final Constructor<? extends CodeGenerator> constructor =
                codeGeneratorClass.getConstructor(TypeElement.class);

            final CodeGenerator generator = constructor.newInstance(element);
            generator.generate();

            return true;
        } catch (final Exception ex) {
            ProcessorUtils.message(Diagnostic.Kind.ERROR, ExceptionUtils.getMessage(ex));
            return false;
        }
    }

    private static Map<String, Class<? extends CodeGenerator>> createCodeWriterMap() {
        final Map<String, Class<? extends CodeGenerator>> map = Maps.newLinkedHashMap();

        map.put(JAdapter.class.getName(), JAdapterCodeGenerator.class);
        map.put(JBean.class.getName(), JBeanCodeGenerator.class);
        map.put(JBuilder.class.getName(), JBuilderCodeGenerator.class);
        map.put(JClassDescriptor.class.getName(), JClassDescriptorCodeGenerator.class);
        map.put(JComparator.class.getName(), JComparatorCodeGenerator.class);
        map.put(JFactoryMethod.class.getName(), JFactoryMethodCodeGenerator.class);
        map.put(JFunction.class.getName(), JFunctionCodeGenerator.class);
        map.put(JMessage.class.getName(), JMessageCodeGenerator.class);
        map.put(JPredicate.class.getName(), JPredicateCodeGenerator.class);
        map.put(JRepeatable.class.getName(), JRepeatableCodeGenerator.class);

        return map;
    }

}
