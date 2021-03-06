package net.frozenorb.potpvp.queue;

import zz.kidog.oglib.util.ItemUtils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static net.frozenorb.potpvp.PotPvPLang.LEFT_ARROW;
import static net.frozenorb.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

@UtilityClass
public final class QueueItems {

    public static final ItemStack JOIN_SOLO_UNRANKED_QUEUE_ITEM = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack LEAVE_SOLO_UNRANKED_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (byte) DyeColor.RED.getDyeData());

    public static final ItemStack JOIN_SOLO_RANKED_QUEUE_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack LEAVE_SOLO_RANKED_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (byte) DyeColor.RED.getDyeData());

    public static final ItemStack JOIN_PARTY_UNRANKED_QUEUE_ITEM = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack LEAVE_PARTY_UNRANKED_QUEUE_ITEM = new ItemStack(Material.ARROW);

    public static final ItemStack JOIN_PARTY_RANKED_QUEUE_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack LEAVE_PARTY_RANKED_QUEUE_ITEM = new ItemStack(Material.ARROW);

    static {
        ItemUtils.setDisplayName(JOIN_SOLO_UNRANKED_QUEUE_ITEM, LEFT_ARROW + AQUA.toString() + BOLD + "Join Unranked Queue" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_SOLO_UNRANKED_QUEUE_ITEM, LEFT_ARROW + RED.toString() + BOLD + "Leave Unranked Queue" + RIGHT_ARROW);

        ItemUtils.setDisplayName(JOIN_SOLO_RANKED_QUEUE_ITEM, LEFT_ARROW + GREEN.toString() + BOLD + "Join " + AQUA.toString() + BOLD + "Ranked" + GREEN.toString() + BOLD + " Queue" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_SOLO_RANKED_QUEUE_ITEM, LEFT_ARROW + RED.toString() + BOLD + "Leave " + AQUA.toString() + BOLD + "Ranked" + RED.toString() + BOLD + " Queue" + RIGHT_ARROW);

        ItemUtils.setDisplayName(JOIN_PARTY_UNRANKED_QUEUE_ITEM, LEFT_ARROW + GREEN.toString() + BOLD + "Join 2v2 Unranked" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_PARTY_UNRANKED_QUEUE_ITEM, LEFT_ARROW + RED.toString() + BOLD + "Leave 2v2 Unranked" + RIGHT_ARROW);

        ItemUtils.setDisplayName(JOIN_PARTY_RANKED_QUEUE_ITEM, LEFT_ARROW + GREEN.toString() + BOLD + "Join 2v2 Ranked" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_PARTY_RANKED_QUEUE_ITEM, LEFT_ARROW + RED.toString() + BOLD + "Leave 2v2 Ranked" + RIGHT_ARROW);
    }

}