package com.github.vbauer.jackdaw.util.callback;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author Vladislav Bauer
 */

public interface SimpleProcessorCallback<T extends Annotation> {

    void process(TypeElement type, String methodName, T annotation);

}
