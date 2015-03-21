package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JBuilder;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Vladislav Bauer
 */

public class JBuilderCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Builder";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);


    public JBuilderCodeGenerator() {
        super(NAME_MODIFIER, ClassType.POJO);
    }


    @Override
    public Class<? extends Annotation> getAnnotation() {
        return JBuilder.class;
    }


    @Override
    protected void generateBody(
        final CodeGeneratorContext context, final TypeSpec.Builder builder
    ) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
        final ClassName originType = getOriginType(context);
        final Map<String, TypeName> variables = SourceCodeUtils.getVariables(typeElement);

        addCreateMethod(builder, originType);
        addMethods(builder, variables, originType);
        addBuildMethod(builder, typeElement, variables);
    }


    private void addMethods(
        final TypeSpec.Builder builder, final Map<String, TypeName> variables,
        final ClassName originType
    ) {
        for (final Map.Entry<String, TypeName> entry : variables.entrySet()) {
            final String variableName = entry.getKey();
            final TypeName variableType = entry.getValue();
            addMethod(builder, variableName, variableType, originType);
        }
    }

    private void addBuildMethod(
        final TypeSpec.Builder builder, final TypeElement typeElement,
        final Map<String, TypeName> variables
    ) {
        builder.addMethod(SourceCodeUtils.createFactoryMethod(
            "build", TypeUtils.getTypeName(typeElement), variables, Modifier.PUBLIC
        ));
    }

    private void addCreateMethod(final TypeSpec.Builder builder, final ClassName originType) {
        builder.addMethod(
            MethodSpec.methodBuilder("create")
                .returns(originType)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return new $T()", originType)
                .build()
        );
    }

    private void addMethod(
        final TypeSpec.Builder builder, final String variableName, final TypeName variableType,
        final ClassName originType
    ) {
        builder.addField(variableType, variableName, Modifier.PRIVATE);

        builder.addMethod(
            MethodSpec.methodBuilder(variableName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(variableType, variableName, Modifier.FINAL)
                .returns(originType)
                .addStatement("this.$L = $L", variableName, variableName)
                .addStatement("return this")
                .build()
        );
    }

    private ClassName getOriginType(final CodeGeneratorContext context) {
        final String packageName = context.getPackageName();
        final String className = context.getClassName(getNameModifier());
        return ClassName.get(packageName, className);
    }

}
