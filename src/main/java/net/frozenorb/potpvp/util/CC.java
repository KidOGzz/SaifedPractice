package net.frozenorb.potpvp.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CC {
    public static String translate(final String in) {
    return ChatColor.translateAlternateColorCodes('&', in);
}

    public static List<String> translate(final List<String> lines) {
        final List<String> toReturn = new ArrayList<String>();
        for (final String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return toReturn;
    }

    public static List<String> lore(List<String> input) {
        return (List)input.stream().map(CC::set).collect(Collectors.toList());
    }

    public static String NO_CONSOLE = CC.translate("&cNo Console!");
    public static String set(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
    public static void log(String in) {
        Bukkit.getConsoleSender().sendMessage(translate(in));
    }

}
