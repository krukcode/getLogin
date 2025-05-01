package pl.krukcode.getLogin.PL.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pl.krukcode.Util.Util;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class onJoin implements Listener {

    public static Map<UUID, Boolean> loggedIn = new HashMap<>();
    public static Map<UUID, Boolean> stop = new HashMap<>();
    PlayerData pd;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        pd = PlayerData.getInstance();
        loggedIn.put(uuid, false);
        stop.put(uuid, false);
        if(Objects.equals(pd.getData().getString(uuid + ".autologin"), "YES")) { onJoin.loggedIn.put(uuid, true); p.sendMessage(Util.fix("&aZostałeś zalogowany pomyślnie poprzez funkcję &eAUTOLOGIN&a!")); return; }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (stop.get(uuid).equals(true)) { cancel(); }
                if(pd.getData().getString(uuid + ".pass") == null) { p.sendMessage(Util.fix("&7Zarejestruj się używając komendy: &e/register [hasło] [hasło]&7!")); }
                else { p.sendMessage(Util.fix("&7Zaloguj się używając komendy: §e/login [hasło]§7!")); }
            }
        }.runTaskTimer(Main.getMain(), 20L, 100L);
    }
}
