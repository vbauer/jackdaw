package com.github.vbauer.jackdaw.code.base;

import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;

/**
 * @author Vladislav Bauer
 */

public interface CodeGenerator {

    void generate(CodeGeneratorContext context) throws Exception;

}
