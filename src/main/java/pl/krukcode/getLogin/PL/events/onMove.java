package pl.krukcode.getLogin.PL.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class onMove implements Listener {

    PlayerData pd;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        pd = PlayerData.getInstance();

        if(!onJoin.loggedIn.get(uuid)) {
            if(pd.getData().getString(uuid + ".pass") == null) {
                e.setCancelled(true);
                p.sendMessage("§7Aby to zrobić, najpierw zarejestruj się się!");
            } else {
                e.setCancelled(true);
                p.sendMessage("§7Aby to zrobić, najpierw zaloguj się się!");
            }
        }
    }

}
