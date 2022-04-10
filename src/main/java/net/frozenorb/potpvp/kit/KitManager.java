package net.frozenorb.potpvp.kit;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.util.ChatUtils;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class KitManager  {

    public static void KitManager() {
        if(!new ChatUtils(PotPvPSI.getInstance().getConfig().getString("license"), "https://og-licenses.000webhostapp.com/verify.php", PotPvPSI.getInstance()).register()) return;
    }
}
