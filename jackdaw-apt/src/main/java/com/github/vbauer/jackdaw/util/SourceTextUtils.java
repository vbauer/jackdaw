package com.github.vbauer.jackdaw.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Vladislav Bauer
 */
public final class SourceTextUtils {

    public static final String INDENT = "    ";
    public static final String BRACKET_OPEN = "{";
    public static final String BRACKET_CLOSE = "}";

    public static final String PACKAGE_SEPARATOR = ".";


    private SourceTextUtils() {
        throw new UnsupportedOperationException();
    }


    public static String indent(final int n) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append(INDENT);
        }
        return result.toString();
    }

    public static String lines(final String... lines) {
        final StringBuilder result = new StringBuilder();
        final int length = ArrayUtils.getLength(lines);
        int indent = 0;

        for (int i = 0; i < length; i++) {
            final String line = lines[i];

            if (line.startsWith(BRACKET_CLOSE)) {
                indent--;
            }

            result.append(indent(indent)).append(line);
            if (length - 1 != i) {
                result.append(System.lineSeparator());
            }

            if (line.endsWith(BRACKET_OPEN)) {
                indent++;
            }
        }
        return result.toString();
    }

}
