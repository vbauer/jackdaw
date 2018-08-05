package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JBean;
import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.util.ModelUtils;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.function.NameBean;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.github.vbauer.jackdaw.util.model.MethodInfo;
import com.google.common.collect.Lists;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.Collection;
import java.util.List;

/**
 * @author Vladislav Bauer
 */

public class JBeanCodeGenerator extends GeneratedCodeGenerator {

    private static final NameBean NAME_MODIFIER = new NameBean();


    public JBeanCodeGenerator() {
        super(NAME_MODIFIER, ClassType.POJO);
    }


    @Override
    public final Class<JBean> getAnnotation() {
        return JBean.class;
    }


    @Override
    protected final void generateBody(
        final CodeGeneratorContext context, final TypeSpec.Builder builder
    ) {
        final TypeElement typeElement = context.getTypeElement();
        SourceCodeUtils.addParent(builder, typeElement);
        SourceCodeUtils.copyConstructors(builder, typeElement);

        final List<VariableElement> fields = ElementFilter.fieldsIn(typeElement.getEnclosedElements());
        final Collection<MethodInfo> implementedMethods = ModelUtils.findImplementedMethods(typeElement);

        for (final VariableElement field : fields) {
            if (!TypeUtils.hasAnyModifier(field, Modifier.STATIC)) {
                addMethod(builder, field, implementedMethods, SourceCodeUtils.createGetter(field));

                if (!TypeUtils.hasAnyModifier(field, Modifier.FINAL, Modifier.PRIVATE)) {
                    addMethod(builder, field, implementedMethods, SourceCodeUtils.createSetter(field));
                }
            }
        }
    }

    private void addMethod(
        final TypeSpec.Builder builder, final VariableElement field,
        final Collection<MethodInfo> implementedMethods, final MethodSpec getter
    ) {
        final MethodInfo getterMethodInfo = new MethodInfo(
            getter.name, field.asType(), Lists.<TypeMirror>newArrayList(), null
        );

        if (!implementedMethods.contains(getterMethodInfo)) {
            builder.addMethod(getter);
        }
    }

}
