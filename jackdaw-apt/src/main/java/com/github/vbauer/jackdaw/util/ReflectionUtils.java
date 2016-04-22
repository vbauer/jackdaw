package com.github.vbauer.jackdaw.util;

import org.apache.commons.lang3.ClassUtils;
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

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(final String name) throws ClassNotFoundException {
        try {
            return (Class<T>) ClassUtils.getClass(name);
        } catch (final Exception ex) {
            return (Class<T>) Class.forName(name);
        }
    }

}
