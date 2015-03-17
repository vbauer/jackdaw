package com.github.vbauer.jackdaw.util;

import com.github.vbauer.jackdaw.context.ProcessorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author Vladislav Bauer
 */

public final class ProcessorUtils {


    private ProcessorUtils() {
        throw new UnsupportedOperationException();
    }


    public static JavaFileObject createSourceFile(
        final TypeElement baseElement, final String packageName, final String className
    ) throws Exception {
        final ProcessingEnvironment processingEnv = getProcessingEnvironment();
        final Filer filer = processingEnv.getFiler();

        return filer.createSourceFile(packageName + SourceCodeUtils.PACKAGE_SEPARATOR + className, baseElement);
    }

    public static String packageName(final TypeElement element) {
        final ProcessingEnvironment processingEnv = getProcessingEnvironment();
        final Elements elementUtils = processingEnv.getElementUtils();
        final PackageElement packageElement = elementUtils.getPackageOf(element);
        return packageElement.getQualifiedName().toString();
    }

    public static TypeElement getWrappedType(final TypeMirror mirror) {
        final ProcessingEnvironment processingEnv = getProcessingEnvironment();
        final Types typeUtils = processingEnv.getTypeUtils();

        final TypeKind kind = mirror.getKind();
        final boolean primitive = kind.isPrimitive();

        if (primitive) {
            return typeUtils.boxedClass((PrimitiveType) mirror);
        }
        return (TypeElement) typeUtils.asElement(mirror);
    }

    public static void message(final Diagnostic.Kind type, final String message) {
        final ProcessingEnvironment processingEnv = getProcessingEnvironment();
        final Messager messager = processingEnv.getMessager();
        messager.printMessage(type, message);
    }


    private static ProcessingEnvironment getProcessingEnvironment() {
        final ProcessorContext processorContext = ProcessorContextHolder.getContext();
        return processorContext.getProcessingEnvironment();
    }

}
