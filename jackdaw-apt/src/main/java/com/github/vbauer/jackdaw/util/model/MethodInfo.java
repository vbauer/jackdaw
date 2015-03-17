package com.github.vbauer.jackdaw.util.model;

import com.github.vbauer.jackdaw.util.TypeUtils;
import com.google.common.collect.Sets;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */
public class MethodInfo {

    private final String name;
    private final TypeMirror returnType;
    private final List<TypeMirror> parameterTypes;
    private final ExecutableElement element;


    public MethodInfo(
        final String name, final TypeMirror returnType, final List<TypeMirror> parameterTypes,
        final ExecutableElement element
    ) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.element = element;
    }


    public static MethodInfo create(final ExecutableElement element) {
        return new MethodInfo(
            TypeUtils.getName(element),
            element.getReturnType(),
            TypeUtils.asTypes(element.getParameters()),
            element
        );
    }

    public static Set<ExecutableElement> convert(final Collection<MethodInfo> methodInfos) {
        final Set<ExecutableElement> methods = Sets.newHashSet();
        for (final MethodInfo method : methodInfos) {
            methods.add(method.getElement());
        }
        return methods;
    }


    public String getName() {
        return name;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }

    public List<TypeMirror> getParameterTypes() {
        return parameterTypes;
    }

    public ExecutableElement getElement() {
        return element;
    }


    @Override
    public int hashCode() {
        return Objects.hash(
            getName(),
            getParameterTypes(),
            getReturnType()
        );
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof  MethodInfo) {
            final MethodInfo other = (MethodInfo) obj;
            return Objects.equals(other.getName(), getName())
                && Objects.equals(other.getParameterTypes(), getParameterTypes())
                && Objects.equals(other.getReturnType(), getReturnType());
        }
        return false;
    }

}
