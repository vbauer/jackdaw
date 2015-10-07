package com.github.vbauer.jackdaw.code;

import com.github.vbauer.jackdaw.code.base.CodeGenerator;
import com.github.vbauer.jackdaw.util.ServiceLoaderUtils;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public final class SourceCodeGeneratorRegistry {

    private static final Map<String, ? extends CodeGenerator> CODE_GENERATORS =
        createCodeWriterMap();


    private SourceCodeGeneratorRegistry() {
        throw new UnsupportedOperationException();
    }


    public static Set<String> getSupportedAnnotations() {
        return CODE_GENERATORS.keySet();
    }

    public static CodeGenerator find(final String annotationClassName) {
        return CODE_GENERATORS.get(annotationClassName);
    }


    private static Map<String, ? extends CodeGenerator> createCodeWriterMap() {
        final Map<String, CodeGenerator> map = Maps.newLinkedHashMap();
        final Collection<CodeGenerator> generators = ServiceLoaderUtils.load(CodeGenerator.class);

        for (final CodeGenerator generator : generators) {
            final Class<? extends Annotation> annotation = generator.getAnnotation();
            final String annotationName = annotation.getName();
            map.put(annotationName, generator);
        }

        return map;
    }

}
