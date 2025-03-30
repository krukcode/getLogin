package pl.krukcode.getLogin.PL.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChat implements Listener {

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if(!onJoin.loggedIn.get(e.getPlayer().getUniqueId())) { e.setCancelled(true); }
    }
}
