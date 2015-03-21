package com.github.vbauer.jackdaw.util;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Vladislav Bauer
 */

public final class ServiceLoaderUtils {

    private ServiceLoaderUtils() {
        throw new UnsupportedOperationException();
    }


    public static <T> Collection<T> load(final Class<T> beanClass) {
        final ClassLoader classLoader = ServiceLoaderUtils.class.getClassLoader();
        final ServiceLoader<T> loader = ServiceLoader.load(beanClass, classLoader);
        final Iterator<T> iterator = loader.iterator();
        return Lists.newArrayList(iterator);
    }

}
