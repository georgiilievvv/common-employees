package com.sirma.commonProjects.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Util class for date calculations and parsing.
 */
public class DateUtil {

    private static final String NULL_VALUE = "NULL";

    public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalStateException("start date is after end date");
        }
    }
    public static boolean hasOverlappingDateRanges(LocalDate s1, LocalDate e1, LocalDate s2, LocalDate e2) {
        return (s1.isBefore(s2) && e1.isAfter(s2)) ||
                (s1.isBefore(e2) && e1.isAfter(e2)) ||
                (s1.isBefore(s2) && e1.isAfter(e2)) ||
                (s1.isAfter(s2) && e1.isBefore(e2)) ||
                s1.isEqual(s2) || e1.isEqual(e2) ||
                s1.isEqual(e2) || e1.isEqual(s2);
    }

    public static LocalDate parseDateFromString(String input, String dateTimeFormatter) {
        if (NULL_VALUE.equalsIgnoreCase(input)) {
            return LocalDate.now();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        return LocalDate.parse(input, formatter);
    }
}
