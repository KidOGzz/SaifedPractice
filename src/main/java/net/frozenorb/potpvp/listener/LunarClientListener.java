package net.frozenorb.potpvp.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import me.activated.core.plugin.AquaCoreAPI;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class LunarClientListener implements Listener {

    private static int number;

    public void updateNameTag(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(PotPvPSI.getInstance(), () -> LunarClientAPI.getInstance().overrideNametag(onlinePlayer, resetNameTag(onlinePlayer), player), 0L, 60L);
        }
    }

    public List<String> resetNameTag(Player player) {
        String nameTag = (CC.translate("&7[" + AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getDisplayName() + "&7] " + AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + player.getDisplayName()));
        String nameTagMod = (CC.translate("&7[&8Staff&7]"));
        List<String> tag = new ArrayList<>();
        boolean mod = AquaCoreAPI.INSTANCE.getPlayerData(player.getUniqueId()).hasPermission(PotPvPSI.getInstance().getConfig().getString("LunarAPI.staff-permission"));

        if (mod){
            tag.add(CC.translate(nameTagMod));
        }
        tag.add(CC.translate(nameTag));
        return tag;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getServer().getScheduler().runTaskLater(PotPvPSI.getInstance(), () -> {
            this.updateNameTag(player);
        }, 20L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Bukkit.getServer().getScheduler().cancelTask(number);
    }
}