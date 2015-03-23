package com.github.vbauer.jackdaw.code.base;

import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;

import java.lang.annotation.Annotation;

/**
 * @author Vladislav Bauer
 */

public interface CodeGenerator {

    Class<? extends Annotation> getAnnotation();

    void generate(CodeGeneratorContext context) throws Exception;

    void onStart() throws Exception;

    void onFinish() throws Exception;

}
