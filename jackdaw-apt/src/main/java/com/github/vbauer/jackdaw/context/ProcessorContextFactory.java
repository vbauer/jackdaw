package com.github.vbauer.jackdaw.context;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav Bauer
 */

public final class ProcessorContextFactory {

    private static final String OPT_ADD_SUPPRESS_WARNINGS_ANNOTATION = "addSuppressWarningsAnnotation";
    private static final String OPT_ADD_GENERATED_ANNOTATION = "addGeneratedAnnotation";
    private static final String OPT_ADD_GENERATED_DATE = "addGeneratedDate";

    private static final boolean DEF_ADD_SUPPRESS_WARNINGS_ANNOTATION = true;
    private static final boolean DEF_ADD_GENERATED_ANNOTATION = true;
    private static final boolean DEF_ADD_GENERATED_DATE = false;


    private ProcessorContextFactory() {
        throw new UnsupportedOperationException();
    }


    public static Set<String> getSupportedOptions() {
        return ImmutableSet.of(
            OPT_ADD_SUPPRESS_WARNINGS_ANNOTATION,
            OPT_ADD_GENERATED_ANNOTATION,
            OPT_ADD_GENERATED_DATE
        );
    }

    public static ProcessorContext create(final ProcessingEnvironment env) {
        final Map<String, String> options = env.getOptions();

        return new ProcessorContext(env)
            .setAddSuppressWarningsAnnotation(
                getBool(
                    options,
                    OPT_ADD_SUPPRESS_WARNINGS_ANNOTATION,
                    DEF_ADD_SUPPRESS_WARNINGS_ANNOTATION
                )
            )
            .setAddGeneratedAnnotation(
                getBool(options, OPT_ADD_GENERATED_ANNOTATION, DEF_ADD_GENERATED_ANNOTATION)
            )
            .setAddGeneratedDate(
                getBool(options, OPT_ADD_GENERATED_DATE, DEF_ADD_GENERATED_DATE)
            );
    }


    private static boolean getBool(
        final Map<String, String> options, final String key, final boolean defaultValue
    ) {
        final String value = options.get(key);
        return StringUtils.isBlank(value) ? defaultValue : Boolean.parseBoolean(value);
    }

}
