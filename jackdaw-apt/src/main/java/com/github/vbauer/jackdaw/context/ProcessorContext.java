package com.github.vbauer.jackdaw.context;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Collection;

/**
 * @author Vladislav Bauer
 */

public class ProcessorContext {

    private final ProcessingEnvironment pe;
    private boolean addSuppressWarningsAnnotation;
    private boolean addGeneratedAnnotation;
    private boolean addGeneratedDate;

    private Collection<ProcessorSourceContext> sourceContexts;


    public ProcessorContext(final ProcessingEnvironment pe) {
        this.pe = pe;
    }


    public final ProcessingEnvironment getProcessingEnvironment() {
        return pe;
    }

    public final boolean isAddSuppressWarningsAnnotation() {
        return addSuppressWarningsAnnotation;
    }

    public final ProcessorContext setAddSuppressWarningsAnnotation(final boolean addSuppressWarningsAnnotation) {
        this.addSuppressWarningsAnnotation = addSuppressWarningsAnnotation;
        return this;
    }

    public final boolean isAddGeneratedAnnotation() {
        return addGeneratedAnnotation;
    }

    public final ProcessorContext setAddGeneratedAnnotation(final boolean addGeneratedAnnotation) {
        this.addGeneratedAnnotation = addGeneratedAnnotation;
        return this;
    }

    public final boolean isAddGeneratedDate() {
        return addGeneratedDate;
    }

    public final ProcessorContext setAddGeneratedDate(final boolean addGeneratedDate) {
        this.addGeneratedDate = addGeneratedDate;
        return this;
    }

    public final Collection<ProcessorSourceContext> getSourceContexts() {
        return sourceContexts;
    }

    public final void setSourceContexts(final Collection<ProcessorSourceContext> sourceContexts) {
        this.sourceContexts = sourceContexts;
    }

}
