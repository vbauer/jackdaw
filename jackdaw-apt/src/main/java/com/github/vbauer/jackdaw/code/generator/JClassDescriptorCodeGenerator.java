package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.function.AddSuffix;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;

/**
 * @author Vladislav Bauer
 */

public class JClassDescriptorCodeGenerator extends GeneratedCodeGenerator {

    private static final String SUFFIX = "ClassDescriptor";
    private static final AddSuffix NAME_MODIFIER = new AddSuffix(SUFFIX);

    private static final String PREFIX_FIELD = "field";
    private static final String PREFIX_METHOD = "method";


    public JClassDescriptorCodeGenerator() {
        super(NAME_MODIFIER, ClassType.UTILITY);
    }


    @Override
    protected void generateBody(
        final CodeGeneratorContext context, final TypeSpec.Builder builder
    ) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
        final List<? extends Element> elements = typeElement.getEnclosedElements();
        final List<VariableElement> fields = ElementFilter.fieldsIn(elements);
        final List<ExecutableElement> methods = ElementFilter.methodsIn(elements);

        addStaticFields(builder, fields, PREFIX_FIELD);
        addStaticFields(builder, methods, PREFIX_METHOD);
    }


    private void addStaticFields(
        final TypeSpec.Builder builder, final List<? extends Element> elements, final String prefix
    ) {
        for (final Element element : elements) {
            addStaticField(builder, element, prefix);
        }
    }

    private void addStaticField(
        final TypeSpec.Builder builder, final Element element, final String prefix
    ) {
        final String originName = TypeUtils.getName(element);
        final String name = SourceCodeUtils.normalizeName(
            prefix + StringUtils.capitalize(originName)
        );

        builder.addField(
            FieldSpec.builder(
                ClassName.get("java.lang", "String"), name,
                Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC
            )
            .initializer("$S", originName)
            .build()
        );
    }

}
