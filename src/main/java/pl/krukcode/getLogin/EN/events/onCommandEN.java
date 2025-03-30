package pl.krukcode.getLogin.EN.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Util;

import java.util.List;

public class onCommandEN implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();

        if (!onJoinEN.loggedIn.get(p.getUniqueId())) {
            List<String> allowedCommands = Main.getInstance().getConfig().getStringList("allowed_commands_before_login");
            String command = event.getMessage().split(" ")[0];
            if (!allowedCommands.contains(command)) {
                event.setCancelled(true);
                p.sendMessage(Util.fix("&cYou can't do that!"));
            }
        }
    }
}
