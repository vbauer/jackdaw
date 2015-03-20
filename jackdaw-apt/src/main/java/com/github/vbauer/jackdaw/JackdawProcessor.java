package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.code.SourceCodeGenerator;
import com.github.vbauer.jackdaw.code.SourceCodeGeneratorRegistry;
import com.github.vbauer.jackdaw.context.ProcessorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextFactory;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.google.common.collect.Maps;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Map;
import java.util.Set;


/**
 * @author Vladislav Bauer
 */

@SupportedOptions({
    JackdawProcessor.ADD_SUPPRESS_WARNINGS_ANNOTATION,
    JackdawProcessor.ADD_GENERATED_ANNOTATION,
    JackdawProcessor.ADD_GENERATED_DATE
})
public class JackdawProcessor extends AbstractProcessor {

    public static final String ADD_SUPPRESS_WARNINGS_ANNOTATION = "addSuppressWarningsAnnotation";
    public static final String ADD_GENERATED_ANNOTATION = "addGeneratedAnnotation";
    public static final String ADD_GENERATED_DATE = "addGeneratedDate";


    private ProcessorContext processorContext;


    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        processorContext = ProcessorContextFactory.create(processingEnv);
    }

    @Override
    public boolean process(
        final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv
    ) {
        final boolean needToProcess =
            !(roundEnv.processingOver() || annotations.isEmpty());

        if (needToProcess) {
            final Map<String, Set<TypeElement>> classMap =
                calculateClassMap(annotations, roundEnv);

            for (final Map.Entry<String, Set<TypeElement>> entry : classMap.entrySet()) {
                final String annotationName = entry.getKey();
                final Set<TypeElement> elements = entry.getValue();

                process(annotationName, elements);
            }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SourceCodeGeneratorRegistry.getSupportedAnnotations();
    }


    private void process(final String annotationName, final Set<TypeElement> elements) {
        ProcessorContextHolder.withContext(processorContext, new Runnable() {
            @Override
            public void run() {
                for (final TypeElement element : elements) {
                    SourceCodeGenerator.generate(element, annotationName);
                }
            }
        });
    }

    private Map<String, Set<TypeElement>> calculateClassMap(
        final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv
    ) {
        final Map<String, Set<TypeElement>> classMap = Maps.newHashMap();

        for (final TypeElement annotation : annotations) {
            final String annotationName = annotation.getQualifiedName().toString();
            final Set<? extends Element> allElements = roundEnv.getElementsAnnotatedWith(annotation);
            final Set<TypeElement> elements = TypeUtils.foldToTypeElements(allElements);

            classMap.put(annotationName, elements);
        }
        return classMap;
    }

}
