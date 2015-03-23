package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.code.SourceCodeGenerator;
import com.github.vbauer.jackdaw.code.SourceCodeGeneratorRegistry;
import com.github.vbauer.jackdaw.context.ProcessorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextFactory;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;
import com.github.vbauer.jackdaw.context.ProcessorSourceContext;
import com.github.vbauer.jackdaw.util.ProcessorUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author Vladislav Bauer
 */

public class JackdawProcessor extends AbstractProcessor {

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

            process();
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

    @Override
    public Set<String> getSupportedOptions() {
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

                    SourceCodeGenerator.generate(annotationName, elements);
                }
            }
        });
    }

}
