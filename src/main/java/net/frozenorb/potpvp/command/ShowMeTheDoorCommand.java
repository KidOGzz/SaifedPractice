package net.frozenorb.potpvp.command;

import com.google.common.collect.ImmutableList;

import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.ogLib;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class ShowMeTheDoorCommand {

    private static final List<String> reasons = ImmutableList.of(
        "Here's the door.",
        "Go cry about it.",
        "Later, skater!"
    );

    @Command(names = {"showmethedoor"}, permission = "")
    public static void showmethedoor(Player sender) {
        String reason = reasons.get(ogLib.RANDOM.nextInt(reasons.size()));
        sender.kickPlayer(ChatColor.YELLOW + reason);
    }

}
