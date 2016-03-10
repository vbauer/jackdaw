package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JPredicate;
import com.github.vbauer.jackdaw.annotation.type.JPredicateType;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.ModelUtils;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.SourceTextUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.callback.AnnotatedElementCallback;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;

/**
 * @author Vladislav Bauer
 */

public class JPredicateCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Predicates";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);

    private static final String CLASS_NAME = "Predicate";

    private static final String PACKAGE_JAVA = "java.util.function";
    private static final String PACKAGE_GUAVA = "com.google.common.base";
    private static final String PACKAGE_COMMONS = "org.apache.commons.collections";


    public JPredicateCodeGenerator() {
        super(NAME_MODIFIER, ClassType.UTILITY);
    }


    @Override
    public final Class<? extends Annotation> getAnnotation() {
        return JPredicate.class;
    }


    @Override
    protected final void generateBody(
        final CodeGeneratorContext context, final TypeSpec.Builder builder
    ) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
        SourceCodeUtils.processSimpleMethodsAndVariables(
            builder, typeElement, JPredicate.class,
            new AnnotatedElementCallback<JPredicate>() {
                @Override
                public void process(final Element element, final JPredicate annotation) {
                    final TypeMirror type = TypeUtils.getTypeMirror(element);
                    if (TypeUtils.hasAnyType(type, Boolean.class, boolean.class)) {
                        addPredicate(builder, typeElement, element, annotation);
                    }
                }
            }
        );
    }


    private void addPredicate(
        final TypeSpec.Builder builder, final TypeElement typeElement,
        final Element element, final JPredicate annotation
    ) {
        final JPredicateType predicateType = annotation.type();
        final boolean reverse = annotation.reverse();
        final boolean nullable = annotation.nullable();
        final TypeName fieldType = getFieldType(predicateType, typeElement);

        builder.addField(
            FieldSpec.builder(
                fieldType,
                SourceCodeUtils.normalizeName(TypeUtils.getName(element)),
                Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC
            )
            .initializer(createInitializer(
                predicateType, reverse, nullable,
                fieldType, element, typeElement
            ))
            .build()
        );
    }

    private TypeName getFieldType(
        final JPredicateType type, final TypeElement typeElement
    ) {
        final String packageName = getPredicatePackageName(type);
        switch (type) {
            case GUAVA:
            case JAVA:
                return ParameterizedTypeName.get(
                    ClassName.get(packageName, CLASS_NAME),
                    TypeUtils.getTypeName(typeElement)
                );
            case COMMONS:
                return ClassName.get(packageName, CLASS_NAME);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private CodeBlock createInitializer(
        final JPredicateType type, final boolean reverse, final boolean nullable,
        final TypeName predicateTypeName, final Element element, final TypeElement typeElement
    ) {
        final String operation = reverse ? "!" : StringUtils.EMPTY;
        final String caller = ModelUtils.getCaller(element);
        final String nullableGuard = nullable ? "input != null && " : StringUtils.EMPTY;

        switch (type) {
            case JAVA:
            case GUAVA:
                return CodeBlock.builder().add(
                    SourceTextUtils.lines(
                        "new $T() {",
                            "@Override",
                            "public boolean apply(final $T input) {",
                                "return " + nullableGuard + operation + "input.$L;",
                            "}",
                        "}"
                    ),
                    predicateTypeName, typeElement, caller
                ).build();
            case COMMONS:
                return CodeBlock.builder().add(
                    SourceTextUtils.lines(
                        "new $T() {",
                            "@Override",
                            "public boolean evaluate(final $T input) {",
                                "return " + nullableGuard + operation + "(($T) input).$L;",
                            "}",
                        "}"
                    ),
                    predicateTypeName, Object.class, typeElement, caller
                ).build();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private String getPredicatePackageName(final JPredicateType type) {
        switch (type) {
            case JAVA:
                return PACKAGE_JAVA;
            case GUAVA:
                return PACKAGE_GUAVA;
            case COMMONS:
                return PACKAGE_COMMONS;
            default:
                throw new UnsupportedOperationException();
        }
    }

}
