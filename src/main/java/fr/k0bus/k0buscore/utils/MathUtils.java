package fr.k0bus.k0buscore.utils;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Utility class providing methods for mathematical operations and number simplification,
 * such as checking if a string can be parsed as an integer or double, and simplifying large numbers
 * by using suffixes (e.g., "k", "M", "G") for easier readability.
 */
public class MathUtils {

    /**
     * Checks if a given string can be parsed as an integer.
     *
     * @param s The string to check.
     * @return {@code true} if the string can be parsed as an integer, otherwise {@code false}.
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a given string can be parsed as a double.
     *
     * @param s The string to check.
     * @return {@code true} if the string can be parsed as a double, otherwise {@code false}.
     */
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    // Suffixes for large numbers
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private static final NavigableMap<Double, String> suffixes_d = new TreeMap<>();
    static {
        suffixes_d.put(1000.0, "k");
        suffixes_d.put(1000000.0, "M");
        suffixes_d.put(1000000000.0, "G");
        suffixes_d.put(1000000000000.0, "T");
        suffixes_d.put(1000000000000000.0, "P");
        suffixes_d.put(1000000000000000000.0, "E");
    }

    /**
     * Simplifies a large number by appending an appropriate suffix (k, M, G, etc.).
     * For example, 1,500 will be simplified to "1.5k".
     *
     * @param value The long value to simplify.
     * @return A string representing the simplified value with the appropriate suffix.
     */
    public static String simplify(long value) {
        // Adjust for Long.MIN_VALUE since it's an edge case
        if (value == Long.MIN_VALUE) return simplify(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + simplify(-value);
        if (value < 1000) return Long.toString(value); // Deal with small numbers

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); // The number part times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / (double)10);
        double finalValue = hasDecimal ? (truncated / 10d) : (truncated / (double)10);
        finalValue = java.lang.Math.round(finalValue * 100.0) / 100.0;
        return finalValue + suffix;
    }

    /**
     * Simplifies a large number by appending an appropriate suffix (k, M, G, etc.).
     * For example, 1,500.75 will be simplified to "1.5k".
     *
     * @param value The double value to simplify.
     * @return A string representing the simplified value with the appropriate suffix.
     */
    public static String simplify(double value) {
        // Adjust for Long.MIN_VALUE since it's an edge case
        if (value == Long.MIN_VALUE) return simplify(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + simplify(-value);
        if (value < 1000) return Double.toString(value); // Deal with small numbers

        Map.Entry<Double, String> e = suffixes_d.floorEntry(value);
        Double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10); // The number part times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        double finalValue = hasDecimal ? (truncated / 10d) : (truncated / 10);
        finalValue = java.lang.Math.round(finalValue * 100.0) / 100.0;
        return finalValue + suffix;
    }
}
