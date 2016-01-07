package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.code.SourceCodeGenerator;
import com.github.vbauer.jackdaw.code.SourceCodeGeneratorRegistry;
import com.github.vbauer.jackdaw.context.ProcessorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextFactory;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;
import com.github.vbauer.jackdaw.context.ProcessorSourceContext;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author Vladislav Bauer
 */

public class JackdawProcessor extends AbstractProcessor {

    private ProcessorContext processorContext;


    @Override
    public final synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        processorContext = ProcessorContextFactory.create(processingEnv);
    }

    @Override
    public final boolean process(
        final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv
    ) {
        final boolean needToProcess =
            !(roundEnv.processingOver() || annotations.isEmpty());

        if (needToProcess) {
            final Collection<ProcessorSourceContext> sourceContexts =
                ProcessorSourceContext.calculate(roundEnv, annotations);

            processorContext.setSourceContexts(sourceContexts);

            process();
        }
        return false;
    }

    @Override
    public final SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return SourceCodeGeneratorRegistry.getSupportedAnnotations();
    }

    @Override
    public final Set<String> getSupportedOptions() {
        return ProcessorContextFactory.getSupportedOptions();
    }


    private void process() {
        ProcessorContextHolder.withContext(processorContext, new Runnable() {
            @Override
            public void run() {
                final Collection<ProcessorSourceContext> sourceContexts =
                    processorContext.getSourceContexts();

                for (final ProcessorSourceContext sourceContext : sourceContexts) {
                    final List<TypeElement> elements = sourceContext.getElements();
                    final String annotationName = sourceContext.getAnnotationClassName();

                    if (!SourceCodeGenerator.generate(annotationName, elements)) {
                        break;
                    }
                }
            }
        });
    }

}
