package com.github.vbauer.jackdaw.util;

import com.github.vbauer.jackdaw.util.model.MethodInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Vladislav Bauer
 */

public final class ModelUtils {

    private ModelUtils() {
        throw new UnsupportedOperationException();
    }


    public static String getName(final AnnotationMirror annotation) {
        return annotation.getAnnotationType().toString();
    }

    public static String getCaller(final Element element) {
        final String name = TypeUtils.getName(element);
        if (element instanceof ExecutableElement) {
            return name + "()";
        }
        return name;
    }

    public static Predicate<Element> createHasSetterPredicate(final TypeElement typeElement) {
        final Collection<MethodInfo> methods = findImplementedMethods(typeElement);
        return new Predicate<Element>() {
            @Override
            public boolean apply(final Element element) {
                final String name = TypeUtils.getName(element);
                final String setterName = TypeUtils.setterName(name);

                final List<TypeMirror> types = Collections.singletonList(TypeUtils.getTypeMirror(element));
                final MethodInfo setter = MethodInfo.find(methods, setterName, types);

                if (setter != null) {
                    final ExecutableElement setterElement = setter.getElement();
                    return TypeUtils.hasAnyModifier(setterElement, Modifier.PUBLIC);
                }
                return false;
            }
        };
    }

    public static Map<String, TypeName> getVariables(
        final TypeElement typeElement, final Predicate<Element> predicate
    ) {
        final List<? extends Element> elements = typeElement.getEnclosedElements();
        final List<VariableElement> variables = ElementFilter.fieldsIn(elements);
        final Map<String, TypeName> result = Maps.newLinkedHashMap();

        for (final VariableElement variable : variables) {
            if (predicate.apply(variable)) {
                final String variableName = TypeUtils.getName(variable);
                final TypeName variableType = TypeUtils.getTypeName(variable);
                result.put(variableName, variableType);
            }
        }

        return result;
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

            final List<TypeElement> interfaces = getInterfaces(rootElement);
            for (final TypeElement element : interfaces) {
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

    public static List<TypeElement> getInterfaces(final TypeElement root) {
        final List<? extends TypeMirror> interfaces = root.getInterfaces();
        final List<TypeElement> result = Lists.newArrayList();

        for (final TypeMirror interfaceMirror : interfaces) {
            result.add(ProcessorUtils.getWrappedType(interfaceMirror));
        }
        return result;
    }

}
