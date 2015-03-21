package com.github.vbauer.jackdaw.context;

import com.github.vbauer.jackdaw.code.SourceCodeGeneratorRegistry;
import com.github.vbauer.jackdaw.code.base.CodeGenerator;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public class ProcessorSourceContext {

    private final String annotationClassName;
    private final List<Pair<TypeElement, String>> elementInfo;


    public ProcessorSourceContext(
            final String annotationClassName,
            final List<Pair<TypeElement, String>> elementsInfo
    ) {
        this.annotationClassName = annotationClassName;
        this.elementInfo = elementsInfo;
    }


    public String getAnnotationClassName() {
        return annotationClassName;
    }

    public List<Pair<TypeElement, String>> getElementInfo() {
        return elementInfo;
    }


    public static TypeElement guessOriginElement(
        final Collection<ProcessorSourceContext> contexts, final String className
    ) {
        for (final ProcessorSourceContext context : contexts) {
            final List<Pair<TypeElement, String>> elementsInfo = context.getElementInfo();
            for (final Pair<TypeElement, String> elementInfo : elementsInfo) {
                final String classNameInfo = elementInfo.getRight();
                if (StringUtils.equals(classNameInfo, className)) {
                    return elementInfo.getLeft();
                }
            }
        }
        return null;
    }

    public static Collection<ProcessorSourceContext> calculate(
        final RoundEnvironment roundEnv, final Collection<? extends TypeElement> annotations
    ) {
        final List<ProcessorSourceContext> contexts = Lists.newArrayList();
        for (final TypeElement annotation : annotations) {
            contexts.add(calculate(roundEnv, annotation));
        }
        return contexts;
    }


    private static ProcessorSourceContext calculate(
        final RoundEnvironment roundEnv, final TypeElement annotation
    ) {
        final String annotationName = annotation.getQualifiedName().toString();
        final Set<? extends Element> allElements = roundEnv.getElementsAnnotatedWith(annotation);
        final Set<TypeElement> elements = TypeUtils.foldToTypeElements(allElements);

        final CodeGenerator generator = SourceCodeGeneratorRegistry.find(annotationName);
        final List<Pair<TypeElement, String>> classes = Lists.newArrayList();

        for (final TypeElement element : elements) {
            final String className = getClassName(generator, element);
            classes.add(Pair.of(element, className));
        }

        return new ProcessorSourceContext(annotationName, classes);
    }

    private static String getClassName(final CodeGenerator generator, final TypeElement element) {
        if (generator instanceof GeneratedCodeGenerator) {
            final GeneratedCodeGenerator gcGenerator = (GeneratedCodeGenerator) generator;
            final Function<String, String> nameModifier = gcGenerator.getNameModifier();
            final String originClassName = TypeUtils.getName(element);
            return nameModifier.apply(originClassName);
        }
        return null;
    }

}
