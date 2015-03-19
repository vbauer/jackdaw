package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JFunction;
import com.github.vbauer.jackdaw.annotation.type.JFunctionType;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.callback.SimpleProcessorCallback;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author Vladislav Bauer
 */

public class JFunctionCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Functions";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);

    private static final String CLASS_NAME = "Function";
    private static final String PACKAGE_GUAVA = "com.google.common.base";
    private static final String PACKAGE_JAVA = "java.util.function";


    public JFunctionCodeGenerator(final TypeElement element) {
        super(element, NAME_MODIFIER, ClassType.UTILITY);
    }


    @Override
    protected void generateBody(final Builder builder) throws Exception {
        TypeUtils.processSimpleMethodsAndVariables(
            builder, typeElement, JFunction.class,
            new SimpleProcessorCallback<JFunction>() {
                @Override
                public void process(
                    final TypeSpec.Builder builder, final TypeElement type, final String methodName,
                    final JFunction annotation
                ) {
                    addFunction(builder, type, methodName, annotation);
                }
            }
        );
    }


    private void addFunction(
        final Builder builder, final TypeElement type, final String methodName,
        final JFunction annotation
    ) {
        final JFunctionType functionType = annotation.type();
        final String packageName = getFunctionPackageName(functionType);

        builder.addField(
            FieldSpec.builder(
                ParameterizedTypeName.get(
                    ClassName.get(packageName, CLASS_NAME),
                    TypeUtils.getTypeName(typeElement),
                    TypeUtils.getTypeName(type)
                ),
                SourceCodeUtils.normalizeName(methodName),
                Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC
            )
            .initializer(
                SourceCodeUtils.lines(
                    "new " + CLASS_NAME + "<$T, $T>() {",
                        "public $T apply(final $T input) {",
                            "return (input != null) ? input.$L() : null;",
                        "}",
                    "}"
                ),
                typeElement, type,
                type, typeElement,
                methodName
            )
            .build()
        );
    }

    private String getFunctionPackageName(final JFunctionType type) {
        switch (type) {
            case GUAVA:
                return PACKAGE_GUAVA;
            case JAVA:
                return PACKAGE_JAVA;
            default:
                throw new UnsupportedOperationException("Unknown type of function: " + type);
        }
    }

}
