package com.github.vbauer.jackdaw.util;

import com.github.vbauer.jackdaw.util.callback.AnnotatedElementCallback;
import com.github.vbauer.jackdaw.util.callback.NamedTypeCallback;
import com.github.vbauer.jackdaw.util.callback.SimpleProcessorCallback;
import com.github.vbauer.jackdaw.util.model.MethodInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public final class TypeUtils {

    private TypeUtils() {
        throw new UnsupportedOperationException();
    }


    public static Set<TypeElement> foldToTypeElements(final Set<? extends Element> allElements) {
        final Set<TypeElement> elements = Sets.newHashSet();

        for (final Element element : allElements) {
            if (element instanceof TypeElement) {
                elements.add((TypeElement) element);
            } else if (element instanceof VariableElement || element instanceof ExecutableElement) {
                final Element enclosingElement = element.getEnclosingElement();
                if (enclosingElement instanceof TypeElement) {
                    elements.add((TypeElement) enclosingElement);
                } else if (enclosingElement instanceof ExecutableElement) {
                    elements.add((TypeElement) enclosingElement.getEnclosingElement());
                }
            } else {
                throw new UnsupportedOperationException("Unknown type of element");
            }
        }

        return elements;
    }

    public static String setterName(final String name) {
        final char[] data = name.toCharArray();
        data[0] = Character.toUpperCase(data[0]);
        return "set" + new String(data);
    }

    public static String getterName(final Element e) {
        final char[] field = TypeUtils.getName(e).toCharArray();
        field[0] = Character.toUpperCase(field[0]);

        final TypeMirror typeMirror = e.asType();
        final TypeKind kind = typeMirror.getKind();
        final String prefix = kind == TypeKind.BOOLEAN ? "is" : "get";
        return prefix + new String(field);
    }

    public static <T extends Annotation> T getAnnotation(
        final Class<T> annotationClass, final Element... elements
    ) {
        for (final Element element : elements) {
            final T annotation = element.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    public static boolean hasAnyType(final TypeElement typeElement, Class<?>... types) {
        final String typeName = typeElement.getQualifiedName().toString();
        for (final Class<?> type : types) {
            if (StringUtils.equals(type.getCanonicalName(), typeName)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Annotation> void filterElementsWithAnnotation(
        final TypeElement typeElement, final Collection<? extends Element> elements,
        final Class<T> annotationClass, final AnnotatedElementCallback<T> callback
    ) throws Exception {
        for (final Element element : elements) {
            final T annotation = getAnnotation(annotationClass, element, typeElement);

            if (annotation != null) {
                callback.process(element, annotation);
            }
        }

    }

    public static void processMethod(
        final TypeSpec.Builder builder, final Element element, final NamedTypeCallback namedTypeCallback
    ) throws Exception {
        if (!hasAnyModifier(element, Modifier.STATIC)) {
            final ExecutableElement ee = (ExecutableElement) element;
            final TypeElement type = ProcessorUtils.getWrappedType(ee.getReturnType());
            final String methodName = TypeUtils.getName(element);
            final String normalizedName = SourceCodeUtils.normalizeName(methodName);

            if (!SourceCodeUtils.hasField(builder, normalizedName)) {
                namedTypeCallback.process(methodName, type);
            }
        }
    }

    public static void processVariable(
        final TypeSpec.Builder builder, final Element element, final NamedTypeCallback namedTypeCallback
    ) throws Exception {
        if (!hasAnyModifier(element, Modifier.STATIC)) {
            final String methodName = getterName(element);
            final String normalizedName = SourceCodeUtils.normalizeName(methodName);

            if (!SourceCodeUtils.hasField(builder, normalizedName)) {
                final TypeElement type = ProcessorUtils.getWrappedType(element.asType());
                namedTypeCallback.process(methodName, type);
            }
        }
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
                    TypeUtils.processVariable(builder, element, new NamedTypeCallback() {
                        @Override
                        public void process(final String methodName, final TypeElement type) {
                            callback.process(builder, type, methodName, annotation);
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
                    TypeUtils.processMethod(builder, element, new NamedTypeCallback() {
                        @Override
                        public void process(final String methodName, final TypeElement type) {
                            if (TypeUtils.isSimpleMethod(element)) {
                                callback.process(builder, type, methodName, annotation);
                            }
                        }
                    });
                }
            }
        );

    }

    public static boolean isSimpleMethod(final Element element) {
        if (element instanceof ExecutableElement) {
            final ExecutableElement ee = (ExecutableElement) element;
            final List<? extends VariableElement> parameters = ee.getParameters();

            return !hasAnyModifier(ee, Modifier.STATIC) && parameters.isEmpty();
        }
        return false;
    }

    public static TypeElement getSuperclass(final TypeElement root) {
        final String objectClassName = Object.class.getCanonicalName();
        final TypeMirror superclass = root.getSuperclass();
        final TypeElement superclassElement = ProcessorUtils.getWrappedType(superclass);

        if (superclassElement != null) {
            final String rootClassName = superclassElement.getQualifiedName().toString();
            return rootClassName.equals(objectClassName) ? null : root;
        }
        return null;
    }

    public static String getDefaultValue(final TypeKind typeKind) {
        switch (typeKind) {
            case BOOLEAN:
                return Boolean.FALSE.toString();
            case INT:
            case SHORT:
            case BYTE:
            case LONG:
            case FLOAT:
            case DOUBLE:
                return "0";
            default:
                return "null";
        }
    }

    public static List<TypeMirror> asTypes(final Collection<? extends Element> elements) {
        final List<TypeMirror> types = Lists.newArrayList();
        for (final Element element : elements) {
            types.add(element.asType());
        }
        return types;
    }

    public static Collection<ExecutableElement> findUnimplementedMethods(final TypeElement rootElement) {
        final Pair<Collection<MethodInfo>, Collection<MethodInfo>> methodInfo = calculateMethodInfo(rootElement);
        final Collection<MethodInfo> unimplementedMethods = methodInfo.getRight();
        return MethodInfo.convert(unimplementedMethods);
    }

    public static String getName(final Element element) {
        return element.getSimpleName().toString();
    }

    public static TypeName getTypeName(final Element parameter) {
        return TypeName.get(parameter.asType());
    }

    public static TypeName getArrayTypeName(final Element parameter) {
        return ArrayTypeName.get(parameter.asType());
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
                if (hasAnyModifier(method, Modifier.ABSTRACT)) {
                    unimplementedMethods.add(MethodInfo.create(method));
                } else {
                    implementedMethods.add(MethodInfo.create(method));
                }
            }

            final List<? extends TypeMirror> interfaces = root.getInterfaces();
            for (final TypeMirror interfaceMirror : interfaces) {
                final TypeElement element = ProcessorUtils.getWrappedType(interfaceMirror);
                final List<ExecutableElement> interfaceMethods = ElementFilter.methodsIn(element.getEnclosedElements());
                for (final ExecutableElement method : interfaceMethods) {
                    unimplementedMethods.add(MethodInfo.create(method));
                }
            }

            root = TypeUtils.getSuperclass(root);
        }

        unimplementedMethods.removeAll(implementedMethods);

        return ImmutablePair.of(implementedMethods, unimplementedMethods);
    }

    public static boolean hasAnyModifier(final Element element, Modifier... modifiers) {
        final Set<Modifier> elementModifiers = element.getModifiers();
        return hasAnyModifier(elementModifiers, modifiers);
    }

    private static boolean hasAnyModifier(
        final Collection<Modifier> element, final Modifier... modifiers
    ) {
        for (final Modifier modifier : modifiers) {
            if (element.contains(modifier)) {
                return true;
            }
        }
        return false;
    }

}
