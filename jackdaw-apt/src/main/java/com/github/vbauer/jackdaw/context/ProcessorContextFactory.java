package com.github.vbauer.jackdaw.context;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;

import static com.github.vbauer.jackdaw.JackdawProcessor.ADD_GENERATED_ANNOTATION;
import static com.github.vbauer.jackdaw.JackdawProcessor.ADD_GENERATED_DATE;
import static com.github.vbauer.jackdaw.JackdawProcessor.ADD_SUPPRESS_WARNINGS_ANNOTATION;

/**
 * @author Vladislav Bauer
 */

public final class ProcessorContextFactory {

    private ProcessorContextFactory() {
        throw new UnsupportedOperationException();
    }


    public static ProcessorContext create(final ProcessingEnvironment env) {
        return new ProcessorContext(env)
            .setAddSuppressWarningsAnnotation(getBool(env, ADD_SUPPRESS_WARNINGS_ANNOTATION, true))
            .setAddGeneratedAnnotation(getBool(env, ADD_GENERATED_ANNOTATION, true))
            .setAddGeneratedDate(getBool(env, ADD_GENERATED_DATE, false));
    }


    private static boolean getBool(
        final ProcessingEnvironment processingEnv, final String key, final boolean defaultValue
    ) {
        final Map<String, String> options = processingEnv.getOptions();
        final String value = options.get(key);
        return StringUtils.isBlank(value) ? defaultValue : Boolean.parseBoolean(value);
    }

}
