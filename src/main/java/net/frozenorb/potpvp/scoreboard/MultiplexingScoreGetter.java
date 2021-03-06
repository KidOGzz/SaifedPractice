package net.frozenorb.potpvp.scoreboard;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.MatchHandler;
import net.frozenorb.potpvp.setting.Setting;
import net.frozenorb.potpvp.setting.SettingHandler;
import zz.kidog.oglib.scoreboard.ScoreGetter;

import zz.kidog.oglib.util.LinkedList;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

final class MultiplexingScoreGetter implements ScoreGetter {

    private final BiConsumer<Player, List<String>> lobbyScoreGetter;
    private final BiConsumer<Player, List<String>> matchScoreGetter;

    MultiplexingScoreGetter(
        BiConsumer<Player, List<String>> lobbyScoreGetter,
        BiConsumer<Player, List<String>> matchScoreGetter
    ) {
        this.lobbyScoreGetter = lobbyScoreGetter;
        this.matchScoreGetter = matchScoreGetter;
    }

    @Override
    public void getScores(LinkedList<String> scores, Player player) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();
        SettingHandler settingHandler = PotPvPSI.getInstance().getSettingHandler();
        scores.add("&a&7&m--------------------");

        if (settingHandler.getSetting(player, Setting.SHOW_SCOREBOARD)) {
            if (matchHandler.isPlayingOrSpectatingMatch(player)) {
                matchScoreGetter.accept(player, scores);
            } else {
                lobbyScoreGetter.accept(player, scores);
            }
        }

        scores.add("&f&7&m--------------------");
    }

}