package fr.k0bus.k0buscore.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for string manipulation and formatting.
 * This class provides methods for color code parsing, proper casing of words,
 * and compatibility with Minecraft's color formatting features.
 * <p>
 * It supports both legacy color codes (using '&amp;') and modern hex color codes (using '&amp;&#38;' syntax).
 * Additionally, it includes a utility method for converting strings to "proper" case.
 */
public class StringUtils {

    // Pattern for matching hex color codes (e.g. &#12345abc)
    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

    /**
     * Parses a string to apply color codes and placeholders.
     * This method first translates color codes in the string, then processes
     * any placeholders using a custom parser.
     *
     * @param s The input string to parse.
     * @return The parsed string with color codes and placeholders.
     */
    public static String parse(String s) {
        return Utils.PAPIParse(translateColor(s));
    }

    /**
     * Translates color codes in the string, supporting both legacy color codes
     * (using '&amp;') and modern hex color codes (using '&amp;&amp;' or '&amp;&#38;').
     *
     * @param s The input string to process for color codes.
     * @return The string with color codes applied.
     */
    public static String translateColor(String s) {

        // If the Minecraft version is older than 1.16, only process legacy color codes
        if (VersionUtils.getMCVersion() < 16) {
            return ChatColor.translateAlternateColorCodes('&', s);
        }

        // Match and replace hex color codes (e.g., &#12345abc)
        Matcher matcher = HEX_PATTERN.matcher(s);
        StringBuilder buffer = new StringBuilder();

        while(matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        // Translate legacy color codes (&) after replacing hex codes
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    /**
     * Converts a string to proper case, where the first letter of each word is capitalized,
     * and underscores are replaced with spaces.
     *
     * @param str The input string to convert to proper case.
     * @return The input string in proper case with underscores replaced by spaces.
     */
    public static String proper(String str) {
        // Replace underscores with spaces
        str = str.replace("_", " ");
        String[] strings = str.split(" ");
        StringBuilder finalString = new StringBuilder();

        // Capitalize the first letter of each word
        for (String s : strings) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            if (!finalString.toString().isEmpty()) finalString.append(" ");
            finalString.append(s);
        }
        return finalString.toString();
    }
}
