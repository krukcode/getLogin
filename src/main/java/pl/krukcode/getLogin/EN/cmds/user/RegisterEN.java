package pl.krukcode.getLogin.EN.cmds.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.EN.events.onJoinEN;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Util;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class RegisterEN implements CommandExecutor {

    Main plugin;
    PlayerData pd;

    public RegisterEN(Main m) {
        plugin = m;
        m.getCommand("register").setExecutor(this);

        pd = PlayerData.getInstance();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String lab, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if(onJoinEN.loggedIn.get(uuid).equals(true)) {
            p.sendMessage(Util.fix("&cYou can't use this command after you login!"));
            return false;
        }
        if(pd.getData().getString(uuid + ".pass") == null) {
            if (args.length == 2) {
                String pass1 = args[0];

                if(args[0].equalsIgnoreCase(args[1])) {
                    pd.getData().set(uuid + ".pass", pass1);
                    pd.saveData();

                    onJoinEN.loggedIn.put(uuid, true);

                    p.sendMessage("§7Successfully registered!");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
                    onJoinEN.stop.put(uuid, true);
                } else {
                    p.sendMessage("§7Passwords aren't same!");
                }
            } else {
                p.sendMessage("§7Usage: §e/register [password] [password]");
            }
        } else {
            p.sendMessage("§7You are already registered!");
        }

        return false;
    }
}
