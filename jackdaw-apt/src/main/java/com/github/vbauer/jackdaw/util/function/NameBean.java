package com.github.vbauer.jackdaw.util.function;

import com.google.common.base.Function;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vladislav Bauer
 */

public class NameBean implements Function<String, String> {

    private static final String BEAN = "Bean";
    private static final String MODEL = "Model";
    private static final String ABSTRACT = "Abstract";


    @Override
    public String apply(final String input) {
        final String name = StringUtils.removeStart(StringUtils.removeEnd(input, MODEL), ABSTRACT);
        return StringUtils.equals(name, input) ? input + BEAN : name;
    }

}
