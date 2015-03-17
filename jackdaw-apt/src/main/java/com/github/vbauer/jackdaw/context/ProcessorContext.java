package com.github.vbauer.jackdaw.context;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author Vladislav Bauer
 */

public class ProcessorContext {

    private final ProcessingEnvironment pe;
    private boolean addSuppressWarningsAnnotation;
    private boolean addGeneratedAnnotation;
    private boolean addGeneratedDate;


    public ProcessorContext(final ProcessingEnvironment pe) {
        this.pe = pe;
    }


    public ProcessingEnvironment getProcessingEnvironment() {
        return pe;
    }

    public boolean isAddSuppressWarningsAnnotation() {
        return addSuppressWarningsAnnotation;
    }

    public ProcessorContext setAddSuppressWarningsAnnotation(final boolean addSuppressWarningsAnnotation) {
        this.addSuppressWarningsAnnotation = addSuppressWarningsAnnotation;
        return this;
    }

    public boolean isAddGeneratedAnnotation() {
        return addGeneratedAnnotation;
    }

    public ProcessorContext setAddGeneratedAnnotation(final boolean addGeneratedAnnotation) {
        this.addGeneratedAnnotation = addGeneratedAnnotation;
        return this;
    }

    public boolean isAddGeneratedDate() {
        return addGeneratedDate;
    }

    public ProcessorContext setAddGeneratedDate(final boolean addGeneratedDate) {
        this.addGeneratedDate = addGeneratedDate;
        return this;
    }

}
