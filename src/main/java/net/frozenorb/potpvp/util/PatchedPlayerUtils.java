package net.frozenorb.potpvp.util;

import zz.kidog.oglib.util.UUIDUtils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class PatchedPlayerUtils {

    public static void resetInventory(Player player) {
        resetInventory(player, null);
    }

    public static void resetInventory(Player player, GameMode gameMode) {
        resetInventory(player, gameMode, false);
    }

    public static void resetInventory(Player player, GameMode gameMode, boolean skipInvReset) {
        player.setHealth(player.getMaxHealth());
        player.setFallDistance(0F);
        player.setFoodLevel(20);
        player.setSaturation(10F);
        player.setLevel(0);
        player.setExp(0F);

        if (!skipInvReset) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        player.setFireTicks(0);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        if (gameMode != null && player.getGameMode() != gameMode) {
            player.setGameMode(gameMode);
        }

        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public static List<String> mapToNames(Collection<UUID> uuids) {
        return uuids.stream().map(UUIDUtils::name).collect(Collectors.toList());
    }

}