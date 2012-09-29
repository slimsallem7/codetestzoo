package org.bookmark.helper;

import java.text.DecimalFormat;

public class FormatterCore {
    private final static DecimalFormat formatter = new DecimalFormat("#,###,###.###");
    private final static DecimalFormat numberFormatter = new DecimalFormat("#########.###");

    public static String decimal(final double input) {
        return FormatterCore.formatter.format(input);
    }

    public static String format(final double input) {
        return FormatterCore.formatter.format(input);
    }

    public static String numberFormat(final double input) {
        String result = FormatterCore.numberFormatter.format(input);
        result = result.replace(",", ".");
        return result;
    }
}
