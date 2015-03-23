package com.github.vbauer.jackdaw.code.generator;

import com.github.vbauer.jackdaw.annotation.JService;
import com.github.vbauer.jackdaw.code.base.BaseCodeGenerator;
import com.github.vbauer.jackdaw.code.context.CodeGeneratorContext;
import com.github.vbauer.jackdaw.context.ProcessorContextHolder;
import com.github.vbauer.jackdaw.util.TypeUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static javax.tools.StandardLocation.CLASS_OUTPUT;

/**
 * @author Vladislav Bauer
 */

public class JServiceCodeGenerator extends BaseCodeGenerator {

    private static final String DIR_SERVICES = "META-INF/services/";

    private final Map<String, Set<String>> allServices = Maps.newTreeMap();


    @Override
    public Class<? extends Annotation> getAnnotation() {
        return JService.class;
    }

    @Override
    public void generate(final CodeGeneratorContext context) throws Exception {
        final TypeElement typeElement = context.getTypeElement();
        final JService annotation = typeElement.getAnnotation(JService.class);
        final String providerClass = getProviderClass(annotation);

        if (TypeUtils.inHierarchy(typeElement, providerClass)) {
            addProvider(typeElement, providerClass);
        }
    }

    @Override
    public void onStart() throws Exception {
        allServices.clear();
    }

    @Override
    public void onFinish() throws Exception {
        for (final Map.Entry<String, Set<String>> entry : allServices.entrySet()) {
            final String providerClass = entry.getKey();
            final Set<String> services = entry.getValue();
            final String resourceFile = DIR_SERVICES + providerClass;

            final ProcessingEnvironment env = ProcessorContextHolder.getProcessingEnvironment();
            final Filer filer = env.getFiler();

            writeServices(filer, resourceFile, services);
        }
    }


    private void addProvider(final TypeElement typeElement, final String providerClass) {
        final String serviceClass = typeElement.getQualifiedName().toString();
        Set<String> services = allServices.get(providerClass);
        if (services == null) {
            services = Sets.newTreeSet();
            allServices.put(providerClass, services);
        }
        services.add(serviceClass);
    }

    private String getProviderClass(final JService annotation) {
        try {
            final Class<?> providerClass = annotation.value();
            return providerClass.getCanonicalName();
        } catch (final MirroredTypeException ex) {
            final TypeMirror typeMirror = ex.getTypeMirror();
            return typeMirror.toString();
        }
    }

    private void writeServices(
        final Filer filer, final String resourceFile, final Collection<String> services
    ) throws Exception {
        final FileObject file = createResource(filer, resourceFile);
        final OutputStream outputStream = file.openOutputStream();
        try {
            IOUtils.writeLines(services, "\n", outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    private FileObject createResource(final Filer filer, final String path) throws Exception {
        return filer.createResource(CLASS_OUTPUT, StringUtils.EMPTY, path);
    }

}
