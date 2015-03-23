package com.github.vbauer.jackdaw.util;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.Format;
import java.util.Collection;
import java.util.Date;

/**
 * @author Vladislav Bauer
 */

public final class DateTimeUtils {

    public static final String FORMAT_DD_MM_YYYY_1 = "dd-MM-yyyy";
    public static final String FORMAT_DD_MM_YYYY_2 = "dd/MM/yyyy";
    public static final String FORMAT_YYYY_MM_DD_1 = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_2 = "yyyy/MM/dd";

    public static final String[] DATE_FORMATS = {
        FORMAT_DD_MM_YYYY_1,
        FORMAT_DD_MM_YYYY_2,
        FORMAT_YYYY_MM_DD_1,
        FORMAT_YYYY_MM_DD_2,
    };


    private DateTimeUtils() {
        throw new UnsupportedOperationException();
    }


    public static Collection<Format> createDateFormats(final String... formats) {
        final ImmutableList.Builder<Format> builder = ImmutableList.builder();
        for (final String format : formats) {
            final FastDateFormat instance = FastDateFormat.getInstance(format);
            builder.add(instance);
        }
        return builder.build();
    }

    public static Date parseDate(final String rawDate, final Collection<Format> formats) {
        final String date = StringUtils.trimToNull(rawDate);
        if (date != null) {
            for (final Format format : formats) {
                final Date parsedDate = parseDate(date, format);
                if (parsedDate != null) {
                    return parsedDate;
                }
            }
        }
        return null;
    }

    public static Date parseDate(final String date, final Format format) {
        try {
            return (Date) format.parseObject(date);
        } catch (final Exception ignored) {
            return null;
        }
    }

}
