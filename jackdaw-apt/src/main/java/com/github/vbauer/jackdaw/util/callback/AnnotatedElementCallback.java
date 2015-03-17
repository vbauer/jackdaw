package com.github.vbauer.jackdaw.util.callback;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/**
 * @author Vladislav Bauer
 */

public interface AnnotatedElementCallback<T extends Annotation> {

    void process(Element element, T annotation) throws Exception;

}
