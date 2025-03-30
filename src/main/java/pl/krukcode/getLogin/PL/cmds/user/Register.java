package pl.krukcode.getLogin.PL.cmds.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.PL.events.onJoin;
import pl.krukcode.getLogin.Util;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class Register implements CommandExecutor {

    Main plugin;
    PlayerData pd;

    public Register(Main m) {
        plugin = m;
        m.getCommand("register").setExecutor(this);

        pd = PlayerData.getInstance();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String lab, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if(onJoin.loggedIn.get(uuid).equals(true)) {
            p.sendMessage(Util.fix("Nie możesz użyć tej komendy po zalogowaniu!"));
            return false;
        }
        if(pd.getData().getString(uuid + ".pass") == null) {
            if (args.length == 2) {
                String pass1 = args[0];

                if(args[0].equalsIgnoreCase(args[1])) {
                    pd.getData().set(uuid + ".pass", pass1);
                    pd.saveData();

                    onJoin.loggedIn.put(uuid, true);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
                    p.sendMessage("§7Zostałeś zarejestrowany!");
                    onJoin.stop.put(uuid, true);
                } else {
                    p.sendMessage("§7Te hasła się nie zgadzają!");
                }
            } else {
                p.sendMessage("§7Użycie: §e/register [haslo1] [powtorz haslo]");
            }
        } else {
            p.sendMessage("§7Jesteś już zarejestrowany!");
        }

        return false;
    }
}
