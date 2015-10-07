package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JFunction;
import com.github.vbauer.jackdaw.annotation.type.JFunctionType;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.callback.AnnotatedElementCallback;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author Vladislav Bauer
 */

public class JFunctionCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Functions";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);

    private static final String CLASS_NAME = "Function";
    private static final String PACKAGE_GUAVA = "com.google.common.base";
    private static final String PACKAGE_JAVA = "java.util.function";


    public JFunctionCodeGenerator() {
        super(NAME_MODIFIER, ClassType.UTILITY);
    }


    @Override
    public final Class<? extends Annotation> getAnnotation() {
        return JFunction.class;
    }


    @Override
    protected final void generateBody(
        final CodeGeneratorContext context, final Builder builder
    ) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
        SourceCodeUtils.processSimpleMethodsAndVariables(
            builder, typeElement, JFunction.class,
            new AnnotatedElementCallback<JFunction>() {
                @Override
                public void process(final Element element, final JFunction annotation) {
                    addFunction(builder, typeElement, element, annotation);
                }
            }
        );
    }


    private void addFunction(
        final Builder builder, final TypeElement typeElement,
        final Element element, final JFunction annotation
    ) {
        final JFunctionType functionType = annotation.type();
        final String packageName = getFunctionPackageName(functionType);
        final String caller = SourceCodeUtils.getCaller(element);
        final TypeName typeName = TypeUtils.getTypeName(element, true);

        final boolean nullable = annotation.nullable();
        final String operation = nullable ? "(input != null) ? input.$L : null" : "input.$L";

        final ParameterizedTypeName fieldTypeName = ParameterizedTypeName.get(
            ClassName.get(packageName, CLASS_NAME),
            TypeUtils.getTypeName(typeElement),
            typeName
        );

        builder.addField(
            FieldSpec.builder(
                fieldTypeName,
                SourceCodeUtils.normalizeName(TypeUtils.getName(element)),
                Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC
            )
            .initializer(
                SourceCodeUtils.lines(
                    "new $T() {",
                        "@Override",
                        "public $T apply(final $T input) {",
                        "return " + operation + ";",
                        "}",
                    "}"
                ),
                fieldTypeName,
                typeName, typeElement,
                caller
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
