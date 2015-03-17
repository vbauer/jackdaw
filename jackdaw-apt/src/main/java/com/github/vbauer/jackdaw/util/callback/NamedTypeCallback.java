package com.github.vbauer.jackdaw.util.callback;

import javax.lang.model.element.TypeElement;

/**
 * @author Vladislav Bauer
 */

public interface NamedTypeCallback {

    void process(String methodName, TypeElement type);

}
