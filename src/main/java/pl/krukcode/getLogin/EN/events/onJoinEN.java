package pl.krukcode.getLogin.EN.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Util;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class onJoinEN implements Listener {

    public static Map<UUID, Boolean> loggedIn = new HashMap<UUID, Boolean>();
    public static Map<UUID, Boolean> stop = new HashMap<UUID, Boolean>();
    PlayerData pd;

    @SuppressWarnings("static-access")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        pd = PlayerData.getInstance();
        loggedIn.put(uuid, false);
        stop.put(uuid, false);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (stop.get(uuid).equals(true)) { cancel(); }
                if(pd.getData().getString(uuid + ".pass") == null) { p.sendMessage(Util.fix("&7Register using command: &e/register [pass] [pass]&7!")); }
                else { p.sendMessage(Util.fix("§7Login using command: §e/login [pass]§7!")); }
            }
        }.runTaskTimer(Main.getMain(), 20L, 100L);
    }
}
