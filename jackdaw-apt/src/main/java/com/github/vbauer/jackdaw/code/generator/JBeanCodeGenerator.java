package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.code.base.GeneratedCodeGenerator;
import com.github.vbauer.jackdaw.util.SourceCodeUtils;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.github.vbauer.jackdaw.util.function.NameBean;
import com.github.vbauer.jackdaw.util.model.ClassType;
import com.github.vbauer.jackdaw.util.model.MethodInfo;
import com.google.common.collect.Lists;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public class JBeanCodeGenerator extends GeneratedCodeGenerator {

    private static final NameBean NAME_MODIFIER = new NameBean();


    public JBeanCodeGenerator(TypeElement typeElement) {
        super(typeElement, NAME_MODIFIER, ClassType.POJO);
    }


    @Override
    protected void generateBody(final TypeSpec.Builder builder) throws Exception {
        SourceCodeUtils.addParent(builder, typeElement);
        SourceCodeUtils.copyConstructors(builder, typeElement);

        final List<VariableElement> fields = ElementFilter.fieldsIn(typeElement.getEnclosedElements());
        final Pair<Set<MethodInfo>, Set<MethodInfo>> methodInfo = TypeUtils.calculateMethodInfo(typeElement);
        final Set<MethodInfo> implementedMethods = methodInfo.getLeft();

        for (final VariableElement field : fields) {
            addMethod(builder, field, implementedMethods, SourceCodeUtils.createGetter(field));
            addMethod(builder, field, implementedMethods, SourceCodeUtils.createSetter(field));
        }
    }

    private void addMethod(
        final TypeSpec.Builder builder, final VariableElement field,
        final Set<MethodInfo> implementedMethods, final MethodSpec getter
    ) {
        final MethodInfo getterMethodInfo = new MethodInfo(
            getter.name, field.asType(), Lists.<TypeMirror>newArrayList(), null
        );

        if (!implementedMethods.contains(getterMethodInfo)) {
            builder.addMethod(getter);
        }
    }

}
