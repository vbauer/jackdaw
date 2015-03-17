package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
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
import java.util.Map;

/**
 * @author Vladislav Bauer
 */

public class JBuilderCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "Builder";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);


    public JBuilderCodeGenerator(final TypeElement typeElement) {
        super(typeElement, NAME_MODIFIER, ClassType.POJO);
    }


    @Override
    protected void generateBody(final TypeSpec.Builder builder) throws Exception {
        final Map<String, TypeName> variables = SourceCodeUtils.getVariables(typeElement);
        addCreateMethod(builder);
        addMethods(builder, variables);
        addBuildMethod(builder, variables);
    }


    private void addMethods(
        final TypeSpec.Builder builder, final Map<String, TypeName> variables
    ) {
        for (final Map.Entry<String, TypeName> entry : variables.entrySet()) {
            final String variableName = entry.getKey();
            final TypeName variableType = entry.getValue();
            addMethod(builder, variableName, variableType);
        }
    }

    private void addBuildMethod(
        final TypeSpec.Builder builder, final Map<String, TypeName> variables
    ) {
        builder.addMethod(SourceCodeUtils.createFactoryMethod(
            "build", TypeUtils.getTypeName(typeElement), variables, Modifier.PUBLIC
        ));
    }

    private void addCreateMethod(final TypeSpec.Builder builder) {
        final ClassName originType = getOriginType();
        builder.addMethod(
            MethodSpec.methodBuilder("create")
                .returns(originType)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return new $T()", originType)
                .build()
        );
    }

    private void addMethod(
        final TypeSpec.Builder builder, final String variableName, final TypeName variableType
    ) {
        builder.addField(variableType, variableName, Modifier.PRIVATE);

        builder.addMethod(
            MethodSpec.methodBuilder(variableName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(variableType, variableName, Modifier.FINAL)
                .returns(getOriginType())
                .addStatement("this.$L = $L", variableName, variableName)
                .addStatement("return this")
                .build()
        );
    }

    private ClassName getOriginType() {
        return ClassName.get(packageName, className);
    }

}
