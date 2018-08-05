package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JAdapter;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.ModelUtils;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.google.common.collect.Sets;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public class JAdapterCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Adapter";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);


    public JAdapterCodeGenerator() {
        super(NAME_MODIFIER, ClassType.POJO);
    }


    @Override
    public final Class<JAdapter> getAnnotation() {
        return JAdapter.class;
    }


    @Override
    protected final void generateBody(
        final CodeGeneratorContext context, final TypeSpec.Builder builder
    ) {
        final TypeElement typeElement = context.getTypeElement();
        SourceCodeUtils.addParent(builder, typeElement);
        SourceCodeUtils.copyConstructors(builder, typeElement);

        final Collection<ExecutableElement> methods = ModelUtils.findUnimplementedMethods(typeElement);
        for (final ExecutableElement method : methods) {
            addMethod(builder, method);
        }
    }

    private void addMethod(
        final TypeSpec.Builder builder, final ExecutableElement element
    ) {
        final TypeMirror returnType = element.getReturnType();
        final List<? extends VariableElement> parameters = element.getParameters();
        final String methodName = TypeUtils.getName(element);

        final Set<Modifier> modifiers = Sets.newHashSet(element.getModifiers());
        modifiers.remove(Modifier.ABSTRACT);
        final Modifier[] methodModifiers = modifiers.toArray(new Modifier[0]);

        final MethodSpec.Builder methodBuilder =
            MethodSpec.methodBuilder(methodName)
                .addModifiers(methodModifiers)
                .addAnnotation(Override.class)
                .returns(TypeName.get(returnType));

        final TypeKind returnTypeKind = returnType.getKind();
        if (returnTypeKind != TypeKind.VOID) {
            final String defaultValue = TypeUtils.getDefaultValue(returnTypeKind);
            methodBuilder.addStatement("return " + defaultValue);
        }

        for (final VariableElement parameter : parameters) {
            methodBuilder.addParameter(
                TypeUtils.getTypeName(parameter),
                TypeUtils.getName(parameter),
                Modifier.FINAL
            );
        }

        builder.addMethod(methodBuilder.build());
    }

}
