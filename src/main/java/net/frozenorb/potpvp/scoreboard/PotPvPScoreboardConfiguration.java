package net.frozenorb.potpvp.scoreboard;

import net.frozenorb.potpvp.PotPvPSI;
import zz.kidog.oglib.scoreboard.ScoreboardConfiguration;
import zz.kidog.oglib.scoreboard.TitleGetter;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PotPvPScoreboardConfiguration {

    private static final String SCOREBOARD_TITLE = PotPvPSI.getInstance().getConfig().getString("SCOREBOARD.tittle");

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(TitleGetter.forStaticString(SCOREBOARD_TITLE));
        configuration.setScoreGetter(new MultiplexingScoreGetter(
            new LobbyScoreGetter(),
            new MatchScoreGetter()
        ));

        return configuration;
    }

}