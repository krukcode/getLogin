package pl.krukcode.getLogin.EN.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class onMoveEN implements Listener {

    PlayerData pd;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        pd = PlayerData.getInstance();

        if(!onJoinEN.loggedIn.get(uuid)) {
            if(pd.getData().getString(uuid + ".pass") == null) {
                e.setCancelled(true);
                p.sendMessage("§cYou must register before doing this!");
            } else {
                e.setCancelled(true);
                p.sendMessage("§cYou must login before doing this!");
            }
        }
    }

}
