package pl.krukcode.getLogin.EN.cmds.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.EN.events.onJoinEN;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class LoginEN implements CommandExecutor {

    Main plugin;
    PlayerData pd;

    public LoginEN(Main m) {
        plugin = m;
        m.getCommand("login").setExecutor(this);

        pd = PlayerData.getInstance();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String lab, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if(onJoinEN.loggedIn.get(uuid) == false) {
            if (args.length == 1) {
                if (pd.getData().getString(uuid + ".pass") != null) {
                    String pass = args[0];
                    String actualPass = pd.getData().getString(uuid + ".pass");

                    if(pass.equals(actualPass)) {
                        onJoinEN.loggedIn.put(uuid, true);

                        p.sendMessage("§aSuccessfully logged in!");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
                        onJoinEN.stop.put(uuid, true);
                    } else {
                        p.kickPlayer("§cInvalid password!\n§7Try again!");
                    }
                } else {
                    p.sendMessage("§7Before you login you must §eregister!");
                    p.sendMessage("§7Use command: §e/register [password] [retype pass]");
                }
            } else {
                p.sendMessage("§7Usage: §e/login [password]");
            }
        } else {
            p.sendMessage("§cYou are already logged in!");
        }
        return false;
    }
}
