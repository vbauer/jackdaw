package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JFactoryMethod;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.ModelUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.google.common.collect.Lists;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public class JFactoryMethodCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Factory";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);


    public JFactoryMethodCodeGenerator() {
        super(NAME_MODIFIER, ClassType.UTILITY);
    }


    @Override
    public final Class<JFactoryMethod> getAnnotation() {
        return JFactoryMethod.class;
    }


    @Override
    protected final void generateBody(
        final CodeGeneratorContext context, final TypeSpec.Builder builder
    ) {
        final TypeElement typeElement = context.getTypeElement();
        final JFactoryMethod annotation = typeElement.getAnnotation(JFactoryMethod.class);
        final Set<Modifier> modifiers = typeElement.getModifiers();
        final ElementKind kind = typeElement.getKind();

        if (annotation != null && kind == ElementKind.CLASS && !modifiers.contains(Modifier.ABSTRACT)) {
            generateFactoryMethod(builder, typeElement, annotation);
        }
    }


    private void generateFactoryMethod(
        final TypeSpec.Builder builder, final TypeElement typeElement,
        final JFactoryMethod annotation
    ) {
        final String methodName = annotation.method();
        final String[] args = annotation.arguments();
        final boolean all = annotation.all();

        final MethodSpec.Builder methodBuilder =
            MethodSpec.methodBuilder(methodName)
                .returns(TypeUtils.getTypeName(typeElement))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        final List<String> arguments = Lists.newArrayList(args);
        final Map<String, TypeName> variables = ModelUtils.getVariables(
            typeElement, ModelUtils.createHasSetterPredicate(typeElement)
        );

        methodBuilder.addStatement("final $T object = new $T()", typeElement, typeElement);
        for (final Map.Entry<String, TypeName> entry : variables.entrySet()) {
            final String variableName = entry.getKey();
            final TypeName variableType = entry.getValue();

            if (all || arguments.contains(variableName)) {
                final String setterName = TypeUtils.setterName(variableName);
                methodBuilder.addParameter(variableType, variableName, Modifier.FINAL);
                methodBuilder.addStatement("object.$L($L)", setterName, variableName);
            }
        }
        methodBuilder.addStatement("return object");

        builder.addMethod(methodBuilder.build());
    }

}
