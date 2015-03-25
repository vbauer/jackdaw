package com.github.vbauer.jackdaw.util.model;

import com.github.vbauer.jackdaw.util.TypeUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;
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

    public static Set<ExecutableElement> convert(final Collection<MethodInfo> info) {
        final Set<ExecutableElement> methods = Sets.newHashSet();
        for (final MethodInfo method : info) {
            methods.add(method.getElement());
        }
        return methods;
    }

    public static MethodInfo find(
        final Collection<MethodInfo> info, final String name, final Collection<TypeMirror> types
    ) {
        for (final MethodInfo method : info) {
            final String methodName = method.getName();
            if (StringUtils.equals(methodName, name)
                && Objects.equal(method.getParameterTypes(), types)) {
                return method;
            }
        }
        return null;
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
        return Objects.hashCode(
            getName(),
            getParameterTypes(),
            getReturnType()
        );
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof  MethodInfo) {
            final MethodInfo other = (MethodInfo) obj;
            return Objects.equal(other.getName(), getName())
                && Objects.equal(other.getParameterTypes(), getParameterTypes())
                && Objects.equal(other.getReturnType(), getReturnType());
        }
        return false;
    }

}
