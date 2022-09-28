package fr.k0bus.k0buscore.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

    public static String translateColor(String s) {

        if (VersionUtils.getMCVersion() < 16) {
            return ChatColor.translateAlternateColorCodes('&', s);
        }

        Matcher matcher = HEX_PATTERN.matcher(s);
        StringBuffer buffer = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static String proper(String str)
    {
        String[] strings = str.split(" ");
        String finalString = "";
        for (String s:strings) {
            s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
            if(finalString != "") finalString = finalString + " ";
            finalString = finalString+s;
        }
        return finalString;
    }
}
