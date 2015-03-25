package com.github.vbauer.jackdaw.util;

import com.github.vbauer.jackdaw.context.ProcessorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;
import com.github.vbauer.jackdaw.context.ProcessorSourceContext;
import com.github.vbauer.jackdaw.util.callback.AnnotatedElementCallback;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
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


    public static Collection<TypeElement> foldToTypeElements(final Set<? extends Element> allElements) {
        final Collection<TypeElement> elements = Sets.newHashSet();

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
        final char[] field = getName(e).toCharArray();
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

    public static boolean hasAnyType(final TypeMirror typeMirror, Class<?>... types) {
        final String typeName = typeMirror.toString();
        for (final Class<?> type : types) {
            if (StringUtils.equals(type.getCanonicalName(), typeName)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Element> Collection<T> filterWithoutAnnotation(
        final Collection<T> elements, final Class<? extends Annotation> annotationClass
    ) {
        final Collection<T> result = Lists.newArrayList();
        for (final T element : elements) {
            final Annotation annotation = getAnnotation(annotationClass, element);
            if (annotation == null) {
                result.add(element);
            }
        }
        return result;
    }

    public static <T extends Annotation> void filterWithAnnotation(
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

    public static String getName(final Element element) {
        return element.getSimpleName().toString();
    }

    public static TypeName getTypeName(final Element element) {
        return getTypeName(element, false);
    }

    public static TypeName getTypeName(final Element element, final boolean wrap) {
        final TypeMirror typeMirror = getTypeMirror(element);
        final TypeKind kind = typeMirror.getKind();

        if (kind == TypeKind.ERROR) {
            final ProcessorContext context = ProcessorContextHolder.getContext();
            final Collection<ProcessorSourceContext> sourceContexts = context.getSourceContexts();

            final String typeName = String.valueOf(typeMirror);
            final TypeElement originElement = ProcessorSourceContext.guessOriginElement(sourceContexts, typeName);

            if (originElement != null) {
                final String packageName = ProcessorUtils.packageName(originElement);
                return ClassName.get(packageName, typeName);
            }
            return ClassName.bestGuess(typeName);
        }

        return TypeName.get(wrap ? ProcessorUtils.getWrappedType(typeMirror).asType() : typeMirror);
    }

    public static TypeMirror getTypeMirror(final Element element) {
        return element instanceof ExecutableElement
            ? ((ExecutableElement) element).getReturnType() : element.asType();
    }

    public static TypeName getArrayTypeName(final Element parameter) {
        return ArrayTypeName.get(parameter.asType());
    }

    public static boolean hasAnyModifier(final Element element, Modifier... modifiers) {
        final Set<Modifier> elementModifiers = element.getModifiers();
        return hasAnyModifier(elementModifiers, modifiers);
    }

    public static boolean hasAnyModifier(
        final Collection<Modifier> element, final Modifier... modifiers
    ) {
        for (final Modifier modifier : modifiers) {
            if (element.contains(modifier)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inHierarchy(final TypeElement rootElement, final String className) {
        TypeElement root = rootElement;

        while (root != null) {
            final String elementName = root.getQualifiedName().toString();
            if (StringUtils.equals(className, elementName)) {
                return true;
            }

            final List<? extends TypeMirror> interfaces = root.getInterfaces();
            for (final TypeMirror interfaceMirror : interfaces) {
                final String interfaceName = interfaceMirror.toString();
                if (StringUtils.equals(className, interfaceName)) {
                    return true;
                }
            }

            root = TypeUtils.getSuperclass(root);
        }

        return false;
    }

}
