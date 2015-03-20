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
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public final class SourceCodeGeneratorRegistry {

    private static Map<String, ? extends CodeGenerator> CODE_GENERATORS = createCodeWriterMap();


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

        map.put(JAdapter.class.getName(), new JAdapterCodeGenerator());
        map.put(JBean.class.getName(), new JBeanCodeGenerator());
        map.put(JBuilder.class.getName(), new JBuilderCodeGenerator());
        map.put(JClassDescriptor.class.getName(), new JClassDescriptorCodeGenerator());
        map.put(JComparator.class.getName(), new JComparatorCodeGenerator());
        map.put(JFactoryMethod.class.getName(), new JFactoryMethodCodeGenerator());
        map.put(JFunction.class.getName(), new JFunctionCodeGenerator());
        map.put(JMessage.class.getName(), new JMessageCodeGenerator());
        map.put(JPredicate.class.getName(), new JPredicateCodeGenerator());
        map.put(JRepeatable.class.getName(), new JRepeatableCodeGenerator());

        return map;
    }

}
