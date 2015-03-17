package com.github.vbauer.jackdaw.util;

import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * @author Vladislav Bauer
 */

public final class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }


    @SuppressWarnings("unchecked")
    public static <T> T readField(final Object object, final String name) throws Exception {
        return (T) FieldUtils.readField(object, name, true);
    }

}
