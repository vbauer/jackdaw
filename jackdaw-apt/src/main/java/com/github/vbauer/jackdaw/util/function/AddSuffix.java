package com.github.vbauer.jackdaw.util.function;

import com.google.common.base.Function;

/**
 * @author Vladislav Bauer
 */

public class AddSuffix implements Function<String, String> {

    private final String suffix;


    public AddSuffix(final String suffix) {
        this.suffix = suffix;
    }


    @Override
    public String apply(final String input) {
        return input + suffix;
    }

}
