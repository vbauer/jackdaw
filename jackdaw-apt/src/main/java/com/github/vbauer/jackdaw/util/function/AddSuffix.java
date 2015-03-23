package com.github.vbauer.jackdaw.util.function;

import com.google.common.base.Function;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vladislav Bauer
 */

public class AddSuffix implements Function<String, String> {

    private final String suffix;


    public AddSuffix(final String suffix) {
        this.suffix = StringUtils.trimToEmpty(suffix);
    }


    @Override
    public String apply(final String input) {
        return input + suffix;
    }

}
