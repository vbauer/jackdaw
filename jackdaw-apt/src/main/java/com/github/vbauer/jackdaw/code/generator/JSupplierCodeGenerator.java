package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JSupplier;
import com.github.vbauer.jackdaw.annotation.type.JSupplierType;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.ModelUtils;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.callback.AnnotatedElementCallback;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author Vladislav Bauer
 */

public class JSupplierCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Suppliers";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);

    private static final String CLASS_NAME = "Supplier";
    private static final String PACKAGE_GUAVA = "com.google.common.base";
    private static final String PACKAGE_JAVA = "java.util.function";


    public JSupplierCodeGenerator() {
        super(NAME_MODIFIER, ClassType.UTILITY);
    }


    @Override
    public final Class<JSupplier> getAnnotation() {
        return JSupplier.class;
    }


    @Override
    protected final void generateBody(
        final CodeGeneratorContext context, final Builder builder
    ) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
        SourceCodeUtils.processSimpleMethodsAndVariables(
            builder, typeElement, JSupplier.class,
            new AnnotatedElementCallback<JSupplier>() {
                @Override
                public void process(final Element element, final JSupplier annotation) {
                    addFunction(builder, typeElement, element, annotation);
                }
            }
        );
    }


    private void addFunction(
        final Builder builder, final TypeElement typeElement,
        final Element element, final JSupplier annotation
    ) {
        final JSupplierType functionType = annotation.type();
        final String packageName = getFunctionPackageName(functionType);
        final String caller = ModelUtils.getCaller(element);
        final TypeName typeName = TypeUtils.getTypeName(element, true);

        final ClassName supplierClass = ClassName.get(packageName, CLASS_NAME);
        final TypeName parameterClass = TypeUtils.getTypeName(typeElement);
        final ParameterizedTypeName fieldTypeName =
            ParameterizedTypeName.get(supplierClass, typeName);

        builder.addMethod(
            MethodSpec.methodBuilder(TypeUtils.getName(element))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameterClass, "o", Modifier.FINAL)
                .returns(fieldTypeName)
                .addCode(
                    SourceCodeUtils.lines(
                        "return new $T() {",
                            "@Override",
                            "public $T get() {",
                                "return o.$L;",
                            "}",
                        "};",
                        ""
                    ),
                    fieldTypeName, typeName, caller
                )
                .build()
        );
    }

    private String getFunctionPackageName(final JSupplierType type) {
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
