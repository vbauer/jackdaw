package com.github.vbauer.jackdaw.code.base;

import com.github.vbauer.jackdaw.util.ProcessorUtils;

import javax.lang.model.element.TypeElement;

/**
 * @author Vladislav Bauer
 */

public abstract class CodeGenerator {

    protected final TypeElement typeElement;
    protected final String packageName;


    public CodeGenerator(final TypeElement typeElement) {
        this.typeElement = typeElement;
        this.packageName = ProcessorUtils.packageName(typeElement);
    }


    public abstract void generate() throws Exception;

}
