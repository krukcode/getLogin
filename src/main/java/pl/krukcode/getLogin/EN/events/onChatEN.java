package pl.krukcode.getLogin.EN.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChatEN implements Listener {

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if(!onJoinEN.loggedIn.get(e.getPlayer().getUniqueId())) { e.setCancelled(true); }
    }
}
