package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.code.SourceCodeGenerator;
import com.github.vbauer.jackdaw.code.SourceCodeGeneratorRegistry;
import com.github.vbauer.jackdaw.context.ProcessorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextFactory;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;
import com.github.vbauer.jackdaw.context.ProcessorSourceContext;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.List;
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
            final Collection<ProcessorSourceContext> sourceContexts =
                ProcessorSourceContext.calculate(roundEnv, annotations);

            processorContext.setSourceContexts(sourceContexts);

            ProcessorContextHolder.withContext(processorContext, new Runnable() {
                @Override
                public void run() {
                    for (final ProcessorSourceContext sourceContext : sourceContexts) {
                        final List<Pair<TypeElement, String>> elementsInfo =
                            sourceContext.getElementInfo();

                        for (final Pair<TypeElement, String> elementInfo : elementsInfo) {
                            final TypeElement element = elementInfo.getLeft();
                            final String annotationName = sourceContext.getAnnotationClassName();
                            SourceCodeGenerator.generate(element, annotationName);
                        }
                    }
                }
            });
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

}
