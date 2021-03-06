package net.frozenorb.potpvp;

import com.comphenix.protocol.ProtocolLibrary;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.duel.DuelHandler;
import net.frozenorb.potpvp.elo.EloHandler;
import net.frozenorb.potpvp.event.EventHandler;
import net.frozenorb.potpvp.event.EventType;
import net.frozenorb.potpvp.event.EventTypeParameterType;
import net.frozenorb.potpvp.follow.FollowHandler;
import net.frozenorb.potpvp.kit.KitHandler;
import net.frozenorb.potpvp.kit.KitManager;
import net.frozenorb.potpvp.kittype.KitType;
import net.frozenorb.potpvp.kittype.KitTypeJsonAdapter;
import net.frozenorb.potpvp.kittype.KitTypeParameterType;
import net.frozenorb.potpvp.listener.*;
import net.frozenorb.potpvp.lobby.LobbyHandler;
import net.frozenorb.potpvp.match.MatchHandler;
import net.frozenorb.potpvp.nametag.PotPvPNametagProvider;
import net.frozenorb.potpvp.party.PartyHandler;
import net.frozenorb.potpvp.postmatchinv.PostMatchInvHandler;
import net.frozenorb.potpvp.protocol.DisableSpectatorSoundAdapter;
import net.frozenorb.potpvp.queue.QueueHandler;
import net.frozenorb.potpvp.rematch.RematchHandler;
import net.frozenorb.potpvp.scoreboard.PotPvPScoreboardConfiguration;
import net.frozenorb.potpvp.setting.SettingHandler;
import net.frozenorb.potpvp.tab.PotPvPLayoutProvider;
import net.frozenorb.potpvp.util.CC;
import zz.kidog.oglib.command.FrozenCommandHandler;
import zz.kidog.oglib.nametag.FrozenNametagHandler;
import zz.kidog.oglib.scoreboard.FrozenScoreboardHandler;
import zz.kidog.oglib.serialization.BlockVectorAdapter;
import zz.kidog.oglib.serialization.ItemStackAdapter;
import zz.kidog.oglib.serialization.LocationAdapter;
import zz.kidog.oglib.serialization.PotionEffectAdapter;
import zz.kidog.oglib.serialization.VectorAdapter;
import zz.kidog.oglib.tab.FrozenTabHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import lombok.Getter;

public final class PotPvPSI extends JavaPlugin {

    @Getter private static PotPvPSI instance;
    @Getter private static Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
        .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
        .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
        .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
        .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
        .registerTypeHierarchyAdapter(KitType.class, new KitTypeJsonAdapter()) // custom KitType serializer
        .serializeNulls()
        .create();

    private MongoClient mongoClient;
    @Getter private MongoDatabase mongoDatabase;

    @Getter private SettingHandler settingHandler;
    @Getter private DuelHandler duelHandler;
    @Getter private KitHandler kitHandler;
    @Getter private LobbyHandler lobbyHandler;
    @Getter private ArenaHandler arenaHandler;
    @Getter private MatchHandler matchHandler;
    @Getter private PartyHandler partyHandler;
    @Getter private QueueHandler queueHandler;
    @Getter private RematchHandler rematchHandler;
    @Getter private PostMatchInvHandler postMatchInvHandler;
    @Getter private FollowHandler followHandler;
    @Getter private EloHandler eloHandler;
    @Getter private EventHandler eventHandler;

    @Override
    public void onEnable() {
        CC.log("&avPotPvP &bAquaCore &dFork");
        CC.log("&bAUTHOR&7: &eKidOGzz");
        instance = this;
        saveDefaultConfig();

        setupMongo();

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }

        settingHandler = new SettingHandler();
        duelHandler = new DuelHandler();
        kitHandler = new KitHandler();
        lobbyHandler = new LobbyHandler();
        arenaHandler = new ArenaHandler();
        matchHandler = new MatchHandler();
        partyHandler = new PartyHandler();
        queueHandler = new QueueHandler();
        rematchHandler = new RematchHandler();
        postMatchInvHandler = new PostMatchInvHandler();
        followHandler = new FollowHandler();
        eloHandler = new EloHandler();
        eventHandler = new EventHandler();

        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new BowHealthListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new ChatToggleListener(), this);
        getServer().getPluginManager().registerEvents(new NightModeListener(), this);
        getServer().getPluginManager().registerEvents(new PearlCooldownListener(), this);
        getServer().getPluginManager().registerEvents(new RankedMatchQualificationListener(), this);
        getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new DisableSpectatorSoundAdapter());

        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerParameterType(EventType.class, new EventTypeParameterType());
        FrozenCommandHandler.registerParameterType(KitType.class, new KitTypeParameterType());
        FrozenTabHandler.setLayoutProvider(new PotPvPLayoutProvider());
        FrozenNametagHandler.registerProvider(new PotPvPNametagProvider());
        if (PotPvPSI.getInstance().getConfig().getBoolean("LunarAPI.enable")) {
            CC.log("&bLunarClientAPI &aEnabled");
            getServer().getPluginManager().registerEvents(new LunarClientListener(), this);
        }
        FrozenScoreboardHandler.setConfiguration(PotPvPScoreboardConfiguration.create());
        KitManager.KitManager();
    }

    @Override
    public void onDisable() {
        instance = null;

        mongoClient.close();
    }

    private void setupMongo() {
        mongoClient = new MongoClient(
            getConfig().getString("Mongo.Host"),
            getConfig().getInt("Mongo.Port")
        );

        String databaseId = getConfig().getString("Mongo.Database");
        mongoDatabase = mongoClient.getDatabase(databaseId);
    }

}