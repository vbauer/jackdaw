package com.github.vbauer.jackdaw.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public final class SourceCodeUtils {

    public static final String INDENT = "    ";
    public static final String BRACKET_OPEN = "{";
    public static final String BRACKET_CLOSE = "}";
    public static final String PACKAGE_SEPARATOR = ".";

    private static final String FIELD_SPECS = "fieldSpecs";
    private static final String FIELD_ANNOTATIONS = "annotations";


    private SourceCodeUtils() {
        throw new UnsupportedOperationException();
    }


    public static String indent(final int n) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append(INDENT);
        }
        return result.toString();
    }

    public static String lines(final String... lines) {
        final StringBuilder result = new StringBuilder();
        final int length = ArrayUtils.getLength(lines);
        int indent = 0;

        for (int i = 0; i < length; i++) {
            final String line = lines[i];

            if (line.startsWith(BRACKET_CLOSE)) {
                indent--;
            }

            result.append(indent(indent)).append(line);
            if (length - 1 != i) {
                result.append(SystemUtils.LINE_SEPARATOR);
            }

            if (line.endsWith(BRACKET_OPEN)) {
                indent++;
            }
        }
        return result.toString();
    }

    public static boolean hasField(
        final TypeSpec.Builder builder, final String methodName
    ) throws Exception {
        final List<FieldSpec> fieldSpecs = ReflectionUtils.readField(builder, FIELD_SPECS);
        for (final FieldSpec fieldSpec : fieldSpecs) {
            if (StringUtils.equals(fieldSpec.name, methodName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnnotation(
        final TypeSpec.Builder builder, final AnnotationMirror annotation
    ) throws Exception {
        final List<AnnotationSpec> annotations = ReflectionUtils.readField(builder, FIELD_ANNOTATIONS);
        final String annotationClassName = getName(annotation);

        for (final AnnotationSpec annotationSpec : annotations) {
            final String annotationSpecClassName = annotationSpec.type.toString();
            if (Objects.equal(annotationSpecClassName, annotationClassName)) {
                return true;
            }
        }
        return false;
    }

    public static String getName(final AnnotationMirror annotation) {
        return annotation.getAnnotationType().toString();
    }

    public static String normalizeName(final String name) {
        final String upperCase = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
        return StringUtils.remove(StringUtils.remove(upperCase, "GET_"), "IS_");
    }

    public static void copyConstructors(final TypeSpec.Builder builder, final TypeElement typeElement) {
        final List<? extends Element> elements = typeElement.getEnclosedElements();
        final List<ExecutableElement> constructors = ElementFilter.constructorsIn(elements);

        for (final ExecutableElement constructor : constructors) {
            final Set<Modifier> constructorModifiers = constructor.getModifiers();

            if (constructorModifiers.contains(Modifier.PUBLIC)) {
                final List<? extends VariableElement> parameters = constructor.getParameters();
                final MethodSpec.Builder newConstructor = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC);

                final List<String> names = Lists.newArrayList();
                for (final VariableElement parameter : parameters) {
                    final TypeName type = TypeUtils.getTypeName(parameter);
                    final String name = TypeUtils.getName(parameter);

                    newConstructor.addParameter(type, name, Modifier.FINAL);
                    names.add(name);
                }

                newConstructor.addStatement(
                    "super(" + StringUtils.repeat("$L", ", ", names.size()) + ")",
                    names.toArray(new String[names.size()])
                );

                builder.addMethod(newConstructor.build());
            }
        }
    }

    public static MethodSpec createFactoryMethod(
        final String name, final TypeName returnType, final Map<String, TypeName> variables,
        final Modifier... modifiers
    ) {
        final MethodSpec.Builder methodBuilder =
            MethodSpec.methodBuilder(name)
                .returns(returnType)
                .addModifiers(modifiers);

        methodBuilder.addStatement("final $T object = new $T()", returnType, returnType);
        for (final String variableName : variables.keySet()) {
            final String methodName = TypeUtils.setterName(variableName);
            methodBuilder.addStatement("object.$L($L)", methodName, variableName);
        }
        methodBuilder.addStatement("return object");
        return methodBuilder.build();
    }

    public static Map<String, TypeName> getVariables(final Element element) {
        final List<? extends Element> elements = element.getEnclosedElements();
        final List<VariableElement> variables = ElementFilter.fieldsIn(elements);
        final Map<String, TypeName> result = Maps.newLinkedHashMap();

        for (final VariableElement variable : variables) {
            final String variableName = TypeUtils.getName(variable);
            final TypeName variableType = TypeUtils.getTypeName(variable);
            result.put(variableName, variableType);
        }

        return result;
    }

    public static void addParent(final TypeSpec.Builder builder, final TypeElement superElement) {
        final ElementKind kind = superElement.getKind();
        final TypeName parentType = TypeUtils.getTypeName(superElement);

        if (kind.isInterface()) {
            builder.addSuperinterface(parentType);
        } else if (kind.isClass()) {
            builder.superclass(parentType);
        }
    }

    public static MethodSpec createGetter(final VariableElement field) {
        final TypeName typeName = TypeUtils.getTypeName(field);
        final String fieldName = TypeUtils.getName(field);
        final String getterName = TypeUtils.getterName(field);

        return MethodSpec.methodBuilder(getterName)
            .addModifiers(Modifier.PUBLIC)
            .returns(typeName)
            .addStatement("return $L", fieldName)
            .build();
    }

    public static MethodSpec createSetter(final VariableElement field) {
        final TypeName type = TypeUtils.getTypeName(field);
        final String fieldName = TypeUtils.getName(field);
        final String setterName = TypeUtils.setterName(fieldName);

        return MethodSpec.methodBuilder(setterName)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(type, fieldName, Modifier.FINAL)
            .addStatement("this.$L = $L", fieldName, fieldName)
            .build();
    }

}
