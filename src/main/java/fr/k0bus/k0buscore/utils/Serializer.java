package fr.k0bus.k0buscore.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A utility class for serializing and deserializing collections of data.
 * This class provides methods to process lists containing integers or strings
 * and convert them into an array of integers.
 */
public class Serializer {

    /**
     * Reads a list of objects and converts it into an array of integers.
     * <p>
     * The method processes each element in the list:
     * <ul>
     *     <li>If the element is an Integer, it adds it directly to the result array.</li>
     *     <li>If the element is a String in the format "start-end", it adds all integers
     *         from "start" to "end" (inclusive) to the result array.</li>
     * </ul>
     *
     * <b>Example:</b>
     * If the input list is `["1-3", 5, "7-9"]`, the method will return an array
     * containing `[1, 2, 3, 5, 7, 8, 9]`.
     *
     * @param objectList A list of objects, each of which can either be an Integer or a String
     *                  in the format "start-end" (inclusive range).
     * @return An array of integers containing all the integers parsed from the input list.
     *         The integers from ranges are added to the result array in order.
     * @throws NumberFormatException If the string representation of a number is not valid.
     */
    public static int[] readIntArray(List<?> objectList) {
        int i = 0;
        List<Integer> values = new ArrayList<>();

        // Process each item in the objectList
        for (Object o : objectList) {
            // If the object is an Integer, add it directly to values
            if (o instanceof Integer) {
                values.add((int) o);
            }
            // If the object is a String in the form "start-end"
            else if (o instanceof String) {
                String[] args = ((String) o).split("-");
                if (args.length >= 2) {
                    try {
                        int n0 = Integer.parseInt(args[0]);
                        int n1 = Integer.parseInt(args[1]) + 1; // Add 1 to include the upper bound
                        // Add all integers in the range [n0, n1)
                        for (int n : IntStream.range(n0, n1).toArray()) {
                            values.add(n);
                            i++;
                        }
                        continue;
                    } catch (NumberFormatException e) {
                        // Handle invalid number format if necessary
                        throw new NumberFormatException("Invalid number format in range string: " + o);
                    }
                }
            }
            i++;
        }

        // Convert List<Integer> to a primitive int array and return it
        return values.stream().mapToInt(Integer::intValue).toArray();
    }
}
