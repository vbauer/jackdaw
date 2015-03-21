package com.github.vbauer.jackdaw.util;

import com.github.vbauer.jackdaw.util.callback.AnnotatedElementCallback;
import com.github.vbauer.jackdaw.util.callback.NamedTypeCallback;
import com.github.vbauer.jackdaw.util.callback.SimpleProcessorCallback;
import com.github.vbauer.jackdaw.util.model.MethodInfo;
import com.google.common.base.CaseFormat;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
            if (TypeUtils.hasAnyModifier(constructor, Modifier.PUBLIC)) {
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

    public static Pair<Collection<MethodInfo>, Collection<MethodInfo>> calculateMethodInfo(
            final TypeElement rootElement
    ) {
        final Collection<MethodInfo> unimplementedMethods = Sets.newLinkedHashSet();
        final Collection<MethodInfo> implementedMethods = Sets.newLinkedHashSet();
        TypeElement root = rootElement;

        while (root != null) {
            final List<ExecutableElement> methods = ElementFilter.methodsIn(root.getEnclosedElements());
            for (final ExecutableElement method : methods) {
                if (TypeUtils.hasAnyModifier(method, Modifier.ABSTRACT)) {
                    unimplementedMethods.add(MethodInfo.create(method));
                } else {
                    implementedMethods.add(MethodInfo.create(method));
                }
            }

            final List<? extends TypeMirror> interfaces = root.getInterfaces();
            for (final TypeMirror interfaceMirror : interfaces) {
                final TypeElement element = ProcessorUtils.getWrappedType(interfaceMirror);
                final List<ExecutableElement> interfaceMethods =
                    ElementFilter.methodsIn(element.getEnclosedElements());

                for (final ExecutableElement method : interfaceMethods) {
                    unimplementedMethods.add(MethodInfo.create(method));
                }
            }

            root = TypeUtils.getSuperclass(root);
        }

        unimplementedMethods.removeAll(implementedMethods);

        return ImmutablePair.of(implementedMethods, unimplementedMethods);
    }

    public static Collection<ExecutableElement> findUnimplementedMethods(final TypeElement rootElement) {
        final Pair<Collection<MethodInfo>, Collection<MethodInfo>> methodInfo = calculateMethodInfo(rootElement);
        final Collection<MethodInfo> unimplementedMethods = methodInfo.getRight();
        return MethodInfo.convert(unimplementedMethods);
    }

    public static Collection<MethodInfo> findImplementedMethods(final TypeElement rootElement) {
        final Pair<Collection<MethodInfo>, Collection<MethodInfo>> methodInfo = calculateMethodInfo(rootElement);
        return methodInfo.getLeft();
    }

    public static <T extends Annotation> void processSimpleMethodsAndVariables(
        final TypeSpec.Builder builder, final TypeElement typeElement,
        final Class<T> annotationClass, final SimpleProcessorCallback<T> callback
    ) throws Exception {
        final List<? extends Element> elements = typeElement.getEnclosedElements();

        TypeUtils.filterElementsWithAnnotation(
            typeElement, ElementFilter.fieldsIn(elements), annotationClass,
            new AnnotatedElementCallback<T>() {
                @Override
                public void process(final Element element, final T annotation) throws Exception {
                    processVariable(builder, element, new NamedTypeCallback() {
                        @Override
                        public void process(final String methodName, final TypeElement type) {
                            callback.process(type, methodName, annotation);
                        }
                    });
                }
            }
        );

        TypeUtils.filterElementsWithAnnotation(
            typeElement, ElementFilter.methodsIn(elements), annotationClass,
            new AnnotatedElementCallback<T>() {
                @Override
                public void process(final Element element, final T annotation) throws Exception {
                    processMethod(builder, element, new NamedTypeCallback() {
                        @Override
                        public void process(final String methodName, final TypeElement type) {
                            callback.process(type, methodName, annotation);
                        }
                    });
                }
            }
        );

    }


    private static void processMethod(
        final TypeSpec.Builder builder, final Element element, final NamedTypeCallback namedTypeCallback
    ) throws Exception {
        if (TypeUtils.isSimpleMethod(element)) {
            final ExecutableElement ee = (ExecutableElement) element;
            final TypeElement type = ProcessorUtils.getWrappedType(ee.getReturnType());
            final String methodName = TypeUtils.getName(element);
            final String normalizedName = SourceCodeUtils.normalizeName(methodName);

            if (!SourceCodeUtils.hasField(builder, normalizedName)) {
                namedTypeCallback.process(methodName, type);
            }
        }
    }

    private static void processVariable(
        final TypeSpec.Builder builder, final Element element, final NamedTypeCallback namedTypeCallback
    ) throws Exception {
        if (!TypeUtils.hasAnyModifier(element, Modifier.STATIC)) {
            final String methodName = TypeUtils.getterName(element);
            final String normalizedName = SourceCodeUtils.normalizeName(methodName);

            if (!SourceCodeUtils.hasField(builder, normalizedName)) {
                final TypeElement type = ProcessorUtils.getWrappedType(element.asType());
                namedTypeCallback.process(methodName, type);
            }
        }
    }

}
