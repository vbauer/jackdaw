package com.github.vbauer.jackdaw.code.context;

import com.github.vbauer.jackdaw.util.ProcessorUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.google.common.base.Function;

import javax.lang.model.element.TypeElement;

/**
 * @author Vladislav Bauer
 */

public class CodeGeneratorContext {

    private final TypeElement typeElement;
    private final String packageName;


    public CodeGeneratorContext(final TypeElement typeElement) {
        this.typeElement = typeElement;
        this.packageName = ProcessorUtils.packageName(typeElement);
    }


    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName(final Function<String, String> nameModifier) {
        return nameModifier.apply(TypeUtils.getName(getTypeElement()));
    }

}
