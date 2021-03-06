package net.frozenorb.potpvp.scoreboard;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.elo.EloHandler;
import net.frozenorb.potpvp.match.MatchHandler;
import net.frozenorb.potpvp.party.PartyHandler;
import net.frozenorb.potpvp.queue.MatchQueue;
import net.frozenorb.potpvp.queue.MatchQueueEntry;
import net.frozenorb.potpvp.queue.QueueHandler;
import zz.kidog.oglib.autoreboot.AutoRebootHandler;
import zz.kidog.oglib.util.TimeUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

final class LobbyScoreGetter implements BiConsumer<Player, List<String>> {

    @Override
    public void accept(Player player, List<String> scores) {
        Optional<UUID> followingOpt = PotPvPSI.getInstance().getFollowHandler().getFollowing(player);
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        QueueHandler queueHandler = PotPvPSI.getInstance().getQueueHandler();
        EloHandler eloHandler = PotPvPSI.getInstance().getEloHandler();

        if (partyHandler.hasParty(player)) {
            int size = partyHandler.getParty(player).getMembers().size();
            scores.add(PotPvPSI.getInstance().getConfig().getString("SCOREBOARD.party") + size);
        }

        scores.add(PotPvPSI.getInstance().getConfig().getString("SCOREBOARD.online") + Bukkit.getOnlinePlayers().size());
        scores.add(PotPvPSI.getInstance().getConfig().getString("SCOREBOARD.in-fight") + matchHandler.countPlayersPlayingInProgressMatches());
        scores.add(PotPvPSI.getInstance().getConfig().getString("SCOREBOARD.in-queue") + queueHandler.getQueuedCount());

        // this definitely can be a .ifPresent, however creating the new lambda that often
        // was causing some performance issues, so we do this less pretty (but more efficent)
        // check (we can't define the lambda up top and reference because we reference the
        // scores variable)
        if (followingOpt.isPresent()) {
            Player following = Bukkit.getPlayer(followingOpt.get());
            scores.add("&5Following: *&7" + following.getName());

            if (player.hasPermission("practice.staff")) {
                MatchQueueEntry targetEntry = getQueueEntry(following);

                if (targetEntry != null) {
                    MatchQueue queue = targetEntry.getQueue();

                    scores.add("&5Target queue:");
                    scores.add("&7" + (queue.isRanked() ? "Ranked" : "Unranked") + " " + queue.getKitType().getDisplayName());
                }
            }
        }

        MatchQueueEntry entry = getQueueEntry(player);

        if (entry != null) {
            String waitTimeFormatted = TimeUtils.formatIntoMMSS(entry.getWaitSeconds());
            MatchQueue queue = entry.getQueue();

            scores.add("&b&7&m--------------------");
            scores.add("&7" + queue.getKitType().getDisplayColor() + (queue.isRanked() ? "Ranked" : "Unranked") + " " + queue.getKitType().getDisplayName());
            scores.add("&5Time: *&7" + waitTimeFormatted);

            if (queue.isRanked()) {
                int elo = eloHandler.getElo(entry.getMembers(), queue.getKitType());
                int window = entry.getWaitSeconds() * QueueHandler.RANKED_WINDOW_GROWTH_PER_SECOND;

                scores.add("&5Search range: *&7" + Math.max(0, elo - window) + " - " + (elo + window));
            }
        }

        if (AutoRebootHandler.isRebooting()) {
            String secondsStr = TimeUtils.formatIntoMMSS(AutoRebootHandler.getRebootSecondsRemaining());
            scores.add("&c&lRebooting: &c" + secondsStr);
        }

        if (player.hasMetadata("ModMode")) {
            scores.add(ChatColor.GRAY.toString() + ChatColor.BOLD + "In Silent Mode");
        }
    }

    private MatchQueueEntry getQueueEntry(Player player) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        QueueHandler queueHandler = PotPvPSI.getInstance().getQueueHandler();

        if (partyHandler.hasParty(player)) {
            return queueHandler.getQueueEntry(partyHandler.getParty(player));
        } else {
            return queueHandler.getQueueEntry(player.getUniqueId());
        }
    }

}