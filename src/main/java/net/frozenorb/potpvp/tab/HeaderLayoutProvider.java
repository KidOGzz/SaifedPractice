package net.frozenorb.potpvp.tab;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.MatchHandler;
import net.frozenorb.potpvp.util.CC;
import zz.kidog.oglib.tab.TabLayout;
import zz.kidog.oglib.util.PlayerUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

final class HeaderLayoutProvider implements BiConsumer<Player, TabLayout> {

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        header: {
            tabLayout.set(1, 0, CC.translate(PotPvPSI.getInstance().getConfig().getString("TABLIST.header")));
        }

        status: {
            tabLayout.set(0, 1, CC.translate(PotPvPSI.getInstance().getConfig().getString("TABLIST.online")) + Bukkit.getOnlinePlayers().size());
            tabLayout.set(1, 1, CC.translate(PotPvPSI.getInstance().getConfig().getString("TABLIST.ping")) + PlayerUtils.getPing(player));
            tabLayout.set(2, 1, CC.translate(PotPvPSI.getInstance().getConfig().getString("TABLIST.in-fights")) + matchHandler.countPlayersPlayingInProgressMatches());
        }
    }

}